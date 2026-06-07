<template>
  <div class="page-container">
    <!-- Glow orbs for decorative effect -->
    <div class="glow-orb glow-orb-1"></div>
    <div class="glow-orb glow-orb-2"></div>

    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon total">
          <el-icon size="22"><Folder /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.total }}</h3>
          <p>题目总数</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon single">
          <el-icon size="22"><CircleCheck /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.single }}</h3>
          <p>单选题</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon multiple">
          <el-icon size="22"><Check /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.multiple }}</h3>
          <p>多选题</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon judge">
          <el-icon size="22"><Help /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.judge }}</h3>
          <p>判断题</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon fill">
          <el-icon size="22"><EditPen /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.fill }}</h3>
          <p>填空题</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon short">
          <el-icon size="22"><Notebook /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.short }}</h3>
          <p>简答题</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon code">
          <el-icon size="22"><Monitor /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.code }}</h3>
          <p>编程题</p>
        </div>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="content-layout">
      <!-- 左侧科目分类 -->
      <div class="category-panel">
        <div class="panel-header">
          <span class="panel-title">科目分类</span>
          <el-button text type="primary" size="small" @click="loadSubjects">
            <el-icon><Refresh /></el-icon>
          </el-button>
        </div>
        <div class="category-list">
          <div
            class="category-item"
            :class="{ active: !searchForm.subjectId }"
            @click="selectSubject(null)"
          >
            <div class="category-icon">
              <el-icon size="18"><Folder /></el-icon>
            </div>
            <div class="category-info">
              <div class="category-name">全部科目</div>
              <div class="category-count">{{ stats.total }} 道题目</div>
            </div>
          </div>
          <div
            v-for="subject in subjectList"
            :key="subject.subjectId"
            class="category-item"
            :class="{ active: searchForm.subjectId === subject.subjectId }"
            @click="selectSubject(subject.subjectId)"
          >
            <div class="category-icon">
              <el-icon size="18"><Document /></el-icon>
            </div>
            <div class="category-info">
              <div class="category-name">{{ subject.subjectName }}</div>
              <div class="category-count">{{ subject.questionCount || 0 }} 道题目</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧题目列表 -->
      <div class="question-panel">
        <!-- 工具栏 -->
        <div class="toolbar">
          <div class="search-box">
            <el-icon class="search-icon" size="18"><Search /></el-icon>
            <el-input
              v-model="searchForm.keyword"
              placeholder="搜索题目内容、知识点..."
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            />
          </div>
          <div class="filter-group">
            <el-select v-model="searchForm.questionType" placeholder="题型" clearable @change="handleSearch">
              <el-option label="单选题" :value="1" />
              <el-option label="多选题" :value="2" />
              <el-option label="判断题" :value="3" />
              <el-option label="填空题" :value="4" />
              <el-option label="简答题" :value="5" />
              <el-option label="编程题" :value="6" />
            </el-select>
            <el-select v-model="searchForm.difficulty" placeholder="难度" clearable @change="handleSearch">
              <el-option label="简单" :value="1" />
              <el-option label="中等" :value="2" />
              <el-option label="困难" :value="3" />
            </el-select>
            <el-button type="primary" @click="openAddDialog">
              <el-icon><Plus /></el-icon>
              新增题目
            </el-button>
            <el-button @click="openBatchDialog">
              <el-icon><Upload /></el-icon>
              批量导入
            </el-button>
          </div>
        </div>

        <!-- 题目列表 -->
        <div class="question-list">
          <div class="question-item" v-for="item in tableData" :key="item.questionId">
            <div class="question-header">
              <div class="question-meta">
                <span class="question-type" :class="getQuestionTypeClass(item.questionType)">
                  {{ getQuestionTypeText(item.questionType) }}
                </span>
                <div class="question-difficulty">
                  <span
                    v-for="n in 3"
                    :key="n"
                    class="difficulty-dot"
                    :class="{ active: n <= item.difficulty }"
                  />
                </div>
                <span class="question-points">{{ item.score }}分</span>
              </div>
              <div class="question-actions">
                <el-button text type="primary" size="small" @click="openEditDialog(item)">
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button text type="danger" size="small" @click="handleDelete(item)">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
            <div class="question-content">{{ item.questionContent }}</div>
            <div class="question-options" v-if="item.options">
              <div class="option-item" v-for="(opt, idx) in parseOptions(item.options)" :key="idx">
                <span class="option-label">{{ ['A','B','C','D','E','F'][idx] }}</span>
                <span class="option-text">{{ opt }}</span>
              </div>
            </div>
            <div class="question-footer">
              <div class="question-tags">
                <span class="tag">{{ item.subjectName }}</span>
                <span class="tag" v-if="item.knowledgePoint">{{ item.knowledgePoint }}</span>
              </div>
              <div class="question-answer" v-if="item.correctAnswer">
                <span class="answer-label">答案:</span>
                <span class="answer-value">{{ item.correctAnswer }}</span>
              </div>
            </div>
          </div>

          <div v-if="tableData.length === 0" class="empty-state">
            <el-icon size="48" color="#cbd5e1"><Document /></el-icon>
            <p>暂无题目数据</p>
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
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增题目' : '编辑题目'" width="700px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
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
        <el-form-item label="题型" prop="questionType">
          <el-radio-group v-model="formData.questionType">
            <el-radio :value="1">单选题</el-radio>
            <el-radio :value="2">多选题</el-radio>
            <el-radio :value="3">判断题</el-radio>
            <el-radio :value="4">填空题</el-radio>
            <el-radio :value="5">简答题</el-radio>
            <el-radio :value="6">编程题</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="题目内容" prop="questionContent">
          <el-input v-model="formData.questionContent" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="选项" v-if="[1, 2].includes(formData.questionType)">
          <el-input v-model="formData.options" type="textarea" :rows="4" placeholder="A.选项1\nB.选项2\nC.选项3\nD.选项4" />
        </el-form-item>
        <el-form-item label="正确答案" prop="correctAnswer">
          <el-input v-model="formData.correctAnswer" placeholder="单选/判断: A/B/C/D 或 0/1; 多选: A|B|C" />
        </el-form-item>
        <el-form-item label="分值" prop="score">
          <el-input-number v-model="formData.score" :min="0.5" :max="100" :step="0.5" />
        </el-form-item>
        <el-form-item label="难度" prop="difficulty">
          <el-rate v-model="formData.difficulty" :max="3" />
        </el-form-item>
        <el-form-item label="知识点">
          <el-input v-model="formData.knowledgePoint" placeholder="请输入知识点标签" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog v-model="batchDialogVisible" title="批量导入题目" width="700px">
      <div class="batch-import-container">
        <el-alert title="支持 Excel (.xlsx) 和 Word (.docx) 格式" type="info" :closable="false" show-icon>
          <template #default>
            <div class="format-guide">
              <p><strong>Excel 格式：</strong>第一行为表头（subjectId, questionType, questionContent, options, correctAnswer, score, difficulty, knowledgePoint），从第二行开始填写题目数据</p>
              <p><strong>Word 格式：</strong>每道题以"题型："开头，空行分隔题目，详见下方模板</p>
            </div>
          </template>
        </el-alert>

        <div class="template-download">
          <el-button type="primary" link size="small" @click="downloadTemplate('excel')">📥 下载 Excel 模板</el-button>
          <el-button type="primary" link size="small" @click="downloadTemplate('word')">📥 下载 Word 模板</el-button>
        </div>

        <div class="upload-section">
          <el-upload
            ref="uploadRef"
            accept=".xlsx,.docx"
            :auto-upload="false"
            :on-change="handleFileSelect"
            :limit="1"
            :show-file-list="false"
            drag
          >
            <div class="upload-content">
              <el-icon size="40"><Upload /></el-icon>
              <p class="upload-text">将文件拖到此处，或 <em>点击上传</em></p>
              <p class="upload-tip">支持 .xlsx 和 .docx 格式</p>
            </div>
          </el-upload>
          <div v-if="selectedFile" class="file-info">
            <el-icon><Document /></el-icon>
            <span class="file-name">{{ selectedFile.name }}</span>
            <span class="file-size">({{ formatFileSize(selectedFile.size) }})</span>
            <el-button text type="primary" size="small" @click="clearFile">重新选择</el-button>
          </div>
        </div>

        <div v-if="parseLoading" class="parse-loading">
          <el-icon class="is-loading"><Loading /></el-icon>
          <span>正在解析文件...</span>
        </div>

        <div v-if="parsedData.length > 0" class="preview-section">
          <el-divider />
          <h4>解析结果预览：</h4>
          <p class="preview-count">共解析到 {{ parsedData.length }} 道题目</p>
          <el-table :data="parsedData.slice(0, 5)" size="small" border stripe>
            <el-table-column prop="questionType" label="题型" width="80">
              <template #default="{ row }">
                <span class="question-type-tag" :class="getQuestionTypeClass(row.questionType)">
                  {{ getQuestionTypeText(row.questionType) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="questionContent" label="题目内容" show-overflow-tooltip />
            <el-table-column prop="score" label="分值" width="70" />
            <el-table-column prop="difficulty" label="难度" width="70">
              <template #default="{ row }">
                <span>{{ ['','简单','中等','困难'][row.difficulty] }}</span>
              </template>
            </el-table-column>
          </el-table>
          <p v-if="parsedData.length > 5" class="more-tip">还有 {{ parsedData.length - 5 }} 道题目未显示...</p>
        </div>

        <div v-if="parseError" class="error-message">
          <el-alert :title="parseError" type="error" show-icon />
        </div>
      </div>
      <template #footer>
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleBatchImport"
          :loading="batchImporting"
          :disabled="parsedData.length === 0"
        >
          导入 ({{ parsedData.length }}条)
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Upload, Edit, Delete, Folder, Document, CircleCheck, Check, Help, EditPen, Notebook, Monitor, Loading } from '@element-plus/icons-vue'
import { getQuestionPage, getQuestionStatistics, addQuestion, updateQuestion, deleteQuestion, batchImportQuestions, parseExcelFile, parseWordFile, downloadExcelTemplate, downloadWordTemplate } from '@/api/question'
import { getSubjectList } from '@/api/subject'

const loading = ref(false)
const tableData = ref([])
const subjectList = ref([])
const dialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)

