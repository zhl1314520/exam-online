<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar animate-fade-in">
      <el-input
        v-model="searchForm.keyword"
        placeholder="搜索班级名称"
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
        <el-table-column prop="departmentName" label="所属院系" width="150" />
        <el-table-column prop="studentCount" label="学生人数" width="100">
          <template #default="{ row }">
            <el-tag type="primary" size="small">{{ row.studentCount || 0 }}人</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="advisorName" label="班主任" width="120">
          <template #default="{ row }">
            <span class="advisor-tag">{{ row.advisorName || '未设置' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="establishYear" label="建立年份" width="100" />
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
        <el-form-item label="班主任" prop="advisorId">
          <el-select v-model="formData.advisorId" placeholder="请选择班主任" clearable>
            <el-option
              v-for="item in teacherList"
              :key="item.teacherId"
              :label="item.teacherName"
              :value="item.teacherId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="建立年份" prop="establishYear">
          <el-date-picker
            v-model="formData.establishYear"
            type="year"
            placeholder="选择年份"
            value-format="YYYY"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getClassPage,
  addClass,
  updateClass,
  deleteClass
} from '@/api/class'
import { getDepartmentList } from '@/api/department'
import { getTeacherList } from '@/api/teacher'

const loading = ref(false)
const tableData = ref([])
const departmentList = ref([])
const teacherList = ref([])
const dialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)

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
  classId: '',
  className: '',
  departmentId: '',
  advisorId: '',
  establishYear: ''
})

const formRules = {
  className: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
  departmentId: [{ required: true, message: '请选择院系', trigger: 'change' }]
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

// 加载教师列表
const loadTeacherList = async () => {
  try {
    const res = await getTeacherList()
    teacherList.value = res.data || []
  } catch (error) {
    console.error('加载教师列表失败', error)
  }
}

// 加载班级数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getClassPage({
      page: pagination.page,
      size: pagination.size,
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
    classId: '',
    className: '',
    departmentId: '',
    advisorId: '',
    establishYear: ''
  })
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  dialogType.value = 'edit'
  Object.assign(formData, {
    classId: row.classId,
    className: row.className,
    departmentId: row.departmentId,
    advisorId: row.advisorId,
    establishYear: row.establishYear?.toString()
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
          await addClass(formData)
          ElMessage.success('新增成功')
        } else {
          await updateClass(formData)
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

// 删除班级
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该班级吗？', '提示', {
      type: 'warning'
    })
    await deleteClass(row.classId)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadDepartmentList()
  loadTeacherList()
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

.advisor-tag {
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