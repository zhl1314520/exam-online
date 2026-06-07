<template>
  <div class="dashboard fade-in">

    <!-- Statistics cards -->
    <div class="stats-row">
      <div class="stat-card card" v-for="stat in stats" :key="stat.title">
        <div class="stat-icon" :style="{ background: stat.bgColor, color: stat.color }">
          <Icon :icon="stat.icon" class="stat-icon-inner" />
        </div>
        <div class="stat-content">
          <h3 class="stat-value">{{ stat.value }}</h3>
          <p class="stat-label">{{ stat.title }}</p>
        </div>
      </div>
    </div>

    <!-- Quick actions - horizontal row below stat cards -->
    <div class="quick-actions-bar card">
      <div class="panel-content">
        <div class="quick-actions">
          <router-link to="/questions" class="quick-action">
            <div class="action-icon" style="background: rgba(16, 185, 129, 0.15); color: var(--accent-mint);">
              <Icon icon="solar:add-circle-bold-duotone" class="action-icon-inner" />
            </div>
            <span>新增题目</span>
          </router-link>
          <router-link to="/papers" class="quick-action">
            <div class="action-icon" style="background: rgba(14, 165, 233, 0.15); color: var(--accent-sky);">
              <Icon icon="solar:document-add-bold-duotone" class="action-icon-inner" />
            </div>
            <span>创建试卷</span>
          </router-link>
          <router-link to="/exams" class="quick-action">
            <div class="action-icon" style="background: rgba(245, 158, 11, 0.15); color: var(--accent-amber);">
              <Icon icon="solar:calendar-bold-duotone" class="action-icon-inner" />
            </div>
            <span>发布考试</span>
          </router-link>
          <router-link to="/records" class="quick-action">
            <div class="action-icon" style="background: rgba(255, 107, 107, 0.15); color: var(--accent-coral);">
              <Icon icon="solar:chart-2-bold-duotone" class="action-icon-inner" />
            </div>
            <span>查看成绩</span>
          </router-link>
        </div>
      </div>
    </div>

    <!-- Content layout -->
    <div class="content-grid">
      <div class="main-panel">
        <div class="panel-card card">
          <div class="panel-header">
            <span class="panel-title">近期题库变动概览</span>
            <div class="panel-actions">
              <button class="refresh-btn" @click="loadQuestionTrend">
                <Icon icon="solar:refresh-linear" class="refresh-icon" />
              </button>
            </div>
          </div>
          <div class="panel-content">
            <div v-if="trendChartOption" class="chart-container">
              <h4>题库题目总数趋势</h4>
              <v-chart :option="trendChartOption" class="echarts-chart" autoresize />
            </div>
            <div v-else class="chart-placeholder">
              <Icon icon="solar:chart-2-bold-duotone" class="chart-icon" />
              <p>题库变动图表区域</p>
            </div>
          </div>
        </div>
      </div>

      <div class="side-panel">
        <div class="panel-card card">
          <div class="panel-header">
            <span class="panel-title">最近操作</span>
          </div>
          <div class="panel-content">
            <div class="recent-operations">
              <div v-for="operation in recentOperations" :key="operation.logId" class="operation-item">
                <div class="operation-info">
                  <div class="operation-name">{{ operation.operation }}：{{ operation.operationDetail }}</div>
                  <div class="operation-time">{{ formatTime(operation.createTime) }}</div>
                </div>
              </div>
              <div v-if="recentOperations.length === 0" class="empty-state">
                暂无最近操作记录
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { getQuestionPage, getQuestionTrend } from '@/api/question'
import { getDashboardStatistics } from '@/api/examRecord'
import { getRecentLogs } from '@/api/systemLog'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  ToolboxComponent,
} from 'echarts/components'

use([
  CanvasRenderer,
  LineChart,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  ToolboxComponent,
])

const stats = ref([
  { title: '题库题目', value: 0, icon: 'solar:database-bold-duotone', color: '#F59E0B', bgColor: 'rgba(245, 158, 11, 0.15)' },
  { title: '试卷数量', value: 0, icon: 'solar:document-bold-duotone', color: '#10B981', bgColor: 'rgba(16, 185, 129, 0.15)' },
  { title: '考试场次', value: 0, icon: 'solar:calendar-bold-duotone', color: '#0EA5E9', bgColor: 'rgba(14, 165, 233, 0.15)' },
  { title: '参考学生', value: 0, icon: 'solar:users-group-rounded-bold-duotone', color: '#FF6B6B', bgColor: 'rgba(255, 107, 107, 0.15)' }
])

const recentOperations = ref([])
const trendChartData = ref([])
const trendChartOption = ref({})

const buildTrendChartOption = () => {
  if (!trendChartData.value || trendChartData.value.length === 0) return {}
  const dates = trendChartData.value.map(item => item.date.substring(5))
  const counts = trendChartData.value.map(item => item.totalCount)
  return {
    tooltip: {
      trigger: 'axis',
      formatter: (params) => {
        const p = params[0]
        const fullDate = trendChartData.value[p.dataIndex].date
        return `${fullDate}<br/>题目总数: ${p.value}`
      },
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '10%',
      containLabel: true,
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: {
        fontSize: 11,
        rotate: 45,
        interval: 2,
      },
      boundaryGap: false,
    },
    yAxis: {
      type: 'value',
      minInterval: 1,
      axisLabel: { fontSize: 12 },
    },
    series: [
      {
        type: 'line',
        data: counts,
        smooth: true,
        symbol: 'circle',
        symbolSize: 4,
        lineStyle: {
          color: '#3B82F6',
          width: 2,
        },
        itemStyle: {
          color: '#3B82F6',
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(59, 130, 246, 0.3)' },
              { offset: 1, color: 'rgba(59, 130, 246, 0.02)' },
            ],
          },
        },
      },
    ],
  }
}

