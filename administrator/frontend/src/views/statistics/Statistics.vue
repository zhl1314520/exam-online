<template>
  <div class="page-container">
    <!-- 统计卡片 -->
    <div class="stats-grid animate-fade-in">
      <div class="stat-card">
        <div class="stat-icon-area">
          <Icon icon="mdi:account-group" class="stat-icon primary" />
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ userStats.totalUsers || 0 }}</span>
          <span class="stat-label">总用户数</span>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon-area">
          <Icon icon="mdi:school" class="stat-icon success" />
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ userStats.studentCount || 0 }}</span>
          <span class="stat-label">学生数</span>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon-area">
          <Icon icon="mdi:teach" class="stat-icon accent" />
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ userStats.teacherCount || 0 }}</span>
          <span class="stat-label">教师数</span>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon-area">
          <Icon icon="mdi:office-building" class="stat-icon info" />
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ userStats.departmentCount || 0 }}</span>
          <span class="stat-label">院系数</span>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <div class="chart-card animate-fade-in" style="animation-delay: 0.1s">
        <div class="card-header">
          <h3 class="card-title">
            <Icon icon="mdi:chart-line" class="title-icon" />
            用户增长趋势
          </h3>
        </div>
        <div class="chart-wrapper">
          <v-chart :option="lineChartOption" autoresize />
        </div>
      </div>

      <div class="chart-card animate-fade-in" style="animation-delay: 0.2s">
        <div class="card-header">
          <h3 class="card-title">
            <Icon icon="mdi:chart-pie" class="title-icon" />
            用户角色分布
          </h3>
        </div>
        <div class="chart-wrapper">
          <v-chart :option="pieChartOption" autoresize />
        </div>
      </div>
    </div>

    <!-- 详细数据表格 -->
    <div class="table-card animate-fade-in" style="animation-delay: 0.3s">
      <div class="card-header">
        <h3 class="card-title">
          <Icon icon="mdi:table" class="title-icon" />
          院系统计数据
        </h3>
      </div>

      <el-table :data="departmentStats" stripe class="custom-table">
        <el-table-column prop="departmentName" label="院系名称" width="150" />
        <el-table-column prop="teacherCount" label="教师人数" width="100">
          <template #default="{ row }">
            <el-tag type="primary" size="small">{{ row.teacherCount || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="classCount" label="班级数量" width="100">
          <template #default="{ row }">
            <el-tag type="accent" size="small">{{ row.classCount || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="studentCount" label="学生人数" width="100">
          <template #default="{ row }">
            <el-tag type="success" size="small">{{ row.studentCount || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="examCount" label="考试场次" width="100">
          <template #default="{ row }">
            <el-tag type="info" size="small">{{ row.examCount || 0 }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Icon } from '@iconify/vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, PieChart, LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import { getOverview, getUserStatistics } from '@/api/statistics'

use([
  CanvasRenderer,
  BarChart,
  PieChart,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const userStats = ref({
  totalUsers: 0,
  studentCount: 0,
  teacherCount: 0,
  departmentCount: 0,
  classCount: 0
})

const departmentStats = ref([])

// 用户增长趋势图表配置
const lineChartOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    backgroundColor: 'rgba(255, 255, 255, 0.95)',
    borderColor: '#E5E7EB',
    textStyle: { color: '#1F2937' }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['1月', '2月', '3月', '4月', '5月', '6月'],
    axisLine: { lineStyle: { color: '#E5E7EB' } },
    axisLabel: { color: '#6B7280' }
  },
  yAxis: {
    type: 'value',
    axisLine: { show: false },
    splitLine: { lineStyle: { color: '#F3F4F6' } },
    axisLabel: { color: '#6B7280' }
  },
  series: [
    {
      name: '学生',
      type: 'line',
      smooth: true,
      data: [100, 150, 200, 280, 350, 420],
      lineStyle: { color: '#0F766E', width: 2 },
      itemStyle: { color: '#0F766E' },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(15, 118, 110, 0.3)' },
            { offset: 1, color: 'rgba(15, 118, 110, 0.05)' }
          ]
        }
      }
    },
    {
      name: '教师',
      type: 'line',
      smooth: true,
      data: [20, 25, 30, 35, 40, 50],
      lineStyle: { color: '#F59E0B', width: 2 },
      itemStyle: { color: '#F59E0B' },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(245, 158, 11, 0.3)' },
            { offset: 1, color: 'rgba(245, 158, 11, 0.05)' }
          ]
        }
      }
    }
  ]
}))

