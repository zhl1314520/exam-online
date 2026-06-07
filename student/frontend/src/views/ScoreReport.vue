<template>
  <div class="score-report-page">
    <div class="page-header">
      <h1>成绩单</h1>
      <p>查看您的所有考试成绩及统计分析</p>
    </div>

    <div v-if="loading" class="loading-container">
      <Icon icon="solar:loader-2-bold" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <template v-else>
      <!-- Stats -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-label">平均分</div>
          <div class="stat-value high">{{ typeof statistics.averageScore === 'number' ? statistics.averageScore.toFixed(1) : (parseFloat(statistics.averageScore) || 0).toFixed(1) }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">最高分</div>
          <div class="stat-value high">{{ statistics.highestScore || 0 }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">最低分</div>
          <div class="stat-value mid">{{ statistics.lowestScore || 0 }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">考试次数</div>
          <div class="stat-value">{{ statistics.completedExams || 0 }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">及格次数</div>
          <div class="stat-value">{{ statistics.passCount || 0 }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">及格率</div>
          <div class="stat-value">{{ (statistics.passRate || 0).toFixed(1) }}%</div>
        </div>
      </div>

      <!-- Chart -->
      <div class="score-chart">
        <div class="chart-header">
          <h2 class="chart-title">成绩趋势</h2>
        </div>
        <div v-if="trendData.length === 0" class="empty-chart">
          <Icon icon="solar:chart-2-bold" class="empty-icon" />
          <p>暂无成绩数据</p>
        </div>
        <div v-else ref="chartRef" class="chart-line"></div>
      </div>

      <!-- Score Table -->
      <div class="section">
        <div class="section-header">
          <h2 class="section-title">历史成绩</h2>
          <div class="search-box">
            <Icon icon="solar:magnifer-outline" style="color: var(--text-muted);" />
            <input type="text" placeholder="搜索考试名称" v-model="searchText">
          </div>
        </div>
        <table class="score-table">
          <thead>
            <tr>
              <th>考试名称</th>
              <th>科目</th>
              <th>考试时间</th>
              <th>得分</th>
              <th>满分</th>
              <th>班级排名</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in filteredTrendData" :key="item.examRecordId">
              <td>{{ item.examName }}</td>
              <td>
                <span class="subject-tag">
                  <Icon icon="solar:book-bold" />
                  {{ item.subjectName }}
                </span>
              </td>
              <td>{{ formatDate(item.submitTime) }}</td>
              <td class="score-value" :class="getScoreClass(item.score)">{{ item.score }}</td>
              <td>{{ item.totalScore }}</td>
              <td>
                <span class="trend" :class="item.rank <= 10 ? 'trend-up' : 'trend-down'">
                  <Icon :icon="item.rank <= 10 ? 'solar:arrow-up-bold' : 'solar:arrow-down-bold'" />
                  {{ item.rank }} / {{ item.total || 45 }}
                </span>
              </td>
              <td>
                <button class="action-btn" @click="viewDetail(item)">查看详情</button>
              </td>
            </tr>
            <tr v-if="filteredTrendData.length === 0">
              <td colspan="7" class="empty-cell">暂无考试记录</td>
            </tr>
          </tbody>
        </table>
        <div class="pagination">
          <button class="page-btn" :disabled="currentPage === 1" @click="currentPage--">上一页</button>
          <button
            v-for="page in totalPages"
            :key="page"
            class="page-btn"
            :class="{ active: currentPage === page }"
            @click="currentPage = page"
          >
            {{ page }}
          </button>
          <button class="page-btn" :disabled="currentPage === totalPages" @click="currentPage++">下一页</button>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, watch, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import * as echarts from 'echarts'
import scoreApi from '@/api/score.js'

const router = useRouter()

const statistics = ref({})
const trendData = ref([])
const recentRecords = ref([])
const loading = ref(true)
const searchText = ref('')
const currentPage = ref(1)
const pageSize = 5
const chartRef = ref(null)
let chartInstance = null

const filteredTrendData = computed(() => {
  let data = recentRecords.value
  if (searchText.value) {
    data = data.filter(item => item.examName?.includes(searchText.value))
  }
  const start = (currentPage.value - 1) * pageSize
  return data.slice(start, start + pageSize)
})

const totalPages = computed(() => Math.ceil(recentRecords.value.length / pageSize) || 1)

const getMonthLabel = (dateStr) => {
  if (!dateStr) return '-'
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`
}

const getCSSVar = (name) => getComputedStyle(document.documentElement).getPropertyValue(name).trim()

const initChart = () => {
  if (!chartRef.value || trendData.value.length === 0) return
  if (chartInstance) {
    chartInstance.dispose()
  }
  chartInstance = echarts.init(chartRef.value)

  const grouped = {}
  trendData.value.forEach(item => {
    const month = getMonthLabel(item.date)
    if (!grouped[month]) grouped[month] = []
    grouped[month].push(item.score)
  })

  const months = Object.keys(grouped).sort()
  const avgScores = months.map(m => {
    const scores = grouped[m]
    return (scores.reduce((a, b) => a + b, 0) / scores.length).toFixed(1)
  })

  const accentSky = getCSSVar('--accent-sky') || '#0284C7'
  const borderColor = getCSSVar('--border') || '#E4E4E7'
  const textSecondary = getCSSVar('--text-secondary') || '#71717A'

  const option = {
    tooltip: {
      trigger: 'axis',
      formatter: '{b}<br/>平均分: {c}分'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: months,
      axisLine: { lineStyle: { color: borderColor } },
      axisLabel: { color: textSecondary }
    },
    yAxis: {
      type: 'value',
      max: 100,
      axisLine: { lineStyle: { color: borderColor } },
      axisLabel: { color: textSecondary },
      splitLine: { lineStyle: { color: borderColor, type: 'dashed' } }
    },
    series: [
      {
        name: '平均分',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        data: avgScores,
        itemStyle: { color: accentSky },
        lineStyle: { width: 3, color: accentSky },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(2, 132, 199, 0.3)' },
              { offset: 1, color: 'rgba(2, 132, 199, 0.05)' }
            ]
          }
        }
      }
    ]
  }

  chartInstance.setOption(option)
}

const fetchData = async () => {
  try {
    const [statsRes, trendRes, recordsRes] = await Promise.all([
      scoreApi.getStatistics(),
      scoreApi.getTrend(),
      scoreApi.getRecentRecords()
    ])

    statistics.value = statsRes.data
    trendData.value = trendRes.data
    recentRecords.value = recordsRes.data
    loading.value = false
    await nextTick()
    initChart()
  } catch (err) {
    console.error('获取成绩单数据失败:', err)
    loading.value = false
  }
}

watch(trendData, () => {
  nextTick(() => {
    if (!loading.value) initChart()
  })
})

const handleResize = () => {
  chartInstance && chartInstance.resize()
}
window.addEventListener('resize', handleResize)

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})

const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

const getScoreClass = (score) => {
  if (score >= 90) return 'score-high'
  if (score >= 60) return 'score-mid'
  return 'score-low'
}

const viewDetail = (item) => {
  router.push(`/records/${item.examRecordId}`)
}

onMounted(fetchData)
</script>

<style scoped>
.score-report-page {
  font-family: 'Space Grotesk', sans-serif;
}

.page-header {
  margin-bottom: 32px;
}

.page-header h1 {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-primary);
}

.page-header p {
  color: var(--text-secondary);
  font-size: 14px;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  color: var(--text-muted);
}

.loading-icon {
  font-size: 40px;
  margin-bottom: 16px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Stats Cards */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 32px;
}

.stat-card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 24px;
  box-shadow: var(--shadow-sm);
  transition: box-shadow 0.2s;
}

.stat-card:hover {
  box-shadow: var(--shadow-md);
}

.stat-label {
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 8px;
}

.stat-value {
  font-size: 32px;
  font-weight: 600;
  color: var(--text-primary);
}

.stat-value.high {
  color: var(--accent-mint);
}

.stat-value.mid {
  color: var(--accent-amber);
}

.stat-value.low {
  color: var(--accent-coral);
}

/* Score Chart */
.score-chart {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: var(--shadow-sm);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.empty-chart {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: var(--text-muted);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.chart-line {
  width: 100%;
  height: 280px;
}

/* Score Table */
.section {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  box-shadow: var(--shadow-sm);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.search-box {
  display: flex;
  align-items: center;
  gap: 8px;
  background: var(--bg-tertiary);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 8px 14px;
}

.search-box input {
  background: transparent;
  border: none;
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  width: 200px;
}

.search-box input::placeholder {
  color: var(--text-muted);
}

.score-table {
  width: 100%;
  border-collapse: collapse;
}

.score-table th,
.score-table td {
  padding: 16px 24px;
  text-align: left;
  font-size: 14px;
}

.score-table th {
  color: var(--text-secondary);
  font-weight: 500;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  background: var(--bg-tertiary);
}

.score-table td {
  border-top: 1px solid var(--border);
  color: var(--text-secondary);
}

.score-table tr:hover td {
  background: var(--bg-tertiary);
}

.score-value {
  font-weight: 600;
  font-family: 'JetBrains Mono', monospace;
  font-size: 16px;
}

.score-high {
  color: var(--accent-mint);
}

.score-mid {
  color: var(--accent-amber);
}

.score-low {
  color: var(--accent-coral);
}

.trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

.trend-up {
  color: var(--accent-mint);
}

.trend-down {
  color: var(--accent-coral);
}

.subject-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  background: var(--bg-tertiary);
  border-radius: 4px;
  font-size: 12px;
  color: var(--text-secondary);
  border: 1px solid var(--border);
}

.action-btn {
  padding: 6px 14px;
  background: transparent;
  border: 1px solid var(--border);
  border-radius: 6px;
  color: var(--text-secondary);
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.action-btn:hover {
  border-color: var(--accent-sky);
  color: var(--accent-sky);
}

.empty-cell {
  text-align: center;
  padding: 40px;
  color: var(--text-muted);
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  padding: 20px;
  border-top: 1px solid var(--border);
}

.page-btn {
  padding: 8px 14px;
  background: transparent;
  border: 1px solid var(--border);
  border-radius: 6px;
  color: var(--text-secondary);
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.page-btn:hover {
  border-color: var(--accent-sky);
  color: var(--accent-sky);
}

.page-btn.active {
  background: var(--accent-sky);
  color: white;
  border-color: var(--accent-sky);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
