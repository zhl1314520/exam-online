<template>
  <div class="exam-list-page">
    <div class="page-header">
      <div class="page-title">
        <h1>欢迎回来，{{ authStore.studentName }}</h1>
        <p>今天是 {{ todayDate }}，你有 <span class="highlight">{{ exams.length }}</span> 场待考考试</p>
      </div>
      <div class="user-profile">
        <div class="user-avatar">{{ authStore.studentName?.charAt(0) }}</div>
        <div class="user-info">
          <div class="user-name">{{ authStore.studentName }}</div>
          <div class="user-role">{{ authStore.user?.className || '学生' }}</div>
        </div>

      </div>
    </div>

    <!-- Stats -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-header">
          <div class="stat-icon orange">
            <Icon icon="solar:clock-circle-bold" />
          </div>
          <div class="stat-label">待考考试</div>
        </div>
        <div class="stat-value">{{ exams.length }}</div>
        <div class="stat-meta">
          <span class="trend-up">●</span>
          <span>明天有{{ tomorrowExamCount }}场</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-header">
          <div class="stat-icon green">
            <Icon icon="solar:check-circle-bold" />
          </div>
          <div class="stat-label">已完成</div>
        </div>
        <div class="stat-value">{{ statistics.completedCount || 0 }}</div>
        <div class="stat-meta">
          <span class="trend-up">+{{ statistics.recentCount || 0 }}</span>
          <span>本月</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-header">
          <div class="stat-icon blue">
            <Icon icon="solar:star-bold" />
          </div>
          <div class="stat-label">平均分</div>
        </div>
        <div class="stat-value">{{ statistics.avgScore || 0 }}</div>
        <div class="stat-meta">
          <span class="trend-up">+{{ statistics.scoreChange || 0 }}</span>
          <span>较上学期</span>
        </div>
      </div>
    </div>

    <div class="two-column">
      <!-- Upcoming Exams -->
      <div class="section">
        <div class="section-header">
          <h2 class="section-title">待考考试</h2>
          <router-link to="/pending-exams" class="section-action">查看全部</router-link>
        </div>
        <div class="exam-list">
          <div
            v-for="exam in exams"
            :key="exam.examId"
            class="exam-item"
          >
            <div class="exam-status upcoming">
              <Icon icon="solar:clock-circle-bold" />
            </div>
            <div class="exam-info">
              <div class="exam-name">{{ exam.examName }}</div>
              <div class="exam-meta">{{ exam.subjectName }} · {{ exam.duration }}分钟 · {{ exam.totalScore }}分</div>
            </div>
            <div class="exam-time">
              <div class="exam-time-value">{{ formatTime(exam.startTime) }}</div>
              <div class="exam-time-label">{{ getTimeLabel(exam.startTime) }}</div>
            </div>
            <div class="exam-action">
              <a
                v-if="isExamAvailable(exam)"
                class="btn btn-primary"
                @click="startExam(exam)"
              >
                进入考试
              </a>
              <button v-else class="btn btn-disabled">未开始</button>
            </div>
          </div>
          <div v-if="exams.length === 0" class="empty-list">
            <Icon icon="solar:calendar-blank" class="empty-icon" />
            <p>暂无待考考试</p>
          </div>
        </div>
      </div>

      <!-- Recent Scores -->
      <div class="section">
        <div class="section-header">
          <h2 class="section-title">最近成绩</h2>
          <router-link to="/score-report" class="section-action">查看全部</router-link>
        </div>
        <table class="score-table">
          <thead>
            <tr>
              <th>考试名称</th>
              <th>分数</th>
              <th>排名</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="record in recentScores" :key="record.examRecordId">
              <td>{{ record.examName }}</td>
              <td class="score-value" :class="getScoreClass(record.score)">{{ record.score }}</td>
              <td><span class="rank-badge">{{ record.rank }} / {{ record.total }}</span></td>
            </tr>
            <tr v-if="recentScores.length === 0">
              <td colspan="3" class="empty-cell">暂无成绩记录</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useExamStore } from '@/stores/exam.js'
import { useAuthStore } from '@/stores/auth.js'
import examApi from '@/api/exam.js'
import examTakingApi from '@/api/examTaking.js'
import scoreApi from '@/api/score.js'

const router = useRouter()
const examStore = useExamStore()
const authStore = useAuthStore()

const handleLogout = () => {
  authStore.logout()
  window.location.href = 'http://localhost:5173/login'
}

const exams = ref([])
const recentScores = ref([])
const statistics = ref({})
const loading = ref(true)

const todayDate = computed(() => {
  const now = new Date()
  return `${now.getFullYear()}年${String(now.getMonth() + 1).padStart(2, '0')}月${String(now.getDate()).padStart(2, '0')}日`
})

const tomorrowExamCount = computed(() => {
  const tomorrow = new Date()
  tomorrow.setDate(tomorrow.getDate() + 1)
  const tomorrowStr = tomorrow.toISOString().split('T')[0]
  return exams.value.filter(e => e.startTime && e.startTime.startsWith(tomorrowStr)).length
})

const fetchExams = async () => {
  try {
    const res = await examApi.getPendingExams()
    exams.value = res.data || []
  } catch (err) {
    console.error('获取待考考试失败:', err)
  } finally {
    loading.value = false
  }
}

const fetchRecentScores = async () => {
  try {
    const res = await scoreApi.getRecentRecords()
    recentScores.value = res.data || []
  } catch (err) {
    console.error('获取最近成绩失败:', err)
  }
}

