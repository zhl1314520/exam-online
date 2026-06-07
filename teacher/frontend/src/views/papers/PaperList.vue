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
          placeholder="搜索试卷名称"
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        />
      </div>
      <div class="filter-group">
        <el-select v-model="searchForm.subjectId" placeholder="选择科目" clearable @change="handleSearch">
          <el-option
            v-for="item in subjectList"
            :key="item.subjectId"
            :label="item.subjectName"
            :value="item.subjectId"
          />
        </el-select>
        <el-button type="primary" @click="openAddDialog">
          <el-icon><Plus /></el-icon>
          新增试卷
        </el-button>
      </div>
    </div>

    <!-- 试卷卡片列表 -->
    <div class="paper-grid">
      <div class="paper-card" v-for="item in tableData" :key="item.paperId">
        <div class="paper-header">
          <div class="paper-title-section">
            <h3 class="paper-title">{{ item.paperName }}</h3>
            <span class="paper-subject">{{ item.subjectName }}</span>
          </div>
          <div class="paper-actions">
            <el-button text size="small" @click="openEditDialog(item)">
              <el-icon><Edit /></el-icon>
            </el-button>
            <el-button text type="danger" size="small" @click="handleDelete(item)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </div>
        </div>

        <div class="paper-stats">
          <div class="stat-item">
            <span class="stat-value">{{ item.questionCount || 0 }}</span>
            <span class="stat-label">题目数</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ item.totalScore }}</span>
            <span class="stat-label">总分</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ item.duration }}</span>
            <span class="stat-label">时长(分)</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ Math.round((item.totalScore || 0) * 0.6) }}</span>
            <span class="stat-label">及格线</span>
          </div>
        </div>

        <div class="paper-footer">
          <el-button type="primary" @click="openQuestionsDialog(item)">
            <el-icon><Document /></el-icon>
            题目管理
          </el-button>
          <el-button @click="viewDetail(item)">
            <el-icon><View /></el-icon>
            预览
          </el-button>
        </div>
      </div>

      <div v-if="tableData.length === 0" class="empty-state">
        <el-icon size="48" color="#cbd5e1"><Collection /></el-icon>
        <p>暂无试卷数据</p>
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

    <!-- 新增/编辑试卷对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增试卷' : '编辑试卷'" width="500px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="试卷名称" prop="paperName">
          <el-input v-model="formData.paperName" placeholder="请输入试卷名称" />
        </el-form-item>
        <el-form-item label="所属科目" prop="subjectId">
          <el-select v-model="formData.subjectId" placeholder="请选择科目" style="width: 100%">
            <el-option
              v-for="item in subjectList"
              :key="item.subjectId"
              :label="item.subjectName"
              :value="item.subjectId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="总分" prop="totalScore">
          <el-input-number v-model="formData.totalScore" :min="1" :max="200" />
        </el-form-item>
        <el-form-item label="考试时长" prop="duration">
          <el-input-number v-model="formData.duration" :min="10" :max="180" />
          <span style="margin-left: 8px; color: #94a3b8; font-size: 13px;">分钟</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 题目管理对话框 -->
    <el-dialog v-model="questionsDialogVisible" title="题目管理" width="900px">
      <div class="questions-header">
        <h3>{{ currentPaper?.paperName }}</h3>
        <el-button type="primary" @click="openAddQuestionDialog">
          <el-icon><Plus /></el-icon>
          添加题目
        </el-button>
      </div>
      <div class="paper-questions-list" v-loading="questionsLoading">
        <div class="question-item" v-for="(q, index) in paperQuestions" :key="q.questionId">
          <div class="question-number">{{ index + 1 }}</div>
          <div class="question-content">
            <div class="question-header">
              <span class="question-type-tag">{{ getQuestionTypeText(q.questionType) }}</span>
              <span class="question-score">{{ q.score }}分</span>
            </div>
            <div class="question-text">{{ q.questionContent }}</div>
          </div>
          <el-button text type="danger" size="small" @click="removeQuestion(index)">
            <el-icon><Delete /></el-icon>
          </el-button>
        </div>
        <div v-if="paperQuestions.length === 0" class="empty-state">
          <el-icon size="32" color="#cbd5e1"><Document /></el-icon>
          <p>暂无题目，请添加</p>
        </div>
      </div>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog v-model="previewDialogVisible" title="试卷预览" width="800px" class="preview-dialog">
      <div v-if="previewData" class="paper-preview">
        <div class="preview-header">
          <h2 class="preview-title">{{ previewData.paperName }}</h2>
          <div class="preview-info">
            <span>科目：{{ previewData.subjectName || '-' }}</span>
            <span>总分：{{ previewData.totalScore }}分</span>
            <span>时长：{{ previewData.duration }}分钟</span>
            <span>题目数：{{ previewData.questionCount || 0 }}道</span>
          </div>
        </div>
        <div class="preview-questions">
          <div v-for="(q, index) in previewData.questions" :key="q.questionId" class="preview-question-item">
            <div class="preview-question-header">
              <span class="preview-question-number">{{ index + 1 }}.</span>
              <span class="preview-question-type">{{ getQuestionTypeText(q.questionType) }}</span>
              <span class="preview-question-score">({{ q.score }}分)</span>
            </div>
            <div class="preview-question-content">{{ q.questionContent }}</div>
            <div v-if="q.options" class="preview-options">
              <div v-for="opt in parseOptions(q.options)" :key="opt.key" class="preview-option">
                {{ opt.key }}. {{ opt.value }}
              </div>
            </div>
            <div class="preview-answer">
              <strong>答案：</strong>{{ q.correctAnswer }}
            </div>
          </div>
          <div v-if="!previewData.questions || previewData.questions.length === 0" class="empty-state">
            <el-icon size="32" color="#cbd5e1"><Document /></el-icon>
            <p>暂无题目</p>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 添加题目对话框 -->
    <el-dialog v-model="addQuestionDialogVisible" title="选择题目" width="900px">
      <div class="toolbar mb-4">
        <el-input
          v-model="questionSearchForm.keyword"
          placeholder="搜索题目"
          clearable
          style="width: 300px"
          @keyup.enter="loadAvailableQuestions"
        />
        <el-select v-model="questionSearchForm.questionType" placeholder="题型" clearable style="width: 120px" @change="loadAvailableQuestions">
          <el-option label="单选题" :value="1" />
          <el-option label="多选题" :value="2" />
          <el-option label="判断题" :value="3" />
          <el-option label="填空题" :value="4" />
          <el-option label="简答题" :value="5" />
          <el-option label="编程题" :value="6" />
        </el-select>
        <el-button type="primary" @click="loadAvailableQuestions">搜索</el-button>
      </div>
      <el-table
        ref="questionTableRef"
        :data="availableQuestions"
        stripe
        v-loading="availableLoading"
        @selection-change="handleQuestionSelection"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="questionContent" label="题目内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="questionType" label="题型" width="100">
          <template #default="{ row }">
            <span class="question-type-tag">{{ getQuestionTypeText(row.questionType) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="difficulty" label="难度" width="100">
          <template #default="{ row }">
            <el-rate v-model="row.difficulty" disabled :max="3" />
          </template>
        </el-table-column>
        <el-table-column prop="score" label="分值" width="80" />
      </el-table>
      <template #footer>
        <el-button @click="addQuestionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmAddQuestions" :disabled="selectedQuestions.length === 0">
          添加选中题目 ({{ selectedQuestions.length }})
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete, Document, View, Collection } from '@element-plus/icons-vue'
import { getPaperPage, addPaper, updatePaper, deletePaper, getPaperQuestions, addQuestionToPaper, deleteQuestionFromPaper } from '@/api/examPaper'
import { getQuestionPage } from '@/api/question'
import { getSubjectList } from '@/api/subject'

const loading = ref(false)
const tableData = ref([])
const subjectList = ref([])
const dialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)