// 用户角色分布饼图配置
const pieChartOption = computed(() => ({
  tooltip: {
    trigger: 'item',
    backgroundColor: 'rgba(255, 255, 255, 0.95)',
    borderColor: '#E5E7EB',
    textStyle: { color: '#1F2937' }
  },
  legend: {
    orient: 'vertical',
    right: '5%',
    top: 'center',
    textStyle: { color: '#6B7280' }
  },
  series: [
    {
      name: '用户分布',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['40%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false },
      emphasis: {
        label: {
          show: true,
          fontSize: 14,
          fontWeight: 'bold'
        }
      },
      data: [
        { value: userStats.value.studentCount, name: '学生', itemStyle: { color: '#0F766E' } },
        { value: userStats.value.teacherCount, name: '教师', itemStyle: { color: '#F59E0B' } },
        { value: 1, name: '管理员', itemStyle: { color: '#3B82F6' } }
      ]
    }
  ]
}))

// 加载统计数据
const loadData = async () => {
  try {
    const overviewRes = await getOverview()
    if (overviewRes.data) {
      userStats.value = overviewRes.data
    }

    const userRes = await getUserStatistics()
    if (userRes.data) {
      departmentStats.value = userRes.data.departmentStats || []
    }
  } catch (error) {
    console.error('加载统计数据失败', error)
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
  gap: $spacing-xl;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: $spacing-lg;
}

.stat-card {
  background: $bg-card;
  border-radius: $radius-lg;
  padding: $spacing-xl;
  border: $border-width solid #E2E8F0;
  display: flex;
  align-items: center;
  gap: $spacing-lg;
  transition: all $transition-normal $ease-minimal;

  &:hover {
    border-color: rgba($black, 0.1);
    box-shadow: $shadow-md;
    transform: translateY(-2px);
  }
}

.stat-icon-area {
  flex-shrink: 0;
}

.stat-icon {
  font-size: 28px;
  width: 48px;
  height: 48px;
  border-radius: $radius-md;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba($black, 0.03);

  &.primary {
    color: $primary-color;
  }

  &.accent {
    color: $success-color;
  }

  &.success {
    color: $info-color;
  }

  &.info {
    color: $warning-color;
  }
}

.stat-content {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
  flex: 1;
}

.stat-value {
  font-size: $font-size-2xl;
  font-weight: $font-weight-semibold;
  color: $black;
  letter-spacing: -0.02em;
}

.stat-label {
  font-size: $font-size-sm;
  color: #6A6A6A;
  font-weight: $font-weight-medium;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: $spacing-lg;
}

.chart-card {
  background: $bg-card;
  border-radius: $radius-lg;
  padding: $spacing-xl;
  border: $border-width solid #E2E8F0;
  transition: all $transition-normal $ease-minimal;

  &:hover {
    border-color: rgba($black, 0.1);
    box-shadow: $shadow-sm;
  }
}

.card-header {
  margin-bottom: $spacing-lg;
  padding-bottom: $spacing-md;
  border-bottom: $border-width solid #E2E8F0;
}

.card-title {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $black;
  letter-spacing: -0.01em;
}

.title-icon {
  font-size: 22px;
  color: $primary-color;
}

.chart-wrapper {
  height: 280px;
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

// 响应式
@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .charts-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>