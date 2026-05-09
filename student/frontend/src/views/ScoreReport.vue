<template>
  <div class="score-report-page fade-in">
    <header class="page-header">
      <h1 class="page-title">成绩单</h1>
      <p class="page-desc">查看您的学习表现和成绩分析</p>
    </header>

    <div v-if="loading" class="loading-container">
      <Icon icon="lucide:loader-2" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <template v-else>
      <!-- 统计概览 -->
      <div class="stats-grid">
        <div class="stat-card card">
          <div class="stat-header">
            <Icon icon="lucide:file-text" class="stat-icon" />
            <span class="stat-label">已完成考试</span>
          </div>
          <span class="stat-value">{{ statistics.completedExams }}</span>
        </div>

        <div class="stat-card card">
          <div class="stat-header">
            <Icon icon="lucide:trending-up" class="stat-icon" />
            <span class="stat-label">平均分</span>
          </div>
          <span class="stat-value">{{ statistics.averageScore?.toFixed(1) }}</span>
        </div>

        <div class="stat-card card">
          <div class="stat-header">
            <Icon icon="lucide:award" class="stat-icon" />
            <span class="stat-label">最高分</span>
          </div>
          <span class="stat-value">{{ statistics.highestScore?.toFixed(1) }}</span>
        </div>

        <div class="stat-card card">
          <div class="stat-header">
            <Icon icon="lucide:check-circle" class="stat-icon" />
            <span class="stat-label">通过率</span>
          </div>
          <span class="stat-value">{{ statistics.passRate?.toFixed(1) }}%</span>
        </div>
      </div>

      <!-- 成绩趋势图 -->
      <div class="chart-section card">
        <div class="chart-header">
          <h3>成绩趋势</h3>
        </div>
        <div class="chart-body">
          <div v-if="trendData.length === 0" class="empty-chart">
            <Icon icon="lucide:bar-chart" class="empty-icon" />
            <p>暂无成绩数据</p>
          </div>
          <div v-else class="trend-chart">
            <div
              v-for="(item, index) in trendData"
              :key="index"
              class="trend-item"
            >
              <div class="trend-bar-container">
                <div
                  class="trend-bar"
                  :style="{ height: (item.score / item.totalScore * 100) + '%' }"
                  :class="{ pass: item.score >= item.passScore }"
                ></div>
              </div>
              <span class="trend-label">{{ item.examName?.substring(0, 8) }}...</span>
              <span class="trend-score">{{ item.score }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 知识点雷达图 -->
      <div class="chart-section card">
        <div class="chart-header">
          <h3>知识点掌握情况</h3>
        </div>
        <div class="chart-body">
          <div v-if="!radarData.knowledgePoints?.length" class="empty-chart">
            <Icon icon="lucide:radar" class="empty-icon" />
            <p>暂无知识点数据</p>
          </div>
          <div v-else class="radar-chart">
            <div
              v-for="(point, index) in radarData.knowledgePoints"
              :key="index"
              class="radar-item"
            >
              <div class="radar-header">
                <span class="radar-name">{{ point }}</span>
                <span class="radar-score">{{ radarData.mastery[index] }}%</span>
              </div>
              <div class="radar-bar">
                <div
                  class="radar-fill"
                  :style="{ width: radarData.mastery[index] + '%' }"
                  :class="getMasteryLevel(radarData.mastery[index])"
                ></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 考试记录表格 -->
      <div class="exam-table card">
        <div class="table-header">
          <h3>考试记录</h3>
        </div>
        <div class="table-body">
          <div v-if="trendData.length === 0" class="empty-table">
            <p>暂无考试记录</p>
          </div>
          <table v-else class="data-table">
            <thead>
              <tr>
                <th>考试名称</th>
                <th>科目</th>
                <th>得分</th>
                <th>满分</th>
                <th>状态</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in trendData" :key="item.date">
                <td>{{ item.examName }}</td>
                <td>{{ item.subjectName }}</td>
                <td class="score-cell">{{ item.score }}</td>
                <td>{{ item.totalScore }}</td>
                <td>
                  <span
                    class="status-tag"
                    :class="item.score >= item.passScore ? 'status-success' : 'status-error'"
                  >
                    {{ item.score >= item.passScore ? '通过' : '未通过' }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import scoreApi from '@/api/score.js'

const statistics = ref({})
const trendData = ref([])
const radarData = ref({})
const loading = ref(true)

const fetchData = async () => {
  try {
    const [statsRes, trendRes, radarRes] = await Promise.all([
      scoreApi.getStatistics(),
      scoreApi.getTrend(),
      scoreApi.getRadar()
    ])

    statistics.value = statsRes.data
    trendData.value = trendRes.data
    radarData.value = radarRes.data
  } catch (err) {
    console.error('获取成绩单数据失败:', err)
  } finally {
    loading.value = false
  }
}

const getMasteryLevel = (score) => {
  if (score >= 80) return 'excellent'
  if (score >= 60) return 'good'
  return 'need-improve'
}

onMounted(fetchData)
</script>

<style scoped>
.page-header {
  margin-bottom: 28px;
}

.page-title {
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 6px;
}

.page-desc {
  font-size: 15px;
  color: var(--text-secondary);
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
  width: 40px;
  height: 40px;
  margin-bottom: 16px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 20px;
  margin-bottom: 28px;
}

.stat-card {
  display: flex;
  flex-direction: column;
  padding: 24px;
}

.stat-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}

.stat-icon {
  width: 24px;
  height: 24px;
  color: var(--primary-color);
}

.stat-label {
  font-size: 14px;
  color: var(--text-secondary);
}

.stat-value {
  font-size: 36px;
  font-weight: 700;
  color: var(--text-primary);
}

.chart-section {
  margin-bottom: 24px;
  overflow: hidden;
}

.chart-header {
  padding: 20px 24px;
  border-bottom: 1px solid var(--border-color);
  background: linear-gradient(90deg, var(--bg-secondary) 0%, transparent 100%);
}

.chart-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.chart-body {
  padding: 24px;
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
  width: 48px;
  height: 48px;
  margin-bottom: 12px;
}

.trend-chart {
  display: flex;
  align-items: flex-end;
  gap: 20px;
  height: 200px;
  padding: 20px 0;
}

.trend-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  min-width: 60px;
}