const questionsDialogVisible = ref(false)
const questionsLoading = ref(false)
const paperQuestions = ref([])
const currentPaper = ref(null)
const currentPaperId = ref(null)

const addQuestionDialogVisible = ref(false)
const availableLoading = ref(false)
const availableQuestions = ref([])
const selectedQuestions = ref([])
const questionTableRef = ref(null)

const searchForm = reactive({
  keyword: '',
  subjectId: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const formData = reactive({
  paperId: null,
  paperName: '',
  subjectId: null,
  totalScore: 100,
  duration: 60
})

const questionSearchForm = reactive({
  keyword: '',
  questionType: null,
  subjectId: null
})

const formRules = {
  paperName: [{ required: true, message: '请输入试卷名称', trigger: 'blur' }],
  subjectId: [{ required: true, message: '请选择科目', trigger: 'change' }],
  totalScore: [{ required: true, message: '请输入总分', trigger: 'blur' }],
  duration: [{ required: true, message: '请输入考试时长', trigger: 'blur' }]
}

const getQuestionTypeText = (type) => {
  const map = { 1: '单选', 2: '多选', 3: '判断', 4: '填空', 5: '简答', 6: '编程' }
  return map[type] || '未知'
}

const parseOptions = (optionsStr) => {
  if (!optionsStr) return []
  return optionsStr.split('\n').filter(opt => opt.trim()).map(opt => {
    const match = opt.match(/^([A-D])\.\s*(.+)$/)
    if (match) {
      return { key: match[1], value: match[2] }
    }
    return { key: opt.charAt(0), value: opt.substring(2).trim() }
  })
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getPaperPage({
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

const loadSubjects = async () => {
  try {
    const res = await getSubjectList()
    subjectList.value = res.data || []
  } catch (error) {
    console.error('加载科目失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(formData, {
    paperId: null,
    paperName: '',
    subjectId: null,
    totalScore: 100,
    duration: 60
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
          await addPaper(formData)
          ElMessage.success('新增成功')
        } else {
          await updatePaper(formData)
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
    await ElMessageBox.confirm('确定要删除该试卷吗？', '提示', { type: 'warning' })
    await deletePaper(row.paperId)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

const openQuestionsDialog = async (row) => {
  currentPaper.value = row
  currentPaperId.value = row.paperId
  questionsDialogVisible.value = true
  await loadPaperQuestions()
}

const loadPaperQuestions = async () => {
  questionsLoading.value = true
  try {
    const res = await getPaperQuestions(currentPaperId.value)
    paperQuestions.value = res.data || []
  } catch (error) {
    ElMessage.error('加载题目失败')
  } finally {
    questionsLoading.value = false
  }
}

const removeQuestion = async (index) => {
  const q = paperQuestions.value[index]
  try {
    await ElMessageBox.confirm('确定要移除该题目吗？', '提示', { type: 'warning' })
    await deleteQuestionFromPaper(currentPaperId.value, q.questionId)
    paperQuestions.value.splice(index, 1)
    ElMessage.success('移除成功')
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('移除失败')
  }
}

const openAddQuestionDialog = () => {
  addQuestionDialogVisible.value = true
  questionSearchForm.keyword = ''
  questionSearchForm.questionType = null
  questionSearchForm.subjectId = formData.subjectId
  loadAvailableQuestions()
}

const loadAvailableQuestions = async () => {
  availableLoading.value = true
  try {
    const res = await getQuestionPage({
      page: 1,
      size: 100,
      ...questionSearchForm
    })
    availableQuestions.value = res.data?.list || []
  } catch (error) {
    ElMessage.error('加载题目列表失败')
  } finally {
    availableLoading.value = false
  }
}

const handleQuestionSelection = (selection) => {
  selectedQuestions.value = selection
}

const confirmAddQuestions = async () => {
  if (selectedQuestions.value.length === 0) return
  try {
    const questionIds = selectedQuestions.value.map(q => q.questionId)
    await addQuestionToPaper(currentPaperId.value, { questionIds })
    ElMessage.success('添加成功')
    addQuestionDialogVisible.value = false
    loadPaperQuestions()
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

const previewDialogVisible = ref(false)
const previewData = ref(null)

const viewDetail = async (row) => {
  previewData.value = row
  previewDialogVisible.value = true
  if (row.paperId) {
    try {
      const res = await getPaperQuestions(row.paperId)
      previewData.value.questions = res.data || []
    } catch (error) {
      previewData.value.questions = []
    }
  }
}

onMounted(() => {
  loadSubjects()
  loadData()
})
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.page-container {
  max-width: 1400px;
  position: relative;
}

/* Glow orbs - 组卷管理使用sky和mint配色 */
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
  background: $accent-sky;
  top: -150px;
  right: 10%;
}

.glow-orb-2 {
  width: 400px;
  height: 400px;
  background: $accent-mint;
  bottom: -100px;
  left: 15%;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;

  &.mb-4 {
    margin-bottom: 16px;
  }
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
    width: 150px;
  }
}

/* 试卷卡片网格 - 原型样式 */
.paper-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.paper-card {
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

.paper-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.paper-title-section {
  flex: 1;
}

.paper-title {
  font-size: 18px;
  font-weight: 600;
  color: $text-primary;
  margin-bottom: 8px;
  line-height: 1.4;
}

.paper-subject {
  font-size: 13px;
  color: $text-muted;
  background: $bg-tertiary;
  padding: 4px 10px;
  border-radius: 6px;
}

.paper-actions {
  display: flex;
  gap: 4px;
}

.paper-stats {
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 20px 0;
  border-top: 1px solid $border-light;
  border-bottom: 1px solid $border-light;
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  .stat-value {
    font-size: 24px;
    font-weight: 700;
    color: $accent-mint;
    font-family: 'JetBrains Mono', monospace;
    margin-bottom: 4px;
    line-height: 1;
  }

  .stat-label {
    font-size: 11px;
    color: $text-muted;
    line-height: 1;
  }
}

.paper-footer {
  display: flex;
  gap: 12px;
}

/* 题目管理对话框 */
.questions-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid $border-light;

  h3 {
    font-size: 18px;
    font-weight: 600;
    color: $text-primary;
  }
}

.paper-questions-list {
  min-height: 300px;

  .question-item {
    display: flex;
    align-items: flex-start;
    gap: 16px;
    padding: 16px;
    background: $bg-secondary;
    border-radius: $radius-md;
    margin-bottom: 12px;

    .question-number {
      width: 28px;
      height: 28px;
      background: $primary-color;
      color: white;
      border-radius: $radius-sm;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 13px;
      font-weight: 600;
    }

    .question-content {
      flex: 1;

      .question-header {
        display: flex;
        gap: 12px;
        margin-bottom: 8px;
      }

      .question-text {
        font-size: 14px;
        color: $text-primary;
        line-height: 1.6;
      }
    }
  }
}

.question-type-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 6px;
  background: rgba($success-color, 0.1);
  color: $success-color;
  font-weight: 500;
}

.question-score {
  font-size: 12px;
  color: $text-muted;
  font-weight: 500;
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

/* 预览对话框样式 */
.preview-dialog {
  .paper-preview {
    max-height: 600px;
    overflow-y: auto;
  }

  .preview-header {
    text-align: center;
    padding-bottom: 20px;
    border-bottom: 2px solid $border-light;
    margin-bottom: 24px;

    .preview-title {
      font-size: 22px;
      font-weight: 700;
      color: $text-primary;
      margin-bottom: 12px;
    }

    .preview-info {
      display: flex;
      justify-content: center;
      gap: 24px;
      font-size: 14px;
      color: $text-secondary;

      span {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
  }

  .preview-questions {
    .preview-question-item {
      margin-bottom: 24px;
      padding-bottom: 20px;
      border-bottom: 1px solid $border-light;

      &:last-child {
        border-bottom: none;
      }

      .preview-question-header {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 12px;

        .preview-question-number {
          font-size: 16px;
          font-weight: 600;
          color: $text-primary;
        }

        .preview-question-type {
          font-size: 12px;
          padding: 2px 8px;
          border-radius: 4px;
          background: rgba($success-color, 0.1);
          color: $success-color;
        }

        .preview-question-score {
          font-size: 13px;
          color: $text-muted;
        }
      }

      .preview-question-content {
        font-size: 15px;
        color: $text-primary;
        line-height: 1.8;
        margin-bottom: 12px;
      }

      .preview-options {
        margin-bottom: 12px;

        .preview-option {
          padding: 6px 0;
          font-size: 14px;
          color: $text-secondary;
          line-height: 1.6;
        }
      }

      .preview-answer {
        font-size: 14px;
        color: $text-secondary;
        padding: 8px 12px;
        background: $bg-secondary;
        border-radius: $radius-sm;

        strong {
          color: $primary-color;
        }
      }
    }
  }
}
</style>
