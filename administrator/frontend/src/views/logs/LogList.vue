<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar animate-fade-in">
      <el-select
        v-model="searchForm.userType"
        placeholder="用户类型"
        class="search-select"
        clearable
      >
        <el-option label="管理员" :value="1" />
        <el-option label="教师" :value="2" />
        <el-option label="学生" :value="3" />
      </el-select>

      <el-select
        v-model="searchForm.operation"
        placeholder="操作类型"
        class="search-select"
        clearable
      >
        <el-option label="登录" value="登录" />
        <el-option label="新增" value="新增" />
        <el-option label="修改" value="修改" />
        <el-option label="删除" value="删除" />
        <el-option label="查看" value="查看" />
        <el-option label="查询" value="查询" />
        <el-option label="导出" value="导出" />
        <el-option label="导入" value="导入" />
        <el-option label="审核" value="审核" />
        <el-option label="发布" value="发布" />
      </el-select>

      <el-date-picker
        v-model="searchForm.dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        class="search-date"
        value-format="YYYY-MM-DD"
      />

      <el-button type="primary" class="search-btn" @click="handleSearch">
        <Icon icon="mdi:filter" />
        筛选
      </el-button>

      <el-button class="reset-btn" @click="resetSearch">
        <Icon icon="mdi:refresh" />
        重置
      </el-button>
    </div>

    <!-- 数据表格 -->
    <div class="table-card animate-fade-in" style="animation-delay: 0.1s">
      <el-table
        :data="tableData"
        stripe
        class="custom-table"
        v-loading="loading"
      >
        <el-table-column prop="logId" label="日志ID" width="80" />
        <el-table-column prop="operatorType" label="用户类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getUserTypeTag(row.userType)" size="small">
              {{ getUserTypeName(row.userType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="操作用户" width="120" />
        <el-table-column prop="operation" label="操作类型" width="100">
          <template #default="{ row }">
            <span class="operation-tag" :class="getOperationClass(row.operation)">
              {{ row.operation }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="operationDetail" label="操作描述" min-width="250" />
        <el-table-column prop="ipAddress" label="IP地址" width="130" />
        <el-table-column prop="createTime" label="操作时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button text type="primary" @click="viewDetail(row)">
              <Icon icon="mdi:eye" />
              详情
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

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="日志详情"
      width="600px"
      class="custom-dialog"
    >
      <div class="detail-content">
        <div class="detail-item">
          <span class="detail-label">日志ID：</span>
          <span class="detail-value">{{ logDetail.logId }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">用户类型：</span>
          <el-tag :type="getUserTypeTag(logDetail.userType)" size="small">
            {{ getUserTypeName(logDetail.userType) }}
          </el-tag>
        </div>
        <div class="detail-item">
          <span class="detail-label">操作用户：</span>
          <span class="detail-value">{{ logDetail.userName }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">用户ID：</span>
          <span class="detail-value">{{ logDetail.userId }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">操作类型：</span>
          <span class="operation-tag" :class="getOperationClass(logDetail.operation)">
            {{ logDetail.operation }}
          </span>
        </div>
        <div class="detail-item">
          <span class="detail-label">操作描述：</span>
          <span class="detail-value">{{ logDetail.operationDetail }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">IP地址：</span>
          <span class="detail-value">{{ logDetail.ipAddress }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">操作时间：</span>
          <span class="detail-value">{{ formatDateTime(logDetail.createTime) }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { ElMessage } from 'element-plus'
import { getLogPage, getLogById } from '@/api/log'

const loading = ref(false)
const tableData = ref([])
const detailDialogVisible = ref(false)
const logDetail = ref({})

const searchForm = reactive({
  userType: '',
  operation: '',
  dateRange: []
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 获取用户类型标签颜色
const getUserTypeTag = (type) => {
  const colors = {
    1: 'primary',
    2: 'info',
    3: 'success'
  }
  return colors[type] || 'info'
}

// 获取用户类型名称
const getUserTypeName = (type) => {
  const names = {
    1: '管理员',
    2: '教师',
    3: '学生'
  }
  return names[type] || '未知'
}

// 获取操作类型样式类
const getOperationClass = (type) => {
  const classes = {
    '登录': 'operation-login',
    '新增': 'operation-add',
    '修改': 'operation-edit',
    '删除': 'operation-delete',
    '查看': 'operation-view',
    '查询': 'operation-view',
    '导出': 'operation-export',
    '导入': 'operation-import',
    '审核': 'operation-audit',
    '发布': 'operation-publish'
  }
  return classes[type] || 'operation-default'
}

// 格式化日期时间
const formatDateTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 加载日志数据
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.page,
      pageSize: pagination.size,
      userType: searchForm.userType ? parseInt(searchForm.userType) : null,
      operation: searchForm.operation
    }

    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }

    const res = await getLogPage(params)
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
  searchForm.userType = ''
  searchForm.operation = ''
  searchForm.dateRange = []
  handleSearch()
}

// 查看详情
const viewDetail = async (row) => {
  try {
    const res = await getLogById(row.logId)
    logDetail.value = res.data || row
    detailDialogVisible.value = true
  } catch (error) {
    logDetail.value = row
    detailDialogVisible.value = true
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

.search-select {
  width: 140px;
}

.search-date {
  width: 280px;
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

.operation-tag {
  padding: $spacing-xs $spacing-md;
  border-radius: $radius-md;
  font-size: $font-size-xs;
  font-weight: $font-weight-medium;

  &.operation-login {
    background: rgba($success-color, 0.08);
    color: $success-color;
  }

  &.operation-add {
    background: rgba($primary-color, 0.08);
    color: $primary-color;
  }

  &.operation-edit {
    background: rgba($warning-color, 0.08);
    color: $warning-color;
  }

  &.operation-delete {
    background: rgba($danger-color, 0.08);
    color: $danger-color;
  }

  &.operation-view, &.operation-query {
    background: rgba($info-color, 0.08);
    color: $info-color;
  }

  &.operation-export {
    background: rgba($primary-color-light, 0.08);
    color: $primary-color-light;
  }

  &.operation-import {
    background: rgba(#8B5CF6, 0.08);
    color: #8B5CF6;
  }

  &.operation-audit {
    background: rgba(#F59E0B, 0.08);
    color: #F59E0B;
  }

  &.operation-publish {
    background: rgba(#10B981, 0.08);
    color: #10B981;
  }

  &.operation-default {
    background: rgba($black, 0.04);
    color: #6A6A6A;
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
}

.detail-content {
  padding: $spacing-md;
}

.detail-item {
  display: flex;
  align-items: center;
  padding: $spacing-md 0;
  border-bottom: $border-width solid #E2E8F0;

  &:last-child {
    border-bottom: none;
  }
}

.detail-label {
  font-size: $font-size-sm;
  color: #6A6A6A;
  width: 100px;
  font-weight: $font-weight-medium;
}

.detail-value {
  font-size: $font-size-sm;
  color: $black;
}

:deep(.el-button--text) {
  font-weight: $font-weight-medium;
  padding: $spacing-xs $spacing-sm;
  color: $primary-color;

  &:hover {
    color: $primary-color-light;
    background: rgba($primary-color, 0.04);
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