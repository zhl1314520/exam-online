<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar animate-fade-in">
      <el-input
        v-model="searchForm.keyword"
        placeholder="搜索院系名称"
        class="search-input"
        clearable
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <Icon icon="mdi:search" />
        </template>
      </el-input>

      <el-button type="primary" class="search-btn" @click="handleSearch">
        <Icon icon="mdi:filter" />
        筛选
      </el-button>

      <el-button class="reset-btn" @click="resetSearch">
        <Icon icon="mdi:refresh" />
        重置
      </el-button>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar animate-fade-in" style="animation-delay: 0.1s">
      <el-button type="primary" class="add-btn" @click="openAddDialog">
        <Icon icon="mdi:plus" />
        新增院系
      </el-button>
    </div>

    <!-- 数据表格 -->
    <div class="table-card animate-fade-in" style="animation-delay: 0.2s">
      <el-table
        :data="tableData"
        stripe
        class="custom-table"
        v-loading="loading"
      >
        <el-table-column prop="departmentName" label="院系名称" width="200" />
        <el-table-column prop="departmentCode" label="院系代码" width="120" />
        <el-table-column prop="teacherCount" label="教师人数" width="100">
          <template #default="{ row }">
            <el-tag type="primary" size="small">{{ row.teacherCount || 0 }}人</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="classCount" label="班级数量" width="100">
          <template #default="{ row }">
            <el-tag type="info" size="small">{{ row.classCount || 0 }}个</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="studentCount" label="学生人数" width="100">
          <template #default="{ row }">
            <el-tag type="success" size="small">{{ row.studentCount || 0 }}人</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="subjectNames" label="下属学科" min-width="200">
          <template #default="{ row }">
            <span v-if="row.subjectNames">{{ row.subjectNames }}</span>
            <span v-else style="color: #c0c4cc;">暂无</span>
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

      <!-- 分页 -->
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增院系' : '编辑院系'"
      width="600px"
      class="custom-dialog"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="院系名称" prop="departmentName">
          <el-input v-model="formData.departmentName" />
        </el-form-item>
        <el-form-item label="院系代码" prop="departmentCode">
          <el-input v-model="formData.departmentCode" />
        </el-form-item>
        <el-form-item label="下属学科">
          <div class="subject-manager">
            <div class="subject-add-bar">
              <el-input v-model="newSubject.subjectName" placeholder="学科名称" style="width: 140px;" size="small" />
              <el-input v-model="newSubject.subjectCode" placeholder="学科代码" style="width: 120px;" size="small" />
              <el-input-number v-model="newSubject.credit" :min="0.5" :max="10" :step="0.5" :precision="1" placeholder="学分" style="width: 100px;" size="small" />
              <el-button type="primary" size="small" @click="addSubjectItem" :disabled="dialogType === 'add'">
                <Icon icon="mdi:plus" />
                添加
              </el-button>
            </div>
            <div v-if="dialogType === 'add'" class="subject-hint">请先保存院系后再添加学科</div>
            <div v-else class="subject-list">
              <div v-for="(item, index) in subjectList" :key="item.subjectId" class="subject-item">
                <div class="subject-info">
                  <span class="subject-name">{{ item.subjectName }}</span>
                  <el-tag size="small" type="info">{{ item.subjectCode }}</el-tag>
                  <el-tag size="small" type="warning">{{ item.credit }}学分</el-tag>
                </div>
                <div class="subject-actions">
                  <el-button text type="primary" size="small" @click="openEditSubject(item)">
                    <Icon icon="mdi:pencil" />编辑
                  </el-button>
                  <el-button text type="danger" size="small" @click="handleDeleteSubject(item)">
                    <Icon icon="mdi:delete" />删除
                  </el-button>
                </div>
              </div>
              <div v-if="subjectList.length === 0" class="subject-empty">暂无学科</div>
            </div>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 学科编辑对话框 -->
    <el-dialog
      v-model="subjectEditVisible"
      title="编辑学科"
      width="400px"
      class="custom-dialog"
      append-to-body
    >
      <el-form ref="subjectFormRef" :model="subjectEditData" :rules="subjectFormRules" label-width="80px">
        <el-form-item label="学科名称" prop="subjectName">
          <el-input v-model="subjectEditData.subjectName" />
        </el-form-item>
        <el-form-item label="学科代码" prop="subjectCode">
          <el-input v-model="subjectEditData.subjectCode" />
        </el-form-item>
        <el-form-item label="学分" prop="credit">
          <el-input-number v-model="subjectEditData.credit" :min="0.5" :max="10" :step="0.5" :precision="1" style="width: 100%;" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="subjectEditVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEditSubject">确定</el-button>
      </template>
    </el-dialog>

    <!-- 删除确认对话框 -->
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

    <!-- 结果提示对话框 -->
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
import {
  getDepartmentPage,
  addDepartment,
  updateDepartment,
  deleteDepartment
} from '@/api/department'
import {
  getSubjectsByDepartment,
  addSubject,
  updateSubject,
  deleteSubject
} from '@/api/subject'

const loading = ref(false)
const deleteLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const resultDialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)
const currentDeleteRow = ref(null)

// 结果弹窗状态
const resultType = ref('success')
const resultTitle = ref('')
const resultMessage = ref('')

