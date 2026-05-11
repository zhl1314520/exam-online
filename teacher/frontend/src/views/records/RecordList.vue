<template>
  <div class="page-container">

    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon primary">
          <el-icon size="22"><User /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.totalStudents }}</h3>
          <p>参考人数</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon success">
          <el-icon size="22"><TrendCharts /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.avgScore }}</h3>
          <p>平均分</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon warning">
          <el-icon size="22"><Trophy /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.maxScore }}</h3>
          <p>最高分</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon danger">
          <el-icon size="22"><Warning /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.minScore }}</h3>
          <p>最低分</p>
        </div>
      </div>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="search-box">
        <el-icon class="search-icon" size="18"><Search /></el-icon>
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索学生姓名/学号"
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        />
      </div>
      <div class="filter-group">
        <el-select v-model="searchForm.examId" placeholder="选择考试" clearable @change="handleSearch">
          <el-option
            v-for="item in examList"
            :key="item.examId"
            :label="item.examName"
            :value="item.examId"
          />
        </el-select>
        <el-button @click="resetSearch">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
        <el-button type="primary" @click="exportData">
          <el-icon><Download /></el-icon>
          导出成绩
        </el-button>
      </div>
    </div>

    <!-- 成绩列表 -->
    <div class="record-list">
      <div class="record-item" v-for="row in tableData" :key="row.recordId">
        <div class="record-main">
          <div class="student-info">
            <div class="student-avatar">
              <el-icon size="20" color="#3b82f6"><User /></el-icon>
            </div>
            <div class="student-details">
              <div class="student-name">{{ row.studentName }}</div>
              <div class="student-id">{{ row.studentNo }}</div>
            </div>
          </div>
          <div class="exam-info">
            <div class="exam-name">{{ row.examName }}</div>
            <div class="exam-time">{{ row.submitTime }}</div>
          </div>
        </div>
        <div class="record-stats">
          <div class="score-main" :class="getScoreClass(row.totalScore, row.examTotalScore)">
            <span class="score-value">{{ row.totalScore }}</span>
            <span class="score-total">/ {{ row.examTotalScore }}</span>
          </div>
          <div class="score-breakdown">
            <span>客观: {{ row.objectiveScore }}</span>
            <span>主观: {{ row.subjectiveScore }}</span>
          </div>
        </div>
        <div class="record-actions">
          <span class="status-badge" :class="getStatusClass(row.status)">
            {{ getStatusText(row.status) }}
          </span>
          <div class="action-buttons">
            <el-button text type="primary" size="small" @click="viewDetail(row)">
              <el-icon><View /></el-icon>
              详情
            </el-button>
            <el-button text type="warning" size="small" @click="openScoreDialog(row)" v-if="row.status === 1">
              <el-icon><Edit /></el-icon>
              评分
            </el-button>
          </div>
        </div>
      </div>

      <div v-if="tableData.length === 0" class="empty-state">
        <el-icon size="48" color="#cbd5e1"><Document /></el-icon>
        <p>暂无成绩数据</p>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-area">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="loadData"
        @current-change="loadData"
      />
    </div>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="答题详情" width="900px">
      <div class="student-detail-header">
        <div class="detail-avatar">
          <el-icon size="32" color="#3b82f6"><User /></el-icon>
        </div>
        <div class="detail-info">
          <h3>{{ currentRecord.studentName }}</h3>
          <p>{{ currentRecord.studentNo }}</p>
        </div>
        <div class="detail-score">
          <span class="score-label">总分</span>
          <span class="score-value">{{ currentRecord.totalScore }}</span>
          <span class="score-total">/ {{ currentRecord.examTotalScore }}</span>
        </div>
      </div>

      <div class="score-grid">
        <div class="score-item">
          <span class="label">客观题</span>
          <span class="value">{{ currentRecord.objectiveScore }}</span>
        </div>
        <div class="score-item">
          <span class="label">主观题</span>
          <span class="value">{{ currentRecord.subjectiveScore }}</span>
        </div>
        <div class="score-item">
          <span class="label">提交时间</span>
          <span class="value">{{ currentRecord.submitTime }}</span>
        </div>
      </div>

      <div class="answers-section">
        <h4>答题情况</h4>
        <div class="answer-list">
          <div class="answer-item" v-for="item in answerDetails" :key="item.answerId">
            <div class="answer-header">
              <span class="answer-type" :class="getQuestionTypeClass(item.questionType)">
                {{ getQuestionTypeText(item.questionType) }}
              </span>
              <span class="answer-score">
                <span :class="{ 'score-wrong': item.studentScore === 0, 'score-correct': item.studentScore === item.score }">
                  {{ item.studentScore }}
                </span>
                / {{ item.score }}
              </span>
            </div>
            <div class="answer-content">
              <div class="question">{{ item.questionContent }}</div>
              <div class="answers-comparison">
                <div class="answer-row">
                  <span class="label">学生答案:</span>
                  <span class="value student">{{ item.studentAnswer || '未作答' }}</span>
                </div>
                <div class="answer-row">
                  <span class="label">正确答案:</span>
                  <span class="value correct">{{ item.correctAnswer }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 评分对话框 -->
    <el-dialog v-model="scoreDialogVisible" title="主观题评分" width="800px">
      <div class="scoring-header">
        <span class="student-name">{{ currentRecord.studentName }}</span>
        <span class="student-id">{{ currentRecord.studentNo }}</span>
      </div>
      <div class="scoring-list">
        <div class="scoring-item" v-for="item in subjectiveQuestions" :key="item.answerId">
          <div class="scoring-question">
            <span class="question-type">简答题</span>
            <span class="question-score">满分: {{ item.score }}</span>
          </div>
          <div class="question-content">{{ item.questionContent }}</div>
          <div class="student-answer">
            <span class="label">学生答案:</span>
            <p>{{ item.studentAnswer }}</p>
          </div>
          <div class="scoring-input">
            <span class="label">评分:</span>
            <el-input-number v-model="item.teacherScore" :min="0" :max="item.score" size="default" />
            <span class="max-score">/ {{ item.score }} 分</span>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="scoreDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitScores">提交评分</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Download, User, TrendCharts, Trophy, Warning, View, Edit, Document } from '@element-plus/icons-vue'
