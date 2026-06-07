<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar animate-fade-in">
      <el-input
        v-model="searchForm.keyword"
        placeholder="搜索班级名称或代码"
        class="search-input"
        clearable
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <Icon icon="mdi:search" />
        </template>
      </el-input>

      <el-select
        v-model="searchForm.departmentId"
        placeholder="选择院系"
        class="search-select"
        clearable
        @change="handleSearch"
      >
        <el-option
          v-for="item in departmentList"
          :key="item.departmentId"
          :label="item.departmentName"
          :value="item.departmentId"
        />
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

    <!-- 操作栏 -->
    <div class="action-bar animate-fade-in" style="animation-delay: 0.1s">
      <el-button type="primary" class="add-btn" @click="openAddDialog">
        <Icon icon="mdi:plus" />
        新增班级
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
        <el-table-column prop="className" label="班级名称" width="150" />
        <el-table-column prop="classCode" label="班级代码" width="120" />
        <el-table-column prop="departmentName" label="所属院系" width="150" />
        <el-table-column prop="grade" label="年级" width="100">
          <template #default="{ row }">
            <span class="grade-tag">{{ row.grade || '未设置' }}级</span>
          </template>
        </el-table-column>
        <el-table-column prop="studentCount" label="学生人数" width="100">
          <template #default="{ row }">
            <el-tag type="primary" size="small">{{ row.studentCount || 0 }}人</el-tag>
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
      :title="dialogType === 'add' ? '新增班级' : '编辑班级'"
      width="500px"
      class="custom-dialog"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="班级名称" prop="className">
          <el-input v-model="formData.className" />
        </el-form-item>
        <el-form-item label="班级代码" prop="classCode">
          <el-input v-model="formData.classCode" />
        </el-form-item>
        <el-form-item label="所属院系" prop="departmentId">
          <el-select v-model="formData.departmentId" placeholder="请选择院系">
            <el-option
              v-for="item in departmentList"
              :key="item.departmentId"
              :label="item.departmentName"
              :value="item.departmentId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-select v-model="formData.grade" placeholder="请选择年级">
            <el-option label="2026级" :value="2026" />
            <el-option label="2025级" :value="2025" />
            <el-option label="2024级" :value="2024" />
            <el-option label="2023级" :value="2023" />
            <el-option label="2022级" :value="2022" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
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
import { ElMessage } from 'element-plus'
import {
  getClassPage,
  addClass,
  updateClass,
  deleteClass
} from '@/api/class'
import { getDepartmentList } from '@/api/department'

const loading = ref(false)
const deleteLoading = ref(false)
const tableData = ref([])
const departmentList = ref([])
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
  keyword: '',
  departmentId: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const formData = reactive({
  classId: null,
  className: '',
  classCode: '',
  departmentId: '',
  grade: ''
})

const formRules = {
  className: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
  classCode: [{ required: true, message: '请输入班级代码', trigger: 'blur' }],
  departmentId: [{ required: true, message: '请选择院系', trigger: 'change' }],
  grade: [{ required: true, message: '请选择年级', trigger: 'change' }]
}

// 加载院系列表
const loadDepartmentList = async () => {
  try {
    const res = await getDepartmentList()
    departmentList.value = res.data || []
  } catch (error) {
    console.error('加载院系列表失败', error)
  }
}

// 加载班级数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getClassPage({
      pageNum: pagination.page,
      pageSize: pagination.size,
      keyword: searchForm.keyword,
      departmentId: searchForm.departmentId
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
  searchForm.departmentId = ''
  handleSearch()
}

// 打开新增对话框
const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(formData, {
    classId: null,
    className: '',
    classCode: '',
    departmentId: '',
    grade: ''
  })
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  dialogType.value = 'edit'
  Object.assign(formData, {
    classId: row.classId,
    className: row.className,
    classCode: row.classCode,
    departmentId: row.departmentId,
    grade: row.grade
  })
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
          await addClass(formData)
          dialogVisible.value = false
          showResultDialog('success', '新增成功！', '班级信息已成功添加到系统')
          loadData()
        } else {
          await updateClass(formData)
          dialogVisible.value = false
          showResultDialog('success', '编辑成功！', '班级信息已成功更新')
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
    await deleteClass(currentDeleteRow.value.classId)
    ElMessage.success('删除成功')
    deleteDialogVisible.value = false
    currentDeleteRow.value = null
    loadData()
  } catch (error) {
    ElMessage.error('删除失败: ' + (error.message || '未知错误'))
  } finally {
    deleteLoading.value = false
  }
}

onMounted(() => {
  loadDepartmentList()
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

.search-select {
  width: 160px;
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

.grade-tag {
  color: #6A6A6A;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
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
    color: $bg-primary;
    padding: $spacing-lg $spacing-xl;
    border-bottom: none;
  }

  :deep(.el-dialog__title) {
    font-size: $font-size-lg;
    font-weight: $font-weight-semibold;
    color: $bg-primary;
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

  &.error {
    background: #ffece8;
    color: #f53f3f;
  }

  &.success {
    background: #e8f5e9;
    color: #00b42a;
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

:deep(.el-overlay) {
  background: rgba(0, 0, 0, 0.45);
}
</style>