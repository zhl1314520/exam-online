<template>
  <div class="page-container">
    <div class="search-bar animate-fade-in">
      <el-input
        v-model="searchForm.keyword"
        placeholder="搜索考试名称"
        class="search-input"
        clearable
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <Icon icon="mdi:search" />
        </template>
      </el-input>

      <el-select v-model="searchForm.status" placeholder="考试状态" clearable style="width: 140px;" @change="handleSearch">
        <el-option label="未开始" :value="0" />
        <el-option label="进行中" :value="1" />
        <el-option label="已结束" :value="2" />
        <el-option label="已阅卷" :value="3" />
      </el-select>

      <el-button type="primary" class="search-btn" @click="handleSearch">
        <Icon icon="mdi:filter" />
        筛选
      </el-button>

      <el-button class="reset-btn" @click="resetSearch">
        <Icon icon="mdi:refresh" />
        重置
      </el-button>
    </div>

    <div class="action-bar animate-fade-in" style="animation-delay: 0.1s">
      <el-button type="primary" class="add-btn" @click="openAddDialog">
        <Icon icon="mdi:plus" />
        新增考试
      </el-button>
    </div>

    <div class="table-card animate-fade-in" style="animation-delay: 0.2s">
      <el-table
        :data="tableData"
        stripe
        class="custom-table"
        v-loading="loading"
      >
        <el-table-column prop="examName" label="考试名称" min-width="220" show-overflow-tooltip />
        <el-table-column prop="className" label="班级" width="140" show-overflow-tooltip />
        <el-table-column prop="startTime" label="开始时间" width="170">
          <template #default="{ row }">
            {{ formatTime(row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="170">
          <template #default="{ row }">
            {{ formatTime(row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)" size="small">{{ statusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submitCount" label="提交人数" width="100">
          <template #default="{ row }">
            <el-tag type="info" size="small">{{ row.submitCount || 0 }}人</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="passRate" label="及格率" width="100">
          <template #default="{ row }">
            <template v-if="row.status >= 2">
              <span v-if="row.passRate !== null && row.passRate !== undefined" :style="{ color: row.passRate >= 60 ? '#52c41a' : '#ff4d4f', fontWeight: 600 }">
                {{ row.passRate }}%
              </span>
              <span v-else style="color: #c0c4cc;">暂无</span>
            </template>
            <span v-else style="color: #c0c4cc;">--</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button text type="primary" @click="openEditDialog(row)">
              <Icon icon="mdi:pencil" />
              编辑
            </el-button>
            <el-button text type="danger" @click="handleDelete(row)">
              <Icon icon="mdi:delete" />
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-area">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增考试' : '编辑考试'"
      width="500px"
      class="custom-dialog"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="考试名称" prop="examName">
          <el-input v-model="formData.examName" />
        </el-form-item>
        <el-form-item label="试卷" prop="paperId">
          <el-select v-model="formData.paperId" placeholder="请选择试卷" style="width: 100%;">
            <el-option v-for="p in paperOptions" :key="p.paperId" :label="p.paperName" :value="p.paperId" />
          </el-select>
        </el-form-item>
        <el-form-item label="班级" prop="classId">
          <el-select v-model="formData.classId" placeholder="请选择班级" style="width: 100%;">
            <el-option v-for="c in classOptions" :key="c.classId" :label="c.className" :value="c.classId" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="formData.startTime" type="datetime" placeholder="选择开始时间" style="width: 100%;" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="formData.endTime" type="datetime" placeholder="选择结束时间" style="width: 100%;" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" style="width: 100%;">
            <el-option label="未开始" :value="0" />
            <el-option label="进行中" :value="1" />
            <el-option label="已结束" :value="2" />
            <el-option label="已阅卷" :value="3" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="deleteDialogVisible"
      :show-close="false"
      width="400px"
      class="confirm-dialog"
      align-center
    >
      <div class="confirm-content">
        <div class="confirm-icon warning">
          <Icon icon="mdi:alert" />
        </div>
        <div class="confirm-title">确认删除</div>
        <div class="confirm-desc">删除后数据无法恢复，请谨慎操作</div>
      </div>
      <template #footer>
        <div class="confirm-footer">
          <el-button class="cancel-btn" @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="primary" class="confirm-btn" @click="confirmDelete" :loading="deleteLoading">确认删除</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      v-model="resultDialogVisible"
      :show-close="false"
      width="400px"
      class="result-dialog"
      align-center
    >
      <div class="result-content">
        <div class="result-icon" :class="resultType">
          <Icon :icon="resultType === 'success' ? 'mdi:check-circle' : 'mdi:close-circle'" />
        </div>
        <div class="result-title">{{ resultTitle }}</div>
        <div class="result-desc">{{ resultMessage }}</div>
      </div>
      <template #footer>
        <div class="result-footer">
          <el-button type="primary" class="result-btn" @click="closeResultDialog">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { getExamPage, addExam, updateExam, deleteExam } from '@/api/exam'
import { getClassList } from '@/api/class'
import { getPaperList } from '@/api/paper'

const loading = ref(false)
const deleteLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const resultDialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)
const currentDeleteRow = ref(null)
const paperOptions = ref([])
const classOptions = ref([])

const resultType = ref('success')
const resultTitle = ref('')
const resultMessage = ref('')

const searchForm = reactive({
  keyword: '',
  status: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const formData = reactive({
  examId: '',
  examName: '',
  paperId: '',
  classId: '',
  startTime: '',
  endTime: '',
  status: 0,
  allowScreenSwitch: 3,
  createBy: ''
})

const formRules = {
  examName: [{ required: true, message: '请输入考试名称', trigger: 'blur' }],
  paperId: [{ required: true, message: '请选择试卷', trigger: 'change' }],
  classId: [{ required: true, message: '请选择班级', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
}

const statusText = (status) => {
  const map = { 0: '未开始', 1: '进行中', 2: '已结束', 3: '已阅卷' }
  return map[status] ?? '未知'
}

const statusTagType = (status) => {
  const map = { 0: 'info', 1: 'warning', 2: 'danger', 3: 'success' }
  return map[status] ?? 'info'
}

const formatTime = (time) => {
  if (!time) return '--'
  return time.replace('T', ' ')
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getExamPage({
      pageNum: pagination.page,
      pageSize: pagination.size,
      keyword: searchForm.keyword || undefined,
      status: searchForm.status !== null && searchForm.status !== '' ? searchForm.status : undefined
    })
    tableData.value = res.data?.list || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadOptions = async () => {
  try {
    const [paperRes, classRes] = await Promise.all([
      getPaperList(),
      getClassList()
    ])
    paperOptions.value = paperRes.data || []
    classOptions.value = classRes.data || []
  } catch (error) {
    // ignore
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const resetSearch = () => {
  searchForm.keyword = ''
  searchForm.status = null
  handleSearch()
}

const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(formData, {
    examId: '',
    examName: '',
    paperId: '',
    classId: '',
    startTime: '',
    endTime: '',
    status: 0,
    allowScreenSwitch: 3,
    createBy: ''
  })
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  dialogType.value = 'edit'
  Object.assign(formData, {
    examId: row.examId,
    examName: row.examName,
    paperId: row.paperId,
    classId: row.classId,
    startTime: row.startTime ? row.startTime.replace('T', ' ') : '',
    endTime: row.endTime ? row.endTime.replace('T', ' ') : '',
    status: row.status,
    allowScreenSwitch: row.allowScreenSwitch,
    createBy: row.createBy
  })
  dialogVisible.value = true
}

const showResultDialog = (type, title, message) => {
  resultType.value = type
  resultTitle.value = title
  resultMessage.value = message
  resultDialogVisible.value = true
}

const closeResultDialog = () => {
  resultDialogVisible.value = false
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogType.value === 'add') {
          await addExam(formData)
          dialogVisible.value = false
          showResultDialog('success', '新增成功！', '考试信息已成功添加到系统')
          loadData()
        } else {
          await updateExam(formData)
          dialogVisible.value = false
          showResultDialog('success', '编辑成功！', '考试信息已成功更新')
          loadData()
        }
      } catch (error) {
        const action = dialogType.value === 'add' ? '新增' : '编辑'
        const errorMsg = error?.response?.data?.message || error?.message || '网络异常，请稍后重试'
        showResultDialog('error', `${action}失败！`, errorMsg)
      }
    }
  })
}

const handleDelete = (row) => {
  currentDeleteRow.value = row
  deleteDialogVisible.value = true
}

const confirmDelete = async () => {
  if (!currentDeleteRow.value) return

  deleteLoading.value = true
  try {
    await deleteExam(currentDeleteRow.value.examId)
    deleteDialogVisible.value = false
    showResultDialog('success', '删除成功！', '考试信息已成功删除')
    currentDeleteRow.value = null
    loadData()
  } catch (error) {
    const errorMsg = error?.response?.data?.message || error?.message || '网络异常，请稍后重试'
    showResultDialog('error', '删除失败！', errorMsg)
  } finally {
    deleteLoading.value = false
  }
}

onMounted(() => {
  loadData()
  loadOptions()
})
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.page-container {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-lg;
  background: $bg-card;
  border-radius: $radius-lg;
  border: $border-width solid #E2E8F0;
}

.search-input {
  width: 240px;
}

.search-btn, .reset-btn {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  font-weight: $font-weight-medium;

  .iconify {
    font-size: 18px;
  }
}

.action-bar {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-lg;
  background: $bg-card;
  border-radius: $radius-lg;
  border: $border-width solid #E2E8F0;
}

.add-btn {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  font-weight: $font-weight-medium;
  height: 40px;
  padding: 0 $spacing-md;

  .iconify {
    font-size: 18px;
  }
}

.table-card {
  background: $bg-card;
  border-radius: $radius-lg;
  padding: $spacing-xl;
  border: $border-width solid #E2E8F0;
}

.custom-table {
  width: 100%;

  :deep(.el-table__header) {
    th {
      font-weight: $font-weight-semibold;
      font-size: $font-size-sm;
      color: $black;
      background: rgba($black, 0.02);
      border-bottom: none;
      padding: $spacing-lg 0;
    }
  }

  :deep(.el-table__body) {
    td {
      padding: $spacing-md 0;
      border-bottom: $border-width solid rgba($black, 0.04);
      font-size: $font-size-md;
      color: $black;
    }

    tr:last-child td {
      border-bottom: none;
    }
  }

  :deep(.el-table__row) {
    transition: background $transition-fast;

    &:hover > td {
      background: rgba($primary-color, 0.02) !important;
    }
  }
}

.pagination-area {
  display: flex;
  justify-content: flex-end;
  margin-top: $spacing-xl;
  padding-top: $spacing-lg;
  border-top: $border-width solid #E2E8F0;
}

.custom-dialog {
  :deep(.el-dialog__header) {
    background: $bg-sidebar;
    color: #FFFFFF;
    padding: $spacing-lg $spacing-xl;
    border-bottom: none;
  }

  :deep(.el-dialog__title) {
    font-size: $font-size-lg;
    font-weight: $font-weight-semibold;
    color: #FFFFFF;
  }

  :deep(.el-dialog__body) {
    padding: $spacing-xl;
  }

  :deep(.el-form-item__label) {
    font-weight: $font-weight-medium;
    color: $black;
    font-size: $font-size-md;
  }
}

:deep(.el-button--text) {
  font-weight: $font-weight-medium;
  padding: $spacing-xs $spacing-sm;

  &.el-button--primary {
    color: $primary-color;

    &:hover {
      color: $primary-color-light;
      background: rgba($primary-color, 0.04);
    }
  }

  &.el-button--danger {
    color: $danger-color;

    &:hover {
      color: lighten($danger-color, 10%);
      background: rgba($danger-color, 0.04);
    }
  }

  .iconify {
    font-size: 16px;
    margin-right: $spacing-xs;
  }
}

.animate-fade-in {
  animation: fadeIn $transition-normal $ease-minimal forwards;
  opacity: 0;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>

<style>
.confirm-dialog {
  :deep(.el-dialog__header) {
    display: none;
  }

  :deep(.el-dialog__body) {
    padding: 32px 32px 24px;
  }

  :deep(.el-dialog__footer) {
    padding: 0 32px 32px;
    border-top: none;
  }
}

.confirm-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.confirm-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
  font-size: 24px;

  &.warning {
    background: #fff7e6;
    color: #ff7d00;
  }
}

.confirm-title {
  font-size: 16px;
  font-weight: 600;
  color: #1d2129;
  line-height: 1.5;
  margin-bottom: 8px;
}

.confirm-desc {
  font-size: 14px;
  color: #86909c;
  line-height: 1.6;
}

.confirm-footer {
  display: flex;
  flex-direction: row-reverse;
  justify-content: center;
  gap: 12px;
}

.cancel-btn {
  min-width: 88px;
  height: 36px;
  padding: 0 20px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 6px;
  border: none;
  background: #f2f3f5;
  color: #4e5969;
  transition: all 0.2s ease;

  &:hover {
    background: #e5e6eb;
    color: #4e5969;
  }
}

.confirm-btn {
  min-width: 88px;
  height: 36px;
  padding: 0 20px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 6px;
  border: none;
  background: #165dff;
  color: #ffffff;
  box-shadow: 0 2px 8px rgba(22, 93, 255, 0.2);
  transition: all 0.2s ease;

  &:hover {
    background: #4080ff;
    box-shadow: 0 4px 12px rgba(22, 93, 255, 0.3);
    transform: translateY(-1px);
  }
}

.result-dialog {
  :deep(.el-dialog__header) {
    display: none;
  }

  :deep(.el-dialog__body) {
    padding: 32px 32px 24px;
  }

  :deep(.el-dialog__footer) {
    padding: 0 32px 32px;
    border-top: none;
  }
}

.result-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.result-icon {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
  font-size: 28px;

  &.success {
    background: #e6f7e6;
    color: #52c41a;
  }

  &.error {
    background: #fff2f0;
    color: #ff4d4f;
  }
}

.result-title {
  font-size: 18px;
  font-weight: 600;
  color: #1d2129;
  line-height: 1.5;
  margin-bottom: 8px;
}

.result-desc {
  font-size: 14px;
  color: #86909c;
  line-height: 1.6;
}

.result-footer {
  display: flex;
  justify-content: center;
}

.result-btn {
  min-width: 100px;
  height: 40px;
  padding: 0 24px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 6px;
  border: none;
  transition: all 0.2s ease;

  &:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}

:deep(.el-overlay) {
  background: rgba(0, 0, 0, 0.45);
}
</style>