// 批量导入相关
const batchDialogVisible = ref(false)
const uploadRef = ref(null)
const selectedFile = ref(null)
const parsedData = ref([])
const parseError = ref('')
const batchImporting = ref(false)
const parseLoading = ref(false)

const stats = reactive({
  total: 0,
  single: 0,
  multiple: 0,
  judge: 0,
  fill: 0,
  short: 0,
  code: 0
})

const searchForm = reactive({
  keyword: '',
  subjectId: null,
  questionType: null,
  difficulty: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const formData = reactive({
  questionId: null,
  subjectId: null,
  questionType: 1,
  questionContent: '',
  options: '',
  correctAnswer: '',
  score: 2,
  difficulty: 2,
  knowledgePoint: ''
})

const formRules = {
  subjectId: [{ required: true, message: '请选择科目', trigger: 'change' }],
  questionType: [{ required: true, message: '请选择题型', trigger: 'change' }],
  questionContent: [{ required: true, message: '请输入题目内容', trigger: 'blur' }],
  correctAnswer: [{ required: true, message: '请输入正确答案', trigger: 'blur' }],
  score: [{ required: true, message: '请输入分值', trigger: 'blur' }],
  difficulty: [{ required: true, message: '请选择难度', trigger: 'change' }]
}

const getQuestionTypeText = (type) => {
  const map = { 1: '单选', 2: '多选', 3: '判断', 4: '填空', 5: '简答', 6: '编程' }
  return map[type] || '未知'
}

const getQuestionTypeClass = (type) => {
  const map = {
    1: 'single',
    2: 'multiple',
    3: 'judge',
    4: 'fill',
    5: 'short',
    6: 'code'
  }
  return map[type] || ''
}

const parseOptions = (optionsStr) => {
  if (!optionsStr) return []
  return optionsStr.split('\n').filter(opt => opt.trim())
}

const selectSubject = (subjectId) => {
  searchForm.subjectId = subjectId
  pagination.page = 1
  loadData()
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getQuestionPage({
      pageNum: pagination.page,
      pageSize: pagination.size,
      ...searchForm
    })
    tableData.value = res.data?.list || []
    pagination.total = res.data?.total || 0
    stats.total = res.data?.total || 0
    loadStats()
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

const loadStats = async () => {
  try {
    const res = await getQuestionStatistics()
    if (res.data) {
      stats.total = res.data.total || 0
      stats.single = res.data.single || 0
      stats.multiple = res.data.multiple || 0
      stats.judge = res.data.judge || 0
      stats.fill = res.data.fill || 0
      stats.short = res.data.short || 0
      stats.code = res.data.code || 0
    }
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(formData, {
    questionId: null,
    subjectId: null,
    questionType: 1,
    questionContent: '',
    options: '',
    correctAnswer: '',
    score: 2,
    difficulty: 2,
    knowledgePoint: ''
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
          await addQuestion(formData)
          ElMessage.success('新增成功')
        } else {
          await updateQuestion(formData)
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
    await ElMessageBox.confirm('确定要删除该题目吗？', '提示', { type: 'warning' })
    await deleteQuestion(row.questionId)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

// 批量导入相关方法
const openBatchDialog = () => {
  batchDialogVisible.value = true
  selectedFile.value = null
  parsedData.value = []
  parseError.value = ''
  parseLoading.value = false
}

const clearFile = () => {
  selectedFile.value = null
  parsedData.value = []
  parseError.value = ''
  if (uploadRef.value) uploadRef.value.clearFiles()
}

const handleFileSelect = async (uploadFile) => {
  selectedFile.value = uploadFile.raw
  parseError.value = ''
  parsedData.value = []

  if (!uploadFile.raw) {
    return
  }

  const fileName = uploadFile.name.toLowerCase()
  const isExcel = fileName.endsWith('.xlsx')
  const isWord = fileName.endsWith('.docx')

  if (!isExcel && !isWord) {
    parseError.value = '请选择 Excel (.xlsx) 或 Word (.docx) 格式的文件'
    selectedFile.value = null
    return
  }

  parseLoading.value = true
  try {
    const formData = new FormData()
    formData.append('file', uploadFile.raw)

    let res
    if (isExcel) {
      res = await parseExcelFile(formData)
    } else {
      res = await parseWordFile(formData)
    }

    if (res.data && Array.isArray(res.data)) {
      parsedData.value = res.data
      if (res.data.length === 0) {
        parseError.value = '文件中未找到有效的题目数据'
      }
    } else {
      parseError.value = '解析失败：返回数据格式错误'
    }
  } catch (error) {
    parseError.value = error.response?.data?.msg || '文件解析失败，请检查文件格式是否正确'
  } finally {
    parseLoading.value = false
  }
}

const downloadTemplate = async (type) => {
  try {
    let res
    let fileName
    if (type === 'excel') {
      res = await downloadExcelTemplate()
      fileName = '题目导入模板.xlsx'
    } else {
      res = await downloadWordTemplate()
      fileName = '题目导入模板.docx'
    }
    const blob = new Blob([res.data], {
      type: type === 'excel'
        ? 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        : 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
    })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = fileName
    link.click()
    URL.revokeObjectURL(link.href)
  } catch (error) {
    ElMessage.error('模板下载失败，请稍后重试')
  }
}

const formatFileSize = (size) => {
  if (size < 1024) return size + 'B'
  if (size < 1024 * 1024) return (size / 1024).toFixed(2) + 'KB'
  return (size / (1024 * 1024)).toFixed(2) + 'MB'
}

const handleBatchImport = async () => {
  if (parsedData.value.length === 0) {
    ElMessage.warning('没有可导入的数据')
    return
  }

  batchImporting.value = true
  try {
    const res = await batchImportQuestions(parsedData.value)
    ElMessage.success(`成功导入 ${res.data} 道题目`)
    batchDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('批量导入失败：' + (error.message || '请检查数据格式'))
  } finally {
    batchImporting.value = false
  }
}

onMounted(() => {
  loadSubjects()
  loadData()
  loadStats()
})
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.page-container {
  max-width: 1400px;
  position: relative;
}

/* Glow orbs - decorative colored blur effects */
.glow-orb {
  position: fixed;
  border-radius: 50%;
  filter: blur(120px);
  opacity: 0.12;
  pointer-events: none;
  z-index: 0;
}

.glow-orb-1 {
  width: 500px;
  height: 500px;
  background: $accent-mint;
  top: -150px;
  left: 20%;
}

.glow-orb-2 {
  width: 400px;
  height: 400px;
  background: $accent-amber;
  bottom: -100px;
  right: 15%;
}

/* 统计卡片 - 原型样式 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 12px;
  margin-bottom: 24px;
}

.stat-card {
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;

  h3 {
    font-size: 20px;
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

  &.total {
    background: rgba(245, 158, 11, 0.15);
    color: $accent-amber;
  }

  &.single {
    background: rgba(16, 185, 129, 0.15);
    color: $accent-mint;
  }

  &.multiple {
    background: rgba(14, 165, 233, 0.15);
    color: $accent-sky;
  }

  &.judge {
    background: rgba(255, 107, 107, 0.15);
    color: $accent-coral;
  }

  &.fill {
    background: rgba(139, 92, 246, 0.15);
    color: #8B5CF6;
  }

  &.short {
    background: rgba(236, 72, 153, 0.15);
    color: #EC4899;
  }

  &.code {
    background: rgba(99, 102, 241, 0.15);
    color: #6366F1;
  }
}

/* 内容布局 */
.content-layout {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 24px;
}

/* 左侧分类面板 */
.category-panel {
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: $radius-lg;
  overflow: hidden;
  height: fit-content;
}

.panel-header {
  padding: 20px;
  border-bottom: 1px solid $border-light;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .panel-title {
    font-size: 15px;
    font-weight: 600;
    color: $text-primary;
  }
}

.category-list {
  padding: 12px;
}

.category-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  margin-bottom: 4px;

  &:hover {
    background: $bg-tertiary;
  }

  &.active {
    background: rgba($success-color, 0.1);

    .category-icon {
      background: $success-color;
      color: white;
    }

    .category-name {
      color: $success-color;
      font-weight: 600;
    }
  }
}

.category-icon {
  width: 36px;
  height: 36px;
  border-radius: $radius-sm;
  display: flex;
  align-items: center;
  justify-content: center;
  background: $bg-tertiary;
  color: $text-secondary;
  transition: all $transition-fast;
}

.category-info {
  flex: 1;
}

.category-name {
  font-size: 14px;
  font-weight: 500;
  color: $text-primary;
  margin-bottom: 2px;
}

.category-count {
  font-size: 12px;
  color: $text-muted;
}

/* 右侧题目面板 */
.question-panel {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 工具栏 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
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
    width: 120px;
  }
}

/* 题目列表 */
.question-list {
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: $radius-lg;
  overflow: hidden;
  min-height: 400px;
}

.question-item {
  padding: 24px;
  border-bottom: 1px solid $border-light;
  transition: background $transition-fast;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    background: $bg-secondary;
  }
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.question-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.question-type {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;

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

  &.short {
    background: rgba(#EC4899, 0.1);
    color: #EC4899;
  }

  &.code {
    background: rgba($danger-color, 0.1);
    color: $danger-color;
  }
}

.question-difficulty {
  display: flex;
  align-items: center;
  gap: 4px;
}

.difficulty-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: $border-dark;

  &.active {
    background: $warning-color;
  }
}

.question-points {
  font-size: 13px;
  color: $text-muted;
  background: $bg-tertiary;
  padding: 4px 10px;
  border-radius: 6px;
}

.question-content {
  font-size: 15px;
  line-height: 1.7;
  color: $text-primary;
  margin-bottom: 16px;
}

.question-options {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 16px;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: $bg-tertiary;
  border-radius: $radius-md;
  font-size: 14px;
  color: $text-secondary;
}

.option-label {
  width: 24px;
  height: 24px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  background: $bg-card;
  color: $text-muted;
}

.question-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.question-tags {
  display: flex;
  gap: 8px;
}

.tag {
  padding: 4px 10px;
  background: $bg-tertiary;
  border-radius: 6px;
  font-size: 12px;
  color: $text-muted;
}

.question-answer {
  font-size: 13px;

  .answer-label {
    color: $text-muted;
    margin-right: 4px;
  }

  .answer-value {
    color: $success-color;
    font-weight: 500;
    font-family: $font-mono;
  }
}

.empty-state {
  padding: 80px 40px;
  text-align: center;
  color: $text-muted;

  p {
    margin-top: 16px;
    font-size: 14px;
  }
}

/* 分页 */
.pagination-area {
  display: flex;
  justify-content: flex-end;
  padding: 20px 24px;
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: $radius-lg;
}

/* 批量导入对话框样式 */
.batch-import-container {
  padding: 0 10px;

  .format-guide {
    p {
      margin: 4px 0;
      font-size: 13px;
      line-height: 1.6;
    }
  }
}

.template-download {
  margin-top: 12px;
  display: flex;
  align-items: center;
  gap: 8px;

  .divider {
    color: $border-base;
  }
}

.upload-section {
  margin-top: 20px;

  .upload-content {
    text-align: center;
    padding: 20px;

    .upload-text {
      margin: 12px 0 4px;
      font-size: 14px;
      color: $text-primary;

      em {
        color: $primary-color;
        font-style: normal;
      }
    }

    .upload-tip {
      font-size: 12px;
      color: $text-muted;
    }
  }
}

.parse-loading {
  margin-top: 20px;
  padding: 30px;
  text-align: center;
  color: $text-muted;

  .el-icon {
    margin-right: 8px;
    vertical-align: middle;
  }
}

.file-info {
  margin-top: 12px;
  padding: 10px 15px;
  background: $bg-tertiary;
  border-radius: 6px;
  display: flex;
  align-items: center;
  gap: 8px;

  .file-name {
    color: $text-primary;
    font-size: 13px;
  }

  .file-size {
    color: $text-muted;
    font-size: 12px;
  }
}

.question-type-tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;

  &.single { background: rgba(16, 185, 129, 0.1); color: #10B981; }
  &.multiple { background: rgba(59, 130, 246, 0.1); color: #3B82F6; }
  &.judge { background: rgba(255, 107, 107, 0.1); color: #FF6B6B; }
  &.fill { background: rgba($primary-color, 0.1); color: $primary-color; }
  &.short { background: rgba(#EC4899, 0.1); color: #EC4899; }
  &.code { background: rgba($danger-color, 0.1); color: $danger-color; }
}

.preview-section {
  margin-top: 20px;

  .preview-count {
    margin-bottom: 10px;
    color: $text-muted;
    font-size: 13px;
  }

  .more-tip {
    margin-top: 10px;
    color: $text-muted;
    font-size: 12px;
    text-align: center;
  }
}

.error-message {
  margin-top: 15px;
}
</style>