const searchForm = reactive({
  keyword: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const formData = reactive({
  departmentId: '',
  departmentName: '',
  departmentCode: '',
  description: ''
})

const formRules = {
  departmentName: [{ required: true, message: '请输入院系名称', trigger: 'blur' }],
  departmentCode: [{ required: true, message: '请输入院系代码', trigger: 'blur' }]
}

const subjectList = ref([])
const newSubject = reactive({
  subjectName: '',
  subjectCode: '',
  credit: 3.0
})
const subjectEditVisible = ref(false)
const subjectFormRef = ref(null)
const subjectEditData = reactive({
  subjectId: '',
  subjectName: '',
  subjectCode: '',
  credit: 3.0
})
const subjectFormRules = {
  subjectName: [{ required: true, message: '请输入学科名称', trigger: 'blur' }],
  subjectCode: [{ required: true, message: '请输入学科代码', trigger: 'blur' }]
}

const loadSubjects = async (departmentId) => {
  if (!departmentId) {
    subjectList.value = []
    return
  }
  try {
    const res = await getSubjectsByDepartment(departmentId)
    subjectList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载学科失败')
  }
}

const addSubjectItem = async () => {
  if (!newSubject.subjectName || !newSubject.subjectCode) {
    ElMessage.warning('请填写学科名称和代码')
    return
  }
  try {
    await addSubject({
      subjectName: newSubject.subjectName,
      subjectCode: newSubject.subjectCode,
      departmentId: formData.departmentId,
      credit: newSubject.credit
    })
    ElMessage.success('学科添加成功')
    Object.assign(newSubject, { subjectName: '', subjectCode: '', credit: 3.0 })
    loadSubjects(formData.departmentId)
    loadData()
  } catch (error) {
    const errorMsg = error?.response?.data?.message || error?.message || '添加失败'
    ElMessage.error(errorMsg)
  }
}

const openEditSubject = (item) => {
  Object.assign(subjectEditData, {
    subjectId: item.subjectId,
    subjectName: item.subjectName,
    subjectCode: item.subjectCode,
    credit: item.credit,
    departmentId: formData.departmentId
  })
  subjectEditVisible.value = true
}

const handleEditSubject = async () => {
  if (!subjectFormRef.value) return
  await subjectFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await updateSubject(subjectEditData)
        ElMessage.success('学科修改成功')
        subjectEditVisible.value = false
        loadSubjects(formData.departmentId)
        loadData()
      } catch (error) {
        const errorMsg = error?.response?.data?.message || error?.message || '修改失败'
        ElMessage.error(errorMsg)
      }
    }
  })
}

const handleDeleteSubject = async (item) => {
  try {
    await ElMessageBox.confirm(`确认删除学科"${item.subjectName}"？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteSubject(item.subjectId)
    ElMessage.success('学科删除成功')
    loadSubjects(formData.departmentId)
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      const errorMsg = error?.response?.data?.message || error?.message || '删除失败'
      ElMessage.error(errorMsg)
    }
  }
}

// 加载院系数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getDepartmentPage({
      pageNum: pagination.page,
      pageSize: pagination.size,
      keyword: searchForm.keyword
    })
    tableData.value = res.data?.list || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadData()
}

// 重置搜索
const resetSearch = () => {
  searchForm.keyword = ''
  handleSearch()
}

// 打开新增对话框
const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(formData, {
    departmentId: '',
    departmentName: '',
    departmentCode: '',
    description: ''
  })
  subjectList.value = []
  Object.assign(newSubject, { subjectName: '', subjectCode: '', credit: 3.0 })
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  dialogType.value = 'edit'
  Object.assign(formData, {
    departmentId: row.departmentId,
    departmentName: row.departmentName,
    departmentCode: row.departmentCode,
    description: row.description
  })
  Object.assign(newSubject, { subjectName: '', subjectCode: '', credit: 3.0 })
  loadSubjects(row.departmentId)
  dialogVisible.value = true
}

// 显示结果弹窗
const showResultDialog = (type, title, message) => {
  resultType.value = type
  resultTitle.value = title
  resultMessage.value = message
  resultDialogVisible.value = true
}

// 关闭结果弹窗
const closeResultDialog = () => {
  resultDialogVisible.value = false
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogType.value === 'add') {
          await addDepartment(formData)
          dialogVisible.value = false
          showResultDialog('success', '新增成功！', '院系信息已成功添加到系统')
          loadData()
        } else {
          await updateDepartment(formData)
          dialogVisible.value = false
          showResultDialog('success', '编辑成功！', '院系信息已成功更新')
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

// 打开删除确认对话框
const handleDelete = (row) => {
  currentDeleteRow.value = row
  deleteDialogVisible.value = true
}

// 确认删除
const confirmDelete = async () => {
  if (!currentDeleteRow.value) return

  deleteLoading.value = true
  try {
    await deleteDepartment(currentDeleteRow.value.departmentId)
    deleteDialogVisible.value = false
    showResultDialog('success', '删除成功！', '院系信息已成功删除')
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

.subject-manager {
  width: 100%;
}

.subject-add-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.subject-hint {
  color: #c0c4cc;
  font-size: 13px;
  line-height: 32px;
}

.subject-list {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  max-height: 240px;
  overflow-y: auto;
}

.subject-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }
}

.subject-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.subject-name {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.subject-actions {
  display: flex;
  align-items: center;
  gap: 4px;
}

.subject-empty {
  padding: 20px;
  text-align: center;
  color: #c0c4cc;
  font-size: 13px;
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
/* 确认对话框样式 */
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

/* 结果弹窗样式 */
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

/* 遮罩层优化 */
:deep(.el-overlay) {
  background: rgba(0, 0, 0, 0.45);
}
</style>