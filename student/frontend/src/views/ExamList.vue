<template>
  <div class="exam-list-page fade-in">
    <header class="page-header">
      <h1 class="page-title">待考考试</h1>
      <p class="page-desc">查看即将开始的考试并参加考试</p>
    </header>

    <div v-if="loading" class="loading-container">
      <Icon icon="lucide:loader-2" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <div v-else-if="exams.length === 0" class="empty-state">
      <Icon icon="lucide:calendar-x" class="empty-icon" />
      <h3>暂无待考考试</h3>
      <p>当前没有需要参加的考试</p>
    </div>

    <div v-else class="exam-grid">
      <div
        v-for="exam in exams"
        :key="exam.examId"
        class="exam-card card noise-bg"
      >
        <div class="exam-header">
          <div class="exam-subject">
            <Icon icon="lucide:book-open" />
            {{ exam.subjectName }}
          </div>
          <span class="status-tag" :class="getStatusClass(exam)">
            {{ getStatusText(exam) }}
          </span>
        </div>

        <h3 class="exam-name">{{ exam.examName }}</h3>

        <div class="exam-info">
          <div class="info-item">
            <Icon icon="lucide:clock" />
            <span>{{ exam.duration }}分钟</span>
          </div>
          <div class="info-item">
            <Icon icon="lucide:target" />
            <span>{{ exam.totalScore }}分</span>
          </div>
        </div>

        <div class="exam-time">
          <div class="time-row">
            <span class="time-label">开始时间</span>
            <span class="time-value">{{ formatTime(exam.startTime) }}</span>
          </div>
          <div class="time-row">
            <span class="time-label">结束时间</span>
            <span class="time-value">{{ formatTime(exam.endTime) }}</span>
          </div>
        </div>

        <button
          class="btn btn-primary start-btn"
          :disabled="!isExamAvailable(exam)"
          @click="startExam(exam)"
        >
          <Icon icon="lucide:play" />
          {{ isExamAvailable(exam) ? '参加考试' : '未到考试时间' }}
        </button>
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
  return date.toLocaleString('zh-CN', {
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
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

  if (now > endTime) return 'status-error'
  if (now >= startTime) return 'status-success'
  return 'status-info'
}

const getStatusText = (exam) => {
  const now = new Date()
  const startTime = new Date(exam.startTime)
  const endTime = new Date(exam.endTime)

  if (now > endTime) return '已结束'
  if (now >= startTime) return '进行中'
  return '未开始'
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

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  text-align: center;
}

.empty-icon {
  width: 64px;
  height: 64px;
  color: var(--text-muted);
  margin-bottom: 20px;
}

.empty-state h3 {
  font-size: 18px;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.empty-state p {
  color: var(--text-secondary);
}

.exam-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 20px;
}

.exam-card {
  padding: 24px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.exam-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.exam-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.exam-subject {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--primary-color);
  font-weight: 500;
}

.status-tag {
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status-success {
  background: rgba(61, 154, 90, 0.12);
  color: var(--success-color);
}

.status-info {
  background: rgba(74, 139, 168, 0.12);
  color: var(--info-color);
}

.status-error {
  background: rgba(209, 90, 90, 0.12);
  color: var(--error-color);
}

.exam-name {
  font-size: 17px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 16px;
  line-height: 1.4;
}

.exam-info {
  display: flex;
  gap: 24px;
  margin-bottom: 16px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--text-secondary);
}

.exam-time {
  padding: 14px;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  margin-bottom: 20px;
}

.time-row {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}

.time-row:not(:last-child) {
  margin-bottom: 8px;
}

.time-label {
  color: var(--text-muted);
}

.time-value {
  color: var(--text-primary);
  font-weight: 500;
}

.start-btn {
  width: 100%;
}

.start-btn:disabled {
  background: var(--text-muted);
  cursor: not-allowed;
}
</style>
