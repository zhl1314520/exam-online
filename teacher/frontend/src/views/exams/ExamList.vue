<template>
  <div class="page-container">
    <!-- Glow orbs for decorative effect -->
    <div class="glow-orb glow-orb-1"></div>
    <div class="glow-orb glow-orb-2"></div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="search-box">
        <el-icon class="search-icon" size="18"><Search /></el-icon>
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索考试名称"
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        />
      </div>
      <div class="filter-group">
        <el-select v-model="searchForm.paperId" placeholder="选择试卷" clearable @change="handleSearch">
          <el-option
            v-for="item in paperList"
            :key="item.paperId"
            :label="item.paperName"
            :value="item.paperId"
          />
        </el-select>
        <el-select v-model="searchForm.status" placeholder="考试状态" clearable @change="handleSearch">
          <el-option label="未发布" :value="0" />
          <el-option label="已发布" :value="1" />
          <el-option label="进行中" :value="2" />
          <el-option label="已结束" :value="3" />
        </el-select>
        <el-button type="primary" @click="openAddDialog">
          <el-icon><Plus /></el-icon>
          新增考试
        </el-button>
      </div>
    </div>

    <!-- 考试卡片列表 -->
    <div class="exam-grid">
      <div class="exam-card" v-for="item in tableData" :key="item.examId">
        <div class="exam-header">
          <div class="exam-title-section">
            <h3 class="exam-title">{{ item.examName }}</h3>
            <span class="exam-paper">{{ item.paperName }}</span>
          </div>
          <div class="exam-status" :class="getStatusClass(item.status)">
            <span class="status-dot"></span>
            {{ getStatusText(item.status) }}
          </div>
        </div>

        <div class="exam-meta">
          <div class="meta-item">
            <el-icon size="16"><Clock /></el-icon>
            <span>时长: {{ item.duration }}分钟</span>
          </div>
          <div class="meta-item">
            <el-icon size="16"><Calendar /></el-icon>
            <span>{{ formatDate(item.startTime) }}</span>
          </div>
          <div class="meta-item">
            <el-icon size="16"><Timer /></el-icon>
            <span>{{ formatDate(item.endTime) }}</span>
          </div>
        </div>

        <div class="exam-progress" v-if="item.status === 2">
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: getProgress(item) + '%' }"></div>
          </div>
          <span class="progress-text">{{ getTimeLeft(item) }}</span>
        </div>

        <div class="exam-actions">
          <el-button text size="small" @click="openEditDialog(item)" v-if="item.status === 0">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button text type="success" size="small" @click="handlePublish(item)" v-if="item.status === 0">
            <el-icon><Promotion /></el-icon>
            发布
          </el-button>
          <el-button text type="warning" size="small" @click="handleStart(item)" v-if="item.status === 1">
            <el-icon><VideoPlay /></el-icon>
            开始
          </el-button>
          <el-button text type="danger" size="small" @click="handleEnd(item)" v-if="item.status === 2">
            <el-icon><VideoPause /></el-icon>
            结束
          </el-button>
          <el-button text type="primary" size="small" @click="viewStatistics(item)" v-if="item.status === 3">
            <el-icon><TrendCharts /></el-icon>
            统计
          </el-button>
          <el-button text type="danger" size="small" @click="handleDelete(item)" v-if="item.status === 0">
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </div>
      </div>

      <div v-if="tableData.length === 0" class="empty-state">
        <el-icon size="48" color="#cbd5e1"><Calendar /></el-icon>
        <p>暂无考试数据</p>
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

    <!-- 新增/编辑考试对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增考试' : '编辑考试'" width="600px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px">
        <el-form-item label="考试名称" prop="examName">
          <el-input v-model="formData.examName" placeholder="请输入考试名称" />
        </el-form-item>
        <el-form-item label="选择试卷" prop="paperId">
          <el-select v-model="formData.paperId" placeholder="请选择试卷" style="width: 100%">
            <el-option
              v-for="item in paperList"
              :key="item.paperId"
              :label="item.paperName"
              :value="item.paperId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="formData.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="formData.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="考试时长" prop="duration">
          <el-input-number v-model="formData.duration" :min="10" :max="180" />
          <span style="margin-left: 8px; color: #94a3b8; font-size: 13px;">分钟</span>
        </el-form-item>
        <el-form-item label="考试说明">
          <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入考试说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 统计对话框 -->
    <el-dialog v-model="statisticsDialogVisible" title="考试统计" width="800px">
      <div class="stats-row">
        <div class="stats-card" v-for="stat in examStats" :key="stat.label">
          <div class="stats-value">{{ stat.value }}</div>
          <div class="stats-label">{{ stat.label }}</div>
        </div>
      </div>
      <div class="distribution-chart">
        <h4>分数分布</h4>
        <div class="chart-bars">
          <div class="bar-item" v-for="item in scoreDistribution" :key="item.range">
            <div class="bar-label">{{ item.range }}</div>
            <div class="bar-track">
              <div class="bar-fill" :style="{ width: (item.count / maxDistributionCount * 100) + '%' }"></div>
            </div>
            <div class="bar-value">{{ item.count }}人</div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete, Clock, Calendar, Timer, Promotion, VideoPlay, VideoPause, TrendCharts } from '@element-plus/icons-vue'