const formatTime = (timeString) => {
  if (!timeString) return ''
  const date = new Date(timeString)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

const loadStats = async () => {
  try {
    const [questionRes, dashboardStats] = await Promise.all([
      getQuestionPage({ page: 1, size: 1 }),
      getDashboardStatistics()
    ])

    stats.value[0].value = questionRes.data?.total || 0
    stats.value[1].value = dashboardStats.data?.paperCount || 0
    stats.value[2].value = dashboardStats.data?.examCount || 0
    stats.value[3].value = dashboardStats.data?.studentCount || 0
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

const loadQuestionTrend = async () => {
  try {
    const response = await getQuestionTrend()
    trendChartData.value = response.data || []
    trendChartOption.value = buildTrendChartOption()
  } catch (error) {
    console.error('加载题库变动数据失败', error)
    trendChartData.value = []
  }
}

const loadRecentOperations = async () => {
  try {
    const response = await getRecentLogs(10)
    recentOperations.value = response.data || []
  } catch (error) {
    console.error('加载最近操作记录失败', error)
    recentOperations.value = []
  }
}

onMounted(() => {
  loadStats()
  loadQuestionTrend()
  loadRecentOperations()
})
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.dashboard {
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

/* Statistics cards - 原型样式 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
}

.stat-content {
  h3 {
    font-size: 24px;
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

/* Content layout */
.content-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
  align-items: stretch;
}

.main-panel {
  display: flex;
  flex-direction: column;
  gap: 24px;
  height: 100%;
}

.side-panel {
  display: flex;
  flex-direction: column;
  gap: 24px;
  height: 100%;
}

.main-panel > .panel-card,
.side-panel > .panel-card {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.panel-card {
  overflow: hidden;
}

.panel-header {
  padding: 20px 24px;
  border-bottom: 1px solid $border-base;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-title {
  font-size: 16px;
  font-weight: 600;
  color: $text-primary;
}

.panel-actions {
  display: flex;
  gap: 8px;
}

.refresh-btn {
  padding: 8px;
  background: transparent;
  border: 1px solid $border-base;
  border-radius: $radius-sm;
  color: $text-muted;
  cursor: pointer;
  transition: all $transition-fast;

  &:hover {
    background: $bg-tertiary;
    border-color: $border-dark;
    color: $text-secondary;
  }

  .refresh-icon {
    font-size: 18px;
  }
}

.view-all {
  font-size: 13px;
  color: $accent-mint;
  text-decoration: none;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: all $transition-fast;

  &:hover {
    color: $primary-dark;

    .arrow-icon {
      transform: translateX(2px);
    }
  }

  .arrow-icon {
    font-size: 14px;
    transition: transform $transition-fast;
  }
}

.panel-content {
  padding: 24px;
}

/* Chart container */
.chart-container {
  margin-top: 20px;
}

.echarts-chart {
  width: 100%;
  height: 320px;
}

/* Old bar chart styles removed - replaced by ECharts */

/* Chart placeholder */
.chart-placeholder {
  height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: $bg-tertiary;
  border-radius: $radius-md;
  color: $text-muted;

  &.small {
    height: 200px;
  }

  .chart-icon {
    font-size: 48px;

    &.small {
      font-size: 32px;
    }
  }

  p {
    margin-top: 12px;
    font-size: 14px;
  }
}

/* Exam list */
.exam-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.exam-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: $bg-tertiary;
  border-radius: $radius-md;
  border: 1px solid transparent;
  transition: all $transition-fast;

  &:hover {
    border-color: $border-base;
    background: $bg-hover;
  }
}

.exam-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.exam-name {
  font-size: 14px;
  font-weight: 500;
  color: $text-primary;
}

.exam-time {
  font-size: 12px;
  color: $text-muted;
  display: flex;
  align-items: center;
  gap: 4px;

  .time-icon {
    font-size: 14px;
  }
}

.exam-status {
  font-size: 12px;
  font-weight: 500;
  padding: 4px 12px;
  border-radius: 20px;

  &.draft {
    background: rgba($text-muted, 0.15);
    color: $text-muted;
  }

  &.published {
    background: rgba($success-color, 0.15);
    color: $success-color;
  }

  &.ongoing {
    background: rgba($warning-color, 0.15);
    color: $warning-color;
  }

  &.ended {
    background: rgba($danger-color, 0.15);
    color: $danger-color;
  }
}

.empty-state {
  padding: 40px;
  text-align: center;
  color: $text-muted;

  .empty-icon {
    font-size: 32px;
  }

  p {
    margin-top: 12px;
    font-size: 14px;
  }
}

/* Quick actions bar - horizontal row below stat cards */
.quick-actions-bar {
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: 12px;
  margin-bottom: 24px;
}

.quick-actions {
  display: flex;
  gap: 0;
  justify-content: center;
}

.quick-action {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 8px;
  padding: 16px 24px;
  background: transparent;
  cursor: pointer;
  transition: all 0.2s;
  text-decoration: none;
  border-right: 1px solid $border-base;

  &:last-child {
    border-right: none;
  }

  &:hover {
    background: $bg-hover;
  }

  .action-icon {
    width: 36px;
    height: 36px;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;

    .action-icon-inner {
      font-size: 20px;
    }
  }

  span {
    font-size: 14px;
    color: $text-secondary;
    font-weight: 500;
    white-space: nowrap;
  }
}
</style>