.trend-bar-container {
  width: 40px;
  height: 160px;
  background: var(--bg-secondary);
  border-radius: var(--radius-sm);
  position: relative;
}

.trend-bar {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: var(--error-color);
  border-radius: var(--radius-sm);
  transition: height 0.3s;
}

.trend-bar.pass {
  background: var(--success-color);
}

.trend-label {
  margin-top: 8px;
  font-size: 11px;
  color: var(--text-muted);
  white-space: nowrap;
}

.trend-score {
  margin-top: 4px;
  font-size: 13px;
  font-weight: 600;
  color: var(--text-primary);
}

.radar-chart {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.radar-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.radar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.radar-name {
  font-size: 14px;
  color: var(--text-primary);
}

.radar-score {
  font-size: 14px;
  font-weight: 600;
  color: var(--primary-color);
}

.radar-bar {
  height: 8px;
  background: var(--bg-secondary);
  border-radius: 4px;
}

.radar-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s;
}

.radar-fill.excellent {
  background: var(--success-color);
}

.radar-fill.good {
  background: var(--warning-color);
}

.radar-fill.need-improve {
  background: var(--error-color);
}

.exam-table {
  overflow: hidden;
}

.table-header {
  padding: 20px 24px;
  border-bottom: 1px solid var(--border-color);
  background: linear-gradient(90deg, var(--bg-secondary) 0%, transparent 100%);
}

.table-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.table-body {
  padding: 0;
}

.empty-table {
  padding: 40px;
  text-align: center;
  color: var(--text-muted);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 14px 24px;
  text-align: left;
  font-size: 14px;
}

.data-table th {
  background: var(--bg-secondary);
  color: var(--text-secondary);
  font-weight: 500;
}

.data-table tbody tr:hover {
  background: var(--bg-secondary);
}

.score-cell {
  font-weight: 600;
  color: var(--primary-color);
}
</style>