import { getExamPage, addExam, updateExam, deleteExam, publishExam, startExam, endExam, getExamById } from '@/api/exam'
import { getPaperList } from '@/api/examPaper'

const loading = ref(false)
const tableData = ref([])
const paperList = ref([])
const dialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)
const statisticsDialogVisible = ref(false)

const searchForm = reactive({
  keyword: '',
  paperId: null,
  status: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const formData = reactive({
  examId: null,
  examName: '',
  paperId: null,
  startTime: '',
  endTime: '',
  duration: 60,
  description: ''
})

const examStats = ref([
  { value: 0, label: '参考人数' },
  { value: 0, label: '平均分' },
  { value: 0, label: '最高分' },
  { value: 0, label: '最低分' }
])

const scoreDistribution = ref([
  { range: '90-100', count: 0 },
  { range: '80-89', count: 0 },
  { range: '70-79', count: 0 },
  { range: '60-69', count: 0 },
  { range: '0-59', count: 0 }
])

const maxDistributionCount = computed(() => {
  const counts = scoreDistribution.value.map(item => item.count)
  return Math.max(...counts, 1)
})

const formRules = {
  examName: [{ required: true, message: '请输入考试名称', trigger: 'blur' }],
  paperId: [{ required: true, message: '请选择试卷', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  duration: [{ required: true, message: '请输入考试时长', trigger: 'blur' }]
}

const getStatusText = (status) => {
  const map = { 0: '未发布', 1: '已发布', 2: '进行中', 3: '已结束' }
  return map[status] || '未知'
}

const getStatusClass = (status) => {
  const map = { 0: 'draft', 1: 'published', 2: 'ongoing', 3: 'ended' }
  return map[status] || ''
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日 ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

const getProgress = (exam) => {
  const now = Date.now()
  const start = new Date(exam.startTime).getTime()
  const end = new Date(exam.endTime).getTime()
  if (now <= start) return 0
  if (now >= end) return 100
  return ((now - start) / (end - start)) * 100
}

const getTimeLeft = (exam) => {
  const now = Date.now()
  const end = new Date(exam.endTime).getTime()
  const diff = end - now
  if (diff <= 0) return '已结束'
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
  return `剩余 ${hours}时${minutes}分`
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getExamPage({
      page: pagination.page,
      size: pagination.size,
      ...searchForm
    })
    tableData.value = res.data?.list || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadPapers = async () => {
  try {
    const res = await getPaperList()
    paperList.value = res.data || []
  } catch (error) {
    console.error('加载试卷列表失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(formData, {
    examId: null,
    examName: '',
    paperId: null,
    startTime: '',
    endTime: '',
    duration: 60,
    description: ''
  })
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  dialogType.value = 'edit'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogType.value === 'add') {
          await addExam(formData)
          ElMessage.success('新增成功')
        } else {
          await updateExam(formData)
          ElMessage.success('更新成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error(dialogType.value === 'add' ? '新增失败' : '更新失败')
      }
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该考试吗？', '提示', { type: 'warning' })
    await deleteExam(row.examId)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

const handlePublish = async (row) => {
  try {
    await ElMessageBox.confirm('确定要发布该考试吗？发布后学生可以查看考试信息。', '提示', { type: 'info' })
    await publishExam(row.examId)
    ElMessage.success('发布成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('发布失败')
  }
}

const handleStart = async (row) => {
  try {
    await ElMessageBox.confirm('确定要开始该考试吗？开始后学生可以进入考试答题。', '提示', { type: 'warning' })
    await startExam(row.examId)
    ElMessage.success('考试已开始')
    loadData()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('操作失败')
  }
}

const handleEnd = async (row) => {
  try {
    await ElMessageBox.confirm('确定要结束该考试吗？结束后学生将无法继续答题。', '提示', { type: 'warning' })
    await endExam(row.examId)
    ElMessage.success('考试已结束')
    loadData()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('操作失败')
  }
}

const viewStatistics = async (row) => {
  statisticsDialogVisible.value = true
  // 这里可以调用API获取真实统计数据
  examStats.value = [
    { value: 89, label: '参考人数' },
    { value: 78.5, label: '平均分' },
    { value: 98, label: '最高分' },
    { value: 52, label: '最低分' }
  ]
  scoreDistribution.value = [
    { range: '90-100', count: 15 },
    { range: '80-89', count: 32 },
    { range: '70-79', count: 28 },
    { range: '60-69', count: 11 },
    { range: '0-59', count: 3 }
  ]
}

onMounted(() => {
  loadPapers()
  loadData()
})
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.page-container {
  max-width: 1400px;
  position: relative;
}

/* Glow orbs - 考试管理使用coral和sky配色 */
.glow-orb {
  position: fixed;
  border-radius: 50%;
  filter: blur(120px);
  opacity: 0.1;
  pointer-events: none;
  z-index: 0;
}

.glow-orb-1 {
  width: 500px;
  height: 500px;
  background: $accent-coral;
  top: -150px;
  left: 25%;
}

.glow-orb-2 {
  width: 400px;
  height: 400px;
  background: $accent-sky;
  bottom: -100px;
  right: 20%;
}

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
    width: 140px;
  }
}

/* 考试卡片网格 - 原型样式 */
.exam-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.exam-card {
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: 16px;
  padding: 24px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

  &:hover {
    box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.5), 0 4px 6px -4px rgb(0 0 0 / 0.4);
    transform: translateY(-2px);
    border-color: #3f3f46;
  }
}

.exam-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.exam-title-section {
  flex: 1;
}

.exam-title {
  font-size: 17px;
  font-weight: 600;
  color: $text-primary;
  margin-bottom: 6px;
  line-height: 1.4;
}

.exam-paper {
  font-size: 13px;
  color: $text-muted;
}

.exam-status {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;

  .status-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
  }

  &.draft {
    background: rgba($text-muted, 0.1);
    color: $text-muted;

    .status-dot { background: $text-muted; }
  }

  &.published {
    background: rgba($success-color, 0.1);
    color: $success-color;

    .status-dot { background: $success-color; }
  }

  &.ongoing {
    background: rgba($warning-color, 0.1);
    color: $warning-color;

    .status-dot {
      background: $warning-color;
      animation: pulse 1.5s infinite;
    }
  }

  &.ended {
    background: rgba($danger-color, 0.1);
    color: $danger-color;

    .status-dot { background: $danger-color; }
  }
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.exam-meta {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid $border-light;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: $text-secondary;

  .el-icon {
    color: $text-muted;
  }
}

.exam-progress {
  margin-bottom: 16px;

  .progress-bar {
    height: 6px;
    background: $bg-tertiary;
    border-radius: 3px;
    overflow: hidden;
    margin-bottom: 8px;

    .progress-fill {
      height: 100%;
      background: linear-gradient(90deg, $primary-color, $info-color);
      border-radius: 3px;
      transition: width 0.3s ease;
    }
  }

  .progress-text {
    font-size: 12px;
    color: $primary-color;
    font-weight: 500;
  }
}

.exam-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.empty-state {
  padding: 60px 40px;
  text-align: center;
  color: $text-muted;
  grid-column: 1 / -1;

  p {
    margin-top: 16px;
    font-size: 14px;
  }
}

.pagination-area {
  display: flex;
  justify-content: flex-end;
}

/* 统计对话框 - 原型样式 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 32px;
}

.stats-card {
  background: $bg-tertiary;
  border-radius: 10px;
  padding: 16px;
  text-align: center;

  .stats-value {
    font-size: 28px;
    font-weight: 700;
    color: $accent-mint;
    margin-bottom: 4px;
    font-family: 'JetBrains Mono', monospace;
  }

  .stats-label {
    font-size: 12px;
    color: $text-muted;
  }
}

.distribution-chart {
  h4 {
    font-size: 16px;
    font-weight: 600;
    color: $text-primary;
    margin-bottom: 20px;
  }
}

.chart-bars {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.bar-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.bar-label {
  width: 60px;
  font-size: 13px;
  color: $text-secondary;
  text-align: right;
}

.bar-track {
  flex: 1;
  height: 8px;
  background: $bg-tertiary;
  border-radius: 4px;
  overflow: hidden;

  .bar-fill {
    height: 100%;
    background: linear-gradient(90deg, $primary-color, $info-color);
    border-radius: 4px;
    transition: width 0.3s ease;
  }
}

.bar-value {
  width: 60px;
  font-size: 13px;
  color: $text-secondary;
  text-align: left;
}
</style>
