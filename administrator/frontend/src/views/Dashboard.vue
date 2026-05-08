<template>
  <div class="dashboard-container">
    <!-- 统计卡片区域 - 不对称排列 -->
    <div class="stats-grid">
      <div class="stat-card card-large animate-fade-in">
        <div class="stat-icon-area">
          <Icon icon="mdi:account-group" class="stat-icon primary" />
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ overview.totalUsers || 0 }}</span>
          <span class="stat-label">总用户数</span>
          <div class="stat-breakdown">
            <span class="breakdown-item">
              <Icon icon="mdi:school" />
              学生 {{ overview.studentCount || 0 }}
            </span>
            <span class="breakdown-item">
              <Icon icon="mdi:teach" />
              教师 {{ overview.teacherCount || 0 }}
            </span>
          </div>
        </div>
      </div>

      <div class="stat-card card-medium animate-fade-in" style="animation-delay: 0.1s">
        <div class="stat-icon-area">
          <Icon icon="mdi:file-document-outline" class="stat-icon accent" />
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ overview.examCount || 0 }}</span>
          <span class="stat-label">考试场次</span>
        </div>
      </div>

      <div class="stat-card card-small animate-fade-in" style="animation-delay: 0.2s">
        <div class="stat-icon-area">
          <Icon icon="mdi:chart-line-variant" class="stat-icon success" />
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ overview.participantCount || 0 }}</span>
          <span class="stat-label">参与人次</span>
        </div>
      </div>

      <div class="stat-card card-small animate-fade-in" style="animation-delay: 0.3s">
        <div class="stat-icon-area">
          <Icon icon="mdi:office-building" class="stat-icon info" />
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ overview.departmentCount || 0 }}</span>
          <span class="stat-label">院系数量</span>
        </div>
      </div>

      <div class="stat-card card-medium animate-fade-in" style="animation-delay: 0.4s">
        <div class="stat-icon-area">
          <Icon icon="mdi:google-classroom" class="stat-icon warning" />
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ overview.classCount || 0 }}</span>
          <span class="stat-label">班级数量</span>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <div class="chart-card animate-fade-in" style="animation-delay: 0.5s">
        <div class="card-header">
          <h3 class="card-title">
            <Icon icon="mdi:chart-bar" class="title-icon" />
            用户分布统计
          </h3>
        </div>
        <div class="chart-wrapper">
          <v-chart :option="userChartOption" autoresize />
        </div>
      </div>

      <div class="chart-card animate-fade-in" style="animation-delay: 0.6s">
        <div class="card-header">
          <h3 class="card-title">
            <Icon icon="mdi:chart-pie" class="title-icon" />
            院系分布
          </h3>
        </div>
        <div class="chart-wrapper">
          <v-chart :option="pieChartOption" autoresize />
        </div>
      </div>
    </div>

    <!-- 最近日志区域 -->
    <div class="logs-card animate-fade-in" style="animation-delay: 0.7s">
      <div class="card-header">
        <h3 class="card-title">
          <Icon icon="mdi:text-box-search" class="title-icon" />
          最近操作日志
        </h3>
        <el-button text @click="viewAllLogs">
          查看全部
          <Icon icon="mdi:arrow-right" />
        </el-button>
      </div>

      <div class="logs-list">
        <div v-for="log in recentLogs" :key="log.logId" class="log-item">
          <div class="log-icon-area">
            <Icon :icon="getLogIcon(log.operationType)" :class="getLogClass(log.operationType)" />
          </div>
          <div class="log-content">
            <span class="log-description">{{ log.description }}</span>
            <div class="log-meta">
              <span class="log-user">{{ log.operatorName }}</span>
              <span class="log-time">{{ formatTime(log.operationTime) }}</span>
            </div>
          </div>
        </div>

        <div v-if="recentLogs.length === 0" class="empty-state">
          <Icon icon="mdi:inbox-outline" class="empty-icon" />
          <span>暂无操作日志</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import { getOverview, getUserStatistics } from '@/api/statistics'
import { getLogPage } from '@/api/log'

use([
  CanvasRenderer,
  BarChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const router = useRouter()

const overview = ref({
  totalUsers: 0,
  studentCount: 0,
  teacherCount: 0,
  examCount: 0,
  participantCount: 0,
  departmentCount: 0,
  classCount: 0
})

const recentLogs = ref([])

// 用户分布图表配置
const userChartOption = computed(() => ({
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
    data: ['学生', '教师', '管理员'],
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
      name: '用户数',
      type: 'bar',
      barWidth: '40%',
      data: [
        {
          value: overview.value.studentCount,
          itemStyle: { color: '#0F766E' }
        },
        {
          value: overview.value.teacherCount,
          itemStyle: { color: '#F59E0B' }
        },
        {
          value: 1,
          itemStyle: { color: '#3B82F6' }
        }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowColor: 'rgba(0, 0, 0, 0.2)'
        }
      }
    }
  ]
}))

// 院系分布饼图配置
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
      name: '院系',
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
        },
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.2)'
        }
      },
      data: [
        { value: 10, name: '计算机学院', itemStyle: { color: '#0F766E' } },
        { value: 8, name: '软件学院', itemStyle: { color: '#14B8A6' } },
        { value: 6, name: '信管学院', itemStyle: { color: '#F59E0B' } },
        { value: 5, name: '数学学院', itemStyle: { color: '#FBBF24' } },
        { value: 4, name: '物理学院', itemStyle: { color: '#3B82F6' } }
      ]
    }
  ]
}))