const fetchStatistics = async () => {
  try {
    const res = await scoreApi.getHomeStatistics()
    statistics.value = res.data || {}
  } catch (err) {
    console.error('获取统计数据失败:', err)
  }
}

const formatTime = (time) => {
  if (!time) return '-'
  const date = new Date(time)
  return `${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

const getTimeLabel = (time) => {
  if (!time) return ''
  const now = new Date()
  const examDate = new Date(time)
  const diffDays = Math.ceil((examDate - now) / (1000 * 60 * 60 * 24))

  if (diffDays === 0) return '今天'
  if (diffDays === 1) return '明天'
  if (diffDays > 1) return `${diffDays}天后`
  return '今天'
}

const isExamAvailable = (exam) => {
  const now = new Date()
  const startTime = new Date(exam.startTime)
  const endTime = new Date(exam.endTime)
  return now >= startTime && now <= endTime
}

const getScoreClass = (score) => {
  if (score >= 90) return 'score-high'
  if (score >= 60) return 'score-mid'
  return 'score-low'
}

const startExam = async (exam) => {
  try {
    const res = await examTakingApi.startExam(exam.examId)
    examStore.setCurrentExam(res.data.exam)
    examStore.setCurrentRecord(res.data.record)
    examStore.setRemainingTime(res.data.remainingTime)

    router.push(`/exam-taking/${res.data.recordId}`)
  } catch (err) {
    alert(err.message || '开始考试失败')
  }
}

onMounted(async () => {
  await Promise.all([fetchExams(), fetchRecentScores(), fetchStatistics()])
})
</script>

<style scoped>
.exam-list-page {
  font-family: 'Space Grotesk', sans-serif;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.page-title h1 {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-primary);
}

.page-title p {
  color: var(--text-secondary);
  font-size: 14px;
}

.highlight {
  color: var(--accent-amber);
  font-weight: 600;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  box-shadow: var(--shadow-sm);
}

.user-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, var(--accent-coral), var(--accent-amber));
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 15px;
  color: white;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
}

.user-role {
  font-size: 12px;
  color: var(--text-secondary);
}

.logout-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  margin-left: 8px;
  border-radius: 6px;
  transition: background 0.2s;
}

.logout-btn:hover {
  background: var(--bg-tertiary);
}

.logout-icon {
  color: #9ca3af;
  font-size: 18px;
}

.logout-btn:hover .logout-icon {
  color: var(--accent-coral);
}

.header-actions {
  display: flex;
  gap: 12px;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
  font-family: inherit;
  text-decoration: none;
}

.btn-secondary {
  background: var(--bg-tertiary);
  color: var(--text-primary);
  border: 1px solid var(--border);
}

.btn-secondary:hover {
  background: var(--bg-primary);
}

.btn-primary {
  background: linear-gradient(135deg, var(--accent-sky), var(--accent-mint));
  color: white;
}

.btn-primary:hover {
  opacity: 0.9;
}

.btn-disabled {
  background: var(--bg-tertiary);
  color: var(--text-muted);
  border: 1px solid var(--border);
  cursor: not-allowed;
}

/* Stats Cards */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
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

.stat-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.stat-icon {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon.orange {
  background: rgba(217, 119, 6, 0.1);
  color: var(--accent-amber);
}

.stat-icon.green {
  background: rgba(5, 150, 105, 0.1);
  color: var(--accent-mint);
}

.stat-icon.blue {
  background: rgba(2, 132, 199, 0.1);
  color: var(--accent-sky);
}

.stat-icon .iconify {
  font-size: 22px;
}

.stat-label {
  font-size: 13px;
  color: var(--text-secondary);
}

.stat-value {
  font-size: 32px;
  font-weight: 600;
  color: var(--text-primary);
  margin-top: 4px;
}

.stat-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  font-size: 13px;
  color: var(--text-secondary);
}

.trend-up {
  color: var(--accent-mint);
}

.trend-down {
  color: var(--accent-coral);
}

/* Section */
.section {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  box-shadow: var(--shadow-sm);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.section-action {
  font-size: 13px;
  color: var(--accent-sky);
  text-decoration: none;
}

.section-action:hover {
  text-decoration: underline;
}

/* Exam List */
.exam-list {
  padding: 8px;
}

.exam-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
  border-radius: 8px;
  transition: all 0.2s;
  cursor: pointer;
}

.exam-item:hover {
  background: var(--bg-tertiary);
}

.exam-status {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.exam-status.upcoming {
  background: rgba(217, 119, 6, 0.1);
  color: var(--accent-amber);
}

.exam-status.ongoing {
  background: rgba(5, 150, 105, 0.1);
  color: var(--accent-mint);
}

.exam-status.completed {
  background: rgba(161, 161, 170, 0.1);
  color: var(--text-muted);
}

.exam-info {
  flex: 1;
}

.exam-name {
  font-size: 15px;
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.exam-meta {
  font-size: 13px;
  color: var(--text-secondary);
}

.exam-time {
  text-align: right;
  margin-right: 20px;
}

.exam-time-value {
  font-size: 14px;
  color: var(--text-primary);
}

.exam-time-label {
  font-size: 12px;
  color: var(--text-muted);
}

.exam-action .btn {
  padding: 8px 16px;
  font-size: 13px;
}

/* Score Table */
.score-table {
  width: 100%;
  border-collapse: collapse;
}

.score-table th,
.score-table td {
  padding: 14px 24px;
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

.rank-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
  background: var(--bg-tertiary);
  border-radius: 20px;
  font-size: 12px;
  color: var(--text-secondary);
  border: 1px solid var(--border);
}

.empty-list,
.empty-cell {
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

.two-column {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}
</style>
