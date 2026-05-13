<template>
  <div class="exam-list-page">
    <header class="page-header">
      <h1 class="page-title">待考考试</h1>
      <p class="page-desc">查看即将开始的考试并参加考试</p>
    </header>

    <div v-if="loading" class="loading-container">
      <Icon icon="mdi:loading" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <div v-else-if="exams.length === 0" class="empty-state">
      <Icon icon="mdi:calendar-blank" class="empty-icon" />
      <h3>暂无待考考试</h3>
      <p>当前没有需要参加的考试</p>
    </div>

    <div v-else class="exam-grid">
      <div
        v-for="exam in exams"
        :key="exam.examId"
        class="exam-card"
      >
        <div class="exam-header">
          <div class="exam-subject">
            <Icon icon="mdi:book-open" />
            {{ exam.subjectName }}
          </div>
          <el-tag :type="getStatusType(exam)" size="small">
            {{ getStatusText(exam) }}
          </el-tag>
        </div>

        <h3 class="exam-name">{{ exam.examName }}</h3>

        <div class="exam-info">
          <div class="info-item">
            <Icon icon="mdi:clock" />
            <span>{{ exam.duration }}分钟</span>
          </div>
          <div class="info-item">
            <Icon icon="mdi:target" />
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

        <el-button
          type="primary"
          size="large"
          class="start-btn"
          :disabled="!isExamAvailable(exam)"
          @click="startExam(exam)"
        >
          <Icon icon="mdi:play" />
          {{ isExamAvailable(exam) ? '参加考试' : '未到考试时间' }}
        </el-button>
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
import { ElMessage } from 'element-plus'

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

const getStatusType = (exam) => {
  const now = new Date()
  const startTime = new Date(exam.startTime)
  const endTime = new Date(exam.endTime)

  if (now > endTime) return 'danger'
  if (now >= startTime) return 'success'
  return 'info'
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
    ElMessage.error(err.message || '开始考试失败')
  }
}

onMounted(fetchExams)
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.page-header {
  margin-bottom: $spacing-xl;
}

.page-title {
  font-size: $font-size-2xl;
  font-weight: 600;
  color: $dark;
  margin-bottom: $spacing-xs;
}

.page-desc {
  font-size: $font-size-md;
  color: $gray;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: $spacing-2xl;
  color: $gray;
}

.loading-icon {
  font-size: 40px;
  margin-bottom: $spacing-md;
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
  padding: $spacing-2xl;
  text-align: center;
}

.empty-icon {
  font-size: 64px;
  color: $gray-light;
  margin-bottom: $spacing-lg;
}

.empty-state h3 {
  font-size: $font-size-lg;
  color: $dark;
  margin-bottom: $spacing-sm;
}

.empty-state p {
  color: $gray;
}

.exam-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: $spacing-lg;
}

.exam-card {
  background: $bg-card;
  border-radius: $radius-lg;
  padding: $spacing-xl;
  box-shadow: $shadow-sm;
  border: 1px solid $border-color;
  transition: all $transition-normal;

  &:hover {
    transform: translateY(-2px);
    box-shadow: $shadow-md;
  }
}

.exam-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: $spacing-md;
}

.exam-subject {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  font-size: $font-size-sm;
  color: $accent;
  font-weight: 500;
}

.exam-name {
  font-size: $font-size-lg;
  font-weight: 600;
  color: $dark;
  margin-bottom: $spacing-md;
  line-height: 1.4;
}

.exam-info {
  display: flex;
  gap: $spacing-lg;
  margin-bottom: $spacing-md;
}

.info-item {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  font-size: $font-size-sm;
  color: $gray;
}

.exam-time {
  padding: $spacing-md;
  background: $light;
  border-radius: $radius-md;
  margin-bottom: $spacing-lg;
}

.time-row {
  display: flex;
  justify-content: space-between;
  font-size: $font-size-sm;

  &:not(:last-child) {
    margin-bottom: $spacing-xs;
  }
}

.time-label {
  color: $gray;
}

.time-value {
  color: $dark;
  font-weight: 500;
}

.start-btn {
  width: 100%;
}
</style>