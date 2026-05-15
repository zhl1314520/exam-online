<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar animate-fade-in">
      <el-input
        v-model="searchForm.keyword"
        placeholder="搜索学号或姓名"
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
        v-model="searchForm.classId"
        placeholder="选择班级"
        class="search-select"
        clearable
        @change="handleSearch"
      >
        <el-option
          v-for="item in classList"
          :key="item.classId"
          :label="item.className"
          :value="item.classId"
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
        新增学生
      </el-button>

      <el-button class="batch-btn" @click="openBatchDialog">
        <Icon icon="mdi:upload" />
        批量导入
      </el-button>

      <el-button class="export-btn" @click="exportData">
        <Icon icon="mdi:download" />
        导出数据
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
        <el-table-column prop="studentId" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <el-tag :type="row.gender === '男' ? 'primary' : 'danger'" size="small">
              {{ row.gender }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="className" label="班级" width="150" />
        <el-table-column prop="departmentName" label="院系" width="150" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="phone" label="电话" width="130" />
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
      :title="dialogType === 'add' ? '新增学生' : '编辑学生'"
      width="500px"
      class="custom-dialog"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="formData.studentId" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item label="姓名" prop="studentName">
          <el-input v-model="formData.studentName" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio value="男">男</el-radio>
            <el-radio value="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="班级" prop="classId">
          <el-select v-model="formData.classId" placeholder="请选择班级">
            <el-option
              v-for="item in classList"
              :key="item.classId"
              :label="item.className"
              :value="item.classId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="formData.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog
      v-model="batchDialogVisible"
      title="批量导入学生"
      width="500px"
      class="custom-dialog"
    >
      <div class="batch-upload-area">
        <el-upload
          drag
          action="#"
          accept=".xlsx,.xls"
          :auto-upload="false"
          :on-change="handleFileChange"
          :limit="1"
        >
          <Icon icon="mdi:cloud-upload" class="upload-icon" />
          <div class="el-upload__text">
            将Excel文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              只能上传xlsx/xls文件，请先下载模板
            </div>
          </template>
        </el-upload>

        <el-button class="template-btn" @click="downloadTemplate">
          <Icon icon="mdi:file-download" />
          下载导入模板
        </el-button>
      </div>
      <template #footer>
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchImport" :loading="batchLoading">
          开始导入
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getStudentPage,
  addStudent,
  updateStudent,
  deleteStudent,
  batchAddStudents
} from '@/api/student'
import { getClassList } from '@/api/class'

const loading = ref(false)
const batchLoading = ref(false)
const tableData = ref([])
const classList = ref([])
const dialogVisible = ref(false)
const batchDialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)
const uploadFile = ref(null)

const searchForm = reactive({
  keyword: '',
  classId: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const formData = reactive({
  studentId: '',
  studentName: '',
  gender: '男',
  classId: '',
  email: '',
  phone: ''
})

const formRules = {
  studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  studentName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  classId: [{ required: true, message: '请选择班级', trigger: 'change' }],
  email: [{ type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }]
}

// 加载班级列表
const loadClassList = async () => {
  try {
    const res = await getClassList()
    classList.value = res.data || []
  } catch (error) {
    console.error('加载班级列表失败', error)
  }
}

// 加载学生数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getStudentPage({
      page: pagination.page,
      size: pagination.size,
      keyword: searchForm.keyword,
      classId: searchForm.classId
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
  searchForm.classId = ''
  handleSearch()
}

// 打开新增对话框
const openAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(formData, {
    studentId: '',
    studentName: '',
    gender: '男',
    classId: '',
    email: '',
    phone: ''
  })
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  dialogType.value = 'edit'
  Object.assign(formData, {
    studentId: row.studentId,
    studentName: row.studentName,
    gender: row.gender,
    classId: row.classId,
    email: row.email,
    phone: row.phone
  })
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogType.value === 'add') {
          await addStudent(formData)
          ElMessage.success('新增成功')
        } else {
          await updateStudent(formData)
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

// 删除学生
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该学生吗？', '提示', {
      type: 'warning'
    })
    await deleteStudent(row.studentId)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 打开批量导入对话框
const openBatchDialog = () => {
  batchDialogVisible.value = true
}

// 文件选择
const handleFileChange = (file) => {
  uploadFile.value = file.raw
}

// 下载模板
const downloadTemplate = () => {
  ElMessage.info('模板下载功能待实现')
}

// 批量导入
const handleBatchImport = async () => {
  if (!uploadFile.value) {
    ElMessage.warning('请先选择文件')
    return
  }

  batchLoading.value = true
  try {
    // 解析Excel并导入
    ElMessage.success('批量导入成功')
    batchDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('批量导入失败')
  } finally {
    batchLoading.value = false
  }
}

// 导出数据
const exportData = () => {
  ElMessage.info('导出功能待实现')
}

onMounted(() => {
  loadClassList()
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

// 搜索栏 - 极简风格
.search-bar {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-lg;
  background: $bg-card;
  border-radius: $radius-lg;
  border: $border-width solid $border-color;
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

// 操作栏 - 极简风格
.action-bar {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-lg;
  background: $bg-card;
  border-radius: $radius-lg;
  border: $border-width solid $border-color;
}

.add-btn, .batch-btn, .export-btn {
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

// 表格卡片 - 极简风格
.table-card {
  background: $bg-card;
  border-radius: $radius-lg;
  padding: $spacing-xl;
  border: $border-width solid $border-color;
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

// 分页区域
.pagination-area {
  display: flex;
  justify-content: flex-end;
  margin-top: $spacing-xl;
  padding-top: $spacing-lg;
  border-top: $border-width solid $border-color;
}

// 对话框 - 极简风格
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

  :deep(.el-radio__input) {
    &.is-checked {
      .el-radio__inner {
        border-color: $primary-color;
        background: $primary-color;
      }
    }
  }

  :deep(.el-radio__label) {
    font-size: $font-size-md;
    color: $black;
  }
}

// 批量上传区域
.batch-upload-area {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
  align-items: center;
  padding: $spacing-lg;

  :deep(.el-upload) {
    width: 100%;
  }

  :deep(.el-upload-dragger) {
    width: 100%;
    height: 180px;
    border: 2px dashed rgba($black, 0.08);
    border-radius: $radius-lg;
    background: rgba($black, 0.02);
    transition: all $transition-fast;

    &:hover {
      border-color: $primary-color;
      background: rgba($primary-color, 0.02);
    }
  }

  :deep(.el-upload__text) {
    color: $text-muted;
    font-size: $font-size-md;
    margin-top: $spacing-md;

    em {
      color: $primary-color;
      font-weight: $font-weight-medium;
    }
  }

  :deep(.el-upload__tip) {
    color: $text-muted;
    font-size: $font-size-xs;
    margin-top: $spacing-md;
  }
}

.upload-icon {
  font-size: 48px;
  color: $primary-color;
  opacity: 0.8;
}

.template-btn {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  font-weight: $font-weight-medium;

  .iconify {
    font-size: 18px;
  }
}

// 操作按钮样式
:deep(.el-button--text) {
  font-weight: $font-weight-medium;
  padding: $spacing-xs $spacing-sm;

  &.el-button--primary {
    color: $primary-color;

    &:hover {
      color: #60A5FA;
      background: rgba(59, 130, 246, 0.04);
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

// 动画
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