// 获取日志图标
const getLogIcon = (type) => {
  const icons = {
    '登录': 'mdi:login',
    '新增': 'mdi:plus-circle',
    '修改': 'mdi:pencil',
    '删除': 'mdi:delete',
    '查看': 'mdi:eye'
  }
  return icons[type] || 'mdi:circle'
}

// 获取日志样式类
const getLogClass = (type) => {
  const classes = {
    '登录': 'log-success',
    '新增': 'log-primary',
    '修改': 'log-accent',
    '删除': 'log-danger',
    '查看': 'log-info'
  }
  return classes[type] || 'log-default'
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date

  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  return `${Math.floor(diff / 86400000)}天前`
}

// 查看全部日志
const viewAllLogs = () => {
  router.push('/logs')
}

// 加载数据
const loadData = async () => {
  try {
    // 获取概览数据
    const overviewRes = await getOverview()
    if (overviewRes.data) {
      overview.value = overviewRes.data
    }

    // 获取最近日志
    const logsRes = await getLogPage({ page: 1, size: 5 })
    if (logsRes.data && logsRes.data.list) {
      recentLogs.value = logsRes.data.list
    }
  } catch (error) {
    console.error('加载仪表盘数据失败', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.dashboard-container {
  display: flex;
  flex-direction: column;
  gap: $spacing-xl;
}

// 统计卡片网格 - 极简网格布局
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: $spacing-lg;
}

.stat-card {
  background: $bg-card;
  border-radius: $radius-lg;
  padding: $spacing-xl;
  border: $border-width solid $border-color;
  display: flex;
  align-items: center;
  gap: $spacing-lg;
  transition: all $transition-normal $ease-minimal;

  &:hover {
    border-color: rgba($black, 0.1);
    box-shadow: $shadow-md;
    transform: translateY(-2px);
  }

  &.card-large {
    grid-column: span 2;
    background: $white-bright;
    border: $border-width solid $border-color;

    .stat-icon {
      background: rgba($accent, 0.1);
      color: $accent;
    }

    .stat-value {
      color: $black;
    }

    .stat-label {
      color: $gray;
    }

    .breakdown-item {
      color: $gray-light;
    }
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
    color: $accent;
  }

  &.accent {
    color: $success;
  }

  &.success {
    color: $info;
  }

  &.info {
    color: $warning;
  }

  &.warning {
    color: $accent-light;
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
  color: $gray;
  font-weight: $font-weight-medium;
}

.stat-breakdown {
  display: flex;
  gap: $spacing-lg;
  margin-top: $spacing-md;
}

.breakdown-item {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  font-size: $font-size-xs;
  color: $gray-light;

  .iconify {
    font-size: 14px;
    color: $accent;
  }
}

// 图表区域 - 极简风格
.charts-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: $spacing-lg;
}

.chart-card {
  background: $bg-card;
  border-radius: $radius-lg;
  padding: $spacing-xl;
  border: $border-width solid $border-color;
  transition: all $transition-normal $ease-minimal;

  &:hover {
    border-color: rgba($black, 0.1);
    box-shadow: $shadow-sm;
  }
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: $spacing-xl;
  padding-bottom: $spacing-md;
  border-bottom: $border-width solid $border-color;
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
  color: $accent;
}

.chart-wrapper {
  height: 280px;
}

// 日志卡片 - 极简风格
.logs-card {
  background: $bg-card;
  border-radius: $radius-lg;
  padding: $spacing-xl;
  border: $border-width solid $border-color;
  transition: all $transition-normal $ease-minimal;

  &:hover {
    border-color: rgba($black, 0.1);
    box-shadow: $shadow-sm;
  }

  .card-header {
    margin-bottom: $spacing-lg;
  }
}

.logs-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
}

.log-item {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-md;
  border-radius: $radius-md;
  background: transparent;
  border: $border-width solid transparent;
  transition: all $transition-fast;

  &:hover {
    background: rgba($black, 0.02);
    border-color: rgba($black, 0.04);
  }
}

.log-icon-area {
  flex-shrink: 0;
  width: 32px;
  height: 32px;
  border-radius: $radius-sm;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba($black, 0.03);
}

.log-icon {
  font-size: 16px;

  &.log-success {
    color: $success;
  }

  &.log-primary {
    color: $accent;
  }

  &.log-accent {
    color: $warning;
  }

  &.log-danger {
    color: $error;
  }

  &.log-info {
    color: $info;
  }
}

.log-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: $spacing-xs;
}

.log-description {
  font-size: $font-size-md;
  color: $black;
  font-weight: $font-weight-medium;
}

.log-meta {
  display: flex;
  gap: $spacing-md;
  font-size: $font-size-xs;
  color: $gray-light;

  .log-user {
    color: $accent;
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-2xl;
  color: $gray-light;

  .empty-icon {
    font-size: 48px;
    opacity: 0.5;
  }
}

// 按钮样式覆盖
:deep(.el-button.is-text) {
  color: $gray;
  font-weight: $font-weight-medium;

  &:hover {
    color: $accent;
    background: rgba($accent, 0.04);
  }

  .iconify {
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

// 响应式
@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .stat-card.card-large {
    grid-column: span 2;
  }

  .charts-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .stat-card.card-large {
    grid-column: span 1;
  }

  .stat-breakdown {
    flex-direction: column;
    gap: $spacing-sm;
  }
}
</style>