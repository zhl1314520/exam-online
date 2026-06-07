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
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <el-tag :type="row.gender === 1 ? 'primary' : 'danger'" size="small">
              {{ row.gender === 1 ? '男' : '女' }}
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
        <el-form-item label="学号" prop="studentNo">
          <el-input v-model="formData.studentNo" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="formData.realName" />
        </el-form-item>
        <el-form-item label="登录密码" prop="password">
          <el-input v-model="formData.password" :placeholder="dialogType === 'edit' ? '留空则不修改密码' : '默认密码123456'" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio :value="1">男</el-radio>
            <el-radio :value="0">女</el-radio>
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
          accept=".json"
          :auto-upload="false"
          :on-change="handleFileChange"
          :limit="1"
        >
          <Icon icon="mdi:cloud-upload" class="upload-icon" />
          <div class="el-upload__text">
            将JSON文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              只能上传JSON文件，请先下载模板
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
const deleteLoading = ref(false)
const tableData = ref([])
const classList = ref([])
const dialogVisible = ref(false)
const batchDialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const resultDialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)
const uploadFile = ref(null)
const currentDeleteRow = ref(null)

// 结果弹窗状态
const resultType = ref('success')
const resultTitle = ref('')
const resultMessage = ref('')

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
  studentId: null,
  studentNo: '',
  realName: '',
  password: '123456',
  gender: 1,
  classId: '',
  email: '',
  phone: ''
})

const formRules = {
  studentNo: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
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
    ElMessage.error('加载班级列表失败')
  }
}

// 加载学生数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getStudentPage({
      pageNum: pagination.page,
      pageSize: pagination.size,
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
    studentId: null,
    studentNo: '',
    realName: '',
    password: '123456',
    gender: 1,
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
    studentNo: row.studentNo,
    realName: row.realName,
    password: '',
    gender: row.gender,
    classId: row.classId,
    email: row.email,
    phone: row.phone
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
          await addStudent(formData)
          dialogVisible.value = false
          showResultDialog('success', '新增成功！', '学生信息已成功添加到系统')
          loadData()
        } else {
          // 对于编辑，密码字段如果是空字符串则不更新密码
          const updateData = { ...formData }
          if (!updateData.password) {
            delete updateData.password
          }
          await updateStudent(updateData)
          dialogVisible.value = false
          showResultDialog('success', '编辑成功！', '学生信息已成功更新')
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
    await deleteStudent(currentDeleteRow.value.studentId)
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
  // 创建示例数据（JSON格式直接对应后端API期望的结构）
  const templateData = [
    {
      studentNo: '2021001',
      realName: '张三',
      gender: 1,
      classId: 1,
      email: 'zhangsan@example.com',
      phone: '13800138000',
      password: '123456'
    },
    {
      studentNo: '2021002',
      realName: '李四',
      gender: 0,
      classId: 1,
      email: 'lisi@example.com',
      phone: '13800138001',
      password: '123456'
    }
  ]

  // 创建JSON文件并下载
  const jsonStr = JSON.stringify(templateData, null, 2)
  const blob = new Blob([jsonStr], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = '学生导入模板.json'
  a.click()
  URL.revokeObjectURL(url)

  ElMessage.success('模板下载成功')
}

// 批量导入
const handleBatchImport = async () => {
  if (!uploadFile.value) {
    ElMessage.warning('请先选择文件')
    return
  }

  batchLoading.value = true
  const reader = new FileReader()

  reader.onload = async (e) => {
    try {
      const jsonStr = e.target.result
      const students = JSON.parse(jsonStr)

      // 验证是否为数组
      if (!Array.isArray(students)) {
        throw new Error('JSON文件内容必须是数组格式')
      }

      // 验证必要字段
      const requiredFields = ['studentNo', 'realName', 'gender']
      for (const student of students) {
        for (const field of requiredFields) {
          if (student[field] === undefined || student[field] === null) {
            throw new Error(`缺少必要字段: ${field}`)
          }
        }
      }

      // 调用批量导入API
      await batchAddStudents(students)

      ElMessage.success('批量导入成功')
      batchDialogVisible.value = false
      loadData()
    } catch (error) {
      console.error('解析JSON失败:', error)
      ElMessage.error('JSON格式错误: ' + error.message)
    } finally {
      batchLoading.value = false
    }
  }

  reader.onerror = () => {
    batchLoading.value = false
    ElMessage.error('文件读取失败')
  }

  reader.readAsText(uploadFile.value)
}

// 导出数据
const exportData = async () => {
  try {
    // 加载所有数据
    const res = await getStudentPage({
      pageNum: 1,
      pageSize: 99999, // 获取所有数据
      keyword: searchForm.keyword,
      classId: searchForm.classId
    })

    if (!res.data?.list || res.data.list.length === 0) {
      ElMessage.warning('暂无数据可导出')
      return
    }

    // 创建 Excel 工作簿
    const { utils, writeFile } = await import('xlsx')

    // 准备数据
    const exportData = res.data.list.map(item => ({
      '学号': item.studentNo,
      '姓名': item.realName,
      '性别': item.gender === 1 ? '男' : '女',
      '班级': item.className || '',
      '院系': item.departmentName || '',
      '邮箱': item.email || '',
      '电话': item.phone || ''
    }))

    // 创建工作表
    const ws = utils.json_to_sheet(exportData)

    // 设置列宽
    const colWidths = [
      { wch: 15 }, // 学号
      { wch: 10 }, // 姓名
      { wch: 8 },  // 性别
      { wch: 15 }, // 班级
      { wch: 15 }, // 院系
      { wch: 25 }, // 邮箱
      { wch: 15 }  // 电话
    ]
    ws['!cols'] = colWidths

    // 创建工作簿并添加工作表
    const wb = utils.book_new()
    utils.book_append_sheet(wb, ws, '学生列表')

    // 导出文件
    const filename = `学生列表_${new Date().toISOString().slice(0, 10)}.xlsx`
    writeFile(wb, filename)

    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
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

<style>
/* 确认对话框样式 - 参考 Arco Design */
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

/* 遮罩层优化 */
:deep(.el-overlay) {
  background: rgba(0, 0, 0, 0.45);
}
</style>