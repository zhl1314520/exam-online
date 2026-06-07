<template>
  <div class="pending-exams-page">
    <header class="page-header">
      <h1 class="page-title">待考考试</h1>
      <p class="page-desc">查看您即将参加的考试安排</p>
    </header>

    <div v-if="loading" class="loading-container">
      <Icon icon="solar:loader-2-bold" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <div v-else-if="exams.length === 0" class="empty-state">
      <Icon icon="solar:calendar-remove-bold" class="empty-icon" />
      <h3>暂无待考考试</h3>
      <p>当前没有即将开始的考试</p>
    </div>

    <div v-else class="exam-list">
      <div
        v-for="exam in exams"
        :key="exam.examId"
        class="exam-card"
      >
        <div class="exam-header">
          <div class="exam-status" :class="getStatusClass(exam)">
            {{ getStatusText(exam) }}
          </div>
          <div class="exam-time">
            <Icon icon="solar:clock-circle-bold" />
            {{ exam.duration }}分钟
          </div>
        </div>

        <h3 class="exam-name">{{ exam.examName }}</h3>

        <div class="exam-info">
          <div class="info-item">
            <Icon icon="solar:book-bold" />
            <span>{{ exam.subjectName || '未分类' }}</span>
          </div>
          <div class="info-item">
            <Icon icon="solar:calendar-bold" />
            <span>开始：{{ formatTime(exam.startTime) }}</span>
          </div>
          <div class="info-item">
            <Icon icon="solar:calendar-bold" />
            <span>结束：{{ formatTime(exam.endTime) }}</span>
          </div>
        </div>

        <div class="exam-footer">
          <div class="score-info">
            <span class="total-score">满分 {{ exam.totalScore }}分</span>
            <span class="pass-score">及格 {{ exam.passScore }}分</span>
          </div>
          <button
            v-if="isExamAvailable(exam)"
            class="btn btn-primary"
            @click="startExam(exam)"
          >
            <Icon icon="solar:play-circle-bold" />
            进入考试
          </button>
          <button v-else class="btn btn-disabled">
            <Icon icon="solar:lock-bold" />
            {{ getTimeUntil(exam.startTime) }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useExamStore } from '@/stores/exam.js'
import examApi from '@/api/exam.js'
import examTakingApi from '@/api/examTaking.js'

const router = useRouter()
const examStore = useExamStore()

const exams = ref([])
const loading = ref(true)

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

const formatTime = (time) => {
  if (!time) return '-'
  const date = new Date(time)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

const isExamAvailable = (exam) => {
  const now = new Date()
  const startTime = new Date(exam.startTime)
  const endTime = new Date(exam.endTime)
  return now >= startTime && now <= endTime
}

const getStatusClass = (exam) => {
  const now = new Date()
  const startTime = new Date(exam.startTime)
  const endTime = new Date(exam.endTime)

  if (now > endTime) return 'ended'
  if (now >= startTime && now <= endTime) return 'ongoing'
  return 'upcoming'
}

const getStatusText = (exam) => {
  const now = new Date()
  const startTime = new Date(exam.startTime)
  const endTime = new Date(exam.endTime)

  if (now > endTime) return '已结束'
  if (now >= startTime && now <= endTime) return '进行中'
  return '即将开始'
}

const getTimeUntil = (startTime) => {
  const now = new Date()
  const start = new Date(startTime)
  const diff = start - now

  if (diff <= 0) return '已结束'

  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
  const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
  const seconds = Math.floor((diff % (1000 * 60)) / 1000)

  if (days > 0) return `${days}天${hours}小时后开始`
  if (hours > 0) return `${hours}小时${minutes}分钟后开始`
  if (minutes > 0) return `${minutes}分钟后开始`
  return `${seconds}秒后开始`
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

onMounted(fetchExams)
</script>

<style scoped>
.page-header {
  margin-bottom: 32px;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.page-desc {
  font-size: 14px;
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
  font-size: 40px;
  margin-bottom: 16px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  text-align: center;
  color: var(--text-muted);
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-state h3 {
  font-size: 18px;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.exam-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 24px;
}

.exam-card {
  background: var(--bg-card);
  border-radius: 12px;
  padding: 24px;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
  transition: all 0.2s;
}

.exam-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.exam-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.exam-status {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.exam-status.upcoming {
  background: rgba(217, 119, 6, 0.1);
  color: var(--accent-amber);
}

.exam-status.ongoing {
  background: rgba(5, 150, 105, 0.1);
  color: var(--accent-mint);
}

.exam-status.ended {
  background: rgba(161, 161, 170, 0.1);
  color: var(--text-muted);
}

.exam-time {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-secondary);
}

.exam-name {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 16px;
  line-height: 1.4;
}

.exam-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 20px;
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 10px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--text-secondary);
}

.exam-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.score-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.total-score {
  font-size: 14px;
  color: var(--text-primary);
  font-weight: 500;
}

.pass-score {
  font-size: 12px;
  color: var(--text-secondary);
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
</style>