import { getExamRecordsPage, getRecordById, getRecordAnswers } from '@/api/examRecord'
import { getExamList } from '@/api/exam'

const loading = ref(false)
const tableData = ref([])
const examList = ref([])
const detailDialogVisible = ref(false)
const scoreDialogVisible = ref(false)
const currentRecord = ref({})
const answerDetails = ref([])
const subjectiveQuestions = ref([])

const stats = reactive({
  totalStudents: 0,
  avgScore: 0,
  maxScore: 0,
  minScore: 0
})

const searchForm = reactive({
  keyword: '',
  examId: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const getQuestionTypeText = (type) => {
  const map = { 1: '单选', 2: '多选', 3: '判断', 4: '填空', 5: '简答' }
  return map[type] || '未知'
}

const getQuestionTypeClass = (type) => {
  const map = { 1: 'single', 2: 'multiple', 3: 'judge', 4: 'fill', 5: 'code' }
  return map[type] || ''
}

const getStatusText = (status) => {
  const map = { 0: '未提交', 1: '待评分', 2: '已完成' }
  return map[status] || '未知'
}

const getStatusClass = (status) => {
  const map = { 0: 'pending', 1: 'warning', 2: 'completed' }
  return map[status] || ''
}

const getScoreClass = (score, total) => {
  const percentage = (score / total) * 100
  if (percentage >= 90) return 'excellent'
  if (percentage >= 70) return 'good'
  if (percentage >= 60) return 'pass'
  return 'fail'
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getExamRecordsPage(searchForm.examId, {
      page: pagination.page,
      size: pagination.size,
      keyword: searchForm.keyword
    })
    tableData.value = res.data?.list || []
    pagination.total = res.data?.total || 0

    // 计算统计数据
    if (tableData.value.length > 0) {
      stats.totalStudents = pagination.total
      const scores = tableData.value.map(item => item.totalScore)
      stats.avgScore = (scores.reduce((a, b) => a + b, 0) / scores.length).toFixed(1)
      stats.maxScore = Math.max(...scores)
      stats.minScore = Math.min(...scores)
    }
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadExams = async () => {
  try {
    const res = await getExamList()
    examList.value = res.data || []
  } catch (error) {
    console.error('加载考试列表失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const resetSearch = () => {
  searchForm.keyword = ''
  searchForm.examId = null
  handleSearch()
}

const exportData = () => {
  ElMessage.info('导出功能待实现')
}

const viewDetail = async (row) => {
  currentRecord.value = row
  detailDialogVisible.value = true
  try {
    const res = await getRecordAnswers(row.recordId)
    answerDetails.value = res.data || []
  } catch (error) {
    ElMessage.error('加载答题详情失败')
  }
}

const openScoreDialog = async (row) => {
  currentRecord.value = row
  scoreDialogVisible.value = true
  try {
    const res = await getRecordAnswers(row.recordId)
    subjectiveQuestions.value = (res.data || [])
      .filter(item => [4, 5].includes(item.questionType))
      .map(item => ({ ...item, teacherScore: item.studentScore || 0 }))
  } catch (error) {
    ElMessage.error('加载题目失败')
  }
}

const submitScores = async () => {
  ElMessage.info('评分功能待实现')
  scoreDialogVisible.value = false
}

onMounted(() => {
  loadExams()
  loadData()
})
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.page-container {
  max-width: 1400px;
  position: relative;
}


/* 统计卡片 - 原型样式 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;

  h3 {
    font-size: 24px;
    font-weight: 700;
    color: $text-primary;
    margin-bottom: 4px;
    font-family: 'JetBrains Mono', monospace;
  }

  p {
    font-size: 13px;
    color: $text-muted;
  }
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;

  &.primary {
    background: rgba(16, 185, 129, 0.15);
    color: $accent-mint;
  }

  &.success {
    background: rgba(16, 185, 129, 0.15);
    color: $accent-mint;
  }

  &.warning {
    background: rgba(245, 158, 11, 0.15);
    color: $accent-amber;
  }

  &.danger {
    background: rgba(255, 107, 107, 0.15);
    color: $accent-coral;
  }
}

/* 工具栏 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

.search-box {
  flex: 1;
  max-width: 400px;
  position: relative;

  .search-icon {
    position: absolute;
    left: 14px;
    top: 50%;
    transform: translateY(-50%);
    color: $text-muted;
    z-index: 1;
  }

  :deep(.el-input__wrapper) {
    padding-left: 40px;
  }
}

.filter-group {
  display: flex;
  gap: 12px;

  .el-select {
    width: 200px;
  }
}

/* 成绩列表 */
.record-list {
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: $radius-lg;
  overflow: hidden;
  margin-bottom: 24px;
}

.record-item {
  display: flex;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid $border-light;
  transition: background $transition-fast;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    background: $bg-secondary;
  }
}

.record-main {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 40px;
}

.student-info {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 200px;

  .student-avatar {
    width: 40px;
    height: 40px;
    border-radius: 10px;
    background: rgba($primary-color, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .student-name {
    font-size: 15px;
    font-weight: 600;
    color: $text-primary;
    margin-bottom: 2px;
  }

  .student-id {
    font-size: 12px;
    color: $text-muted;
    font-family: $font-mono;
  }
}

.exam-info {
  flex: 1;

  .exam-name {
    font-size: 14px;
    color: $text-primary;
    margin-bottom: 4px;
  }

  .exam-time {
    font-size: 12px;
    color: $text-muted;
  }
}

.record-stats {
  width: 150px;
  text-align: center;

  .score-main {
    margin-bottom: 4px;

    .score-value {
      font-size: 24px;
      font-weight: 700;
      font-family: $font-mono;
    }

    .score-total {
      font-size: 14px;
      color: $text-muted;
    }

    &.excellent {
      .score-value { color: $success-color; }
    }

    &.good {
      .score-value { color: $info-color; }
    }

    &.pass {
      .score-value { color: $warning-color; }
    }

    &.fail {
      .score-value { color: $danger-color; }
    }
  }

  .score-breakdown {
    font-size: 12px;
    color: $text-muted;
    display: flex;
    gap: 12px;
    justify-content: center;
  }
}

.record-actions {
  width: 180px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: flex-end;
}

.status-badge {
  font-size: 12px;
  font-weight: 500;
  padding: 4px 12px;
  border-radius: 20px;

  &.pending {
    background: rgba($text-muted, 0.1);
    color: $text-muted;
  }

  &.warning {
    background: rgba($warning-color, 0.1);
    color: $warning-color;
  }

  &.completed {
    background: rgba($success-color, 0.1);
    color: $success-color;
  }
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.empty-state {
  padding: 60px 40px;
  text-align: center;
  color: $text-muted;

  p {
    margin-top: 16px;
    font-size: 14px;
  }
}

.pagination-area {
  display: flex;
  justify-content: flex-end;
}

/* 详情对话框 */
.student-detail-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: $bg-secondary;
  border-radius: $radius-lg;
  margin-bottom: 20px;

  .detail-avatar {
    width: 60px;
    height: 60px;
    border-radius: $radius-lg;
    background: rgba($primary-color, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .detail-info {
    flex: 1;

    h3 {
      font-size: 18px;
      font-weight: 600;
      color: $text-primary;
      margin-bottom: 4px;
    }

    p {
      font-size: 14px;
      color: $text-muted;
    }
  }

  .detail-score {
    text-align: right;

    .score-label {
      font-size: 12px;
      color: $text-muted;
      margin-bottom: 4px;
      display: block;
    }

    .score-value {
      font-size: 32px;
      font-weight: 700;
      color: $text-primary;
      font-family: $font-mono;
    }

    .score-total {
      font-size: 16px;
      color: $text-muted;
    }
  }
}

.score-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 24px;

  .score-item {
    background: $bg-secondary;
    border-radius: $radius-md;
    padding: 16px;
    text-align: center;

    .label {
      font-size: 12px;
      color: $text-muted;
      margin-bottom: 8px;
      display: block;
    }

    .value {
      font-size: 18px;
      font-weight: 600;
      color: $text-primary;
      font-family: $font-mono;
    }
  }
}

.answers-section {
  h4 {
    font-size: 16px;
    font-weight: 600;
    color: $text-primary;
    margin-bottom: 16px;
  }
}

.answer-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.answer-item {
  background: $bg-secondary;
  border-radius: $radius-md;
  padding: 16px;
}

.answer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.answer-type {
  font-size: 12px;
  font-weight: 500;
  padding: 4px 10px;
  border-radius: 6px;

  &.single {
    background: rgba($success-color, 0.1);
    color: $success-color;
  }

  &.multiple {
    background: rgba($info-color, 0.1);
    color: $info-color;
  }

  &.judge {
    background: rgba($warning-color, 0.1);
    color: $warning-color;
  }

  &.fill {
    background: rgba($primary-color, 0.1);
    color: $primary-color;
  }

  &.code {
    background: rgba($danger-color, 0.1);
    color: $danger-color;
  }
}

.answer-score {
  font-size: 14px;
  font-weight: 600;

  .score-correct { color: $success-color; }
  .score-wrong { color: $danger-color; }
}

.answer-content {
  .question {
    font-size: 14px;
    color: $text-primary;
    margin-bottom: 12px;
    line-height: 1.5;
  }
}

.answers-comparison {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.answer-row {
  display: flex;
  gap: 8px;
  font-size: 13px;

  .label {
    color: $text-muted;
    width: 70px;
  }

  .value {
    flex: 1;

    &.student {
      color: $text-primary;
    }

    &.correct {
      color: $success-color;
      font-weight: 500;
    }
  }
}

/* 评分对话框 */
.scoring-header {
  padding: 16px 20px;
  background: $bg-secondary;
  border-radius: $radius-md;
  margin-bottom: 20px;

  .student-name {
    font-size: 16px;
    font-weight: 600;
    color: $text-primary;
  }

  .student-id {
    font-size: 13px;
    color: $text-muted;
    margin-left: 8px;
  }
}

.scoring-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  max-height: 500px;
  overflow-y: auto;
}

.scoring-item {
  background: $bg-secondary;
  border-radius: $radius-md;
  padding: 20px;
}

.scoring-question {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;

  .question-type {
    font-size: 12px;
    padding: 4px 10px;
    background: rgba($danger-color, 0.1);
    color: $danger-color;
    border-radius: 6px;
    font-weight: 500;
  }

  .question-score {
    font-size: 13px;
    color: $text-muted;
  }
}

.question-content {
  font-size: 14px;
  color: $text-primary;
  margin-bottom: 12px;
  line-height: 1.5;
}

.student-answer {
  background: $bg-card;
  border-radius: $radius-sm;
  padding: 12px;
  margin-bottom: 12px;

  .label {
    font-size: 12px;
    color: $text-muted;
    margin-bottom: 8px;
    display: block;
  }

  p {
    font-size: 13px;
    color: $text-primary;
    line-height: 1.5;
  }
}

.scoring-input {
  display: flex;
  align-items: center;
  gap: 12px;

  .label {
    font-size: 14px;
    color: $text-secondary;
    font-weight: 500;
  }

  .max-score {
    font-size: 14px;
    color: $text-muted;
  }
}
</style>
