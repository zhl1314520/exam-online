<template>
  <div class="exam-taking-page">
    <!-- 顶部计时栏 -->
    <header class="exam-header-bar">
      <div class="header-left">
        <h1 class="exam-title">{{ examStore.currentExam?.examName }}</h1>
        <span class="exam-meta">{{ examStore.currentExam?.subjectName }}</span>
      </div>

      <div class="header-center">
        <div class="timer-box" :class="{ 'warning': remainingTime < 300 }">
          <Icon icon="lucide:clock" class="timer-icon" />
          <span class="timer-text">{{ formatTime(remainingTime) }}</span>
        </div>
      </div>

      <div class="header-right">
        <div class="progress-info">
          <span>已答 {{ examStore.answeredCount }} / {{ examStore.questions.length }} 题</span>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: examStore.progress + '%' }"></div>
          </div>
        </div>
        <button class="btn btn-accent submit-btn" @click="handleSubmit">
          <Icon icon="lucide:check-circle" />
          交卷
        </button>
      </div>
    </header>

    <!-- 主内容区 -->
    <div class="exam-content">
      <!-- 左侧题号导航 -->
      <aside class="question-nav-panel">
        <div class="nav-header">
          <h3>题号导航</h3>
        </div>
        <div class="nav-body">
          <div class="nav-grid">
            <button
              v-for="(q, index) in examStore.questions"
              :key="q.questionId"
              class="nav-item"
              :class="{
                'current': currentQuestionIndex === index,
                'answered': examStore.answers[q.questionId]
              }"
              @click="goToQuestion(index)"
            >
              {{ index + 1 }}
            </button>
          </div>
        </div>
        <div class="nav-footer">
          <div class="legend">
            <span class="legend-item">
              <span class="dot current"></span> 当前
            </span>
            <span class="legend-item">
              <span class="dot answered"></span> 已答
            </span>
            <span class="legend-item">
              <span class="dot"></span> 未答
            </span>
          </div>
        </div>
      </aside>

      <!-- 中间题目区 -->
      <main class="question-area">
        <div v-if="currentQuestion" class="question-card card">
          <div class="question-header">
            <span class="question-type">{{ getQuestionTypeName(currentQuestion.questionType) }}</span>
            <span class="question-score">{{ currentQuestion.score }}分</span>
          </div>

          <div class="question-content">
            <p class="question-text">{{ currentQuestionIndex + 1 }}. {{ currentQuestion.questionContent }}</p>
          </div>

          <!-- 单选题 -->
          <div v-if="currentQuestion.questionType === 1" class="options-list">
            <div
              v-for="option in parseOptions(currentQuestion.options)"
              :key="option.key"
              class="question-option"
              :class="{ 'selected': examStore.answers[currentQuestion.questionId] === option.key }"
              @click="selectAnswer(option.key)"
            >
              <span class="option-key">{{ option.key }}</span>
              <span class="option-text">{{ option.value }}</span>
            </div>
          </div>

          <!-- 多选题 -->
          <div v-else-if="currentQuestion.questionType === 2" class="options-list">
            <div
              v-for="option in parseOptions(currentQuestion.options)"
              :key="option.key"
              class="question-option"
              :class="{ 'selected': isMultiSelected(option.key) }"
              @click="toggleMultiAnswer(option.key)"
            >
              <span class="option-key">{{ option.key }}</span>
              <span class="option-text">{{ option.value }}</span>
            </div>
          </div>

          <!-- 判断题 -->
          <div v-else-if="currentQuestion.questionType === 3" class="options-list">
            <div
              class="question-option"
              :class="{ 'selected': examStore.answers[currentQuestion.questionId] === '1' }"
              @click="selectAnswer('1')"
            >
              <span class="option-key">T</span>
              <span class="option-text">正确</span>
            </div>
            <div
              class="question-option"
              :class="{ 'selected': examStore.answers[currentQuestion.questionId] === '0' }"
              @click="selectAnswer('0')"
            >
              <span class="option-key">F</span>
              <span class="option-text">错误</span>
            </div>
          </div>

          <!-- 填空题 -->
          <div v-else-if="currentQuestion.questionType === 4" class="fill-blank">
            <input
              v-model="examStore.answers[currentQuestion.questionId]"
              type="text"
              class="input"
              placeholder="请输入答案"
              @blur="saveAnswer"
            />
          </div>

          <!-- 简答题 -->
          <div v-else-if="currentQuestion.questionType === 5" class="essay">
            <textarea
              v-model="examStore.answers[currentQuestion.questionId]"
              class="input"
              rows="6"
              placeholder="请输入答案"
              @blur="saveAnswer"
            ></textarea>
          </div>

          <div class="question-actions">
            <button
              v-if="currentQuestionIndex > 0"
              class="btn btn-secondary"
              @click="goToQuestion(currentQuestionIndex - 1)"
            >
              <Icon icon="lucide:chevron-left" />
              上一题
            </button>
            <button
              v-if="currentQuestionIndex < examStore.questions.length - 1"
              class="btn btn-primary"
              @click="goToQuestion(currentQuestionIndex + 1)"
            >
              下一题
              <Icon icon="lucide:chevron-right" />
            </button>
          </div>
        </div>
      </main>
    </div>

    <!-- 切屏警告弹窗 -->
    <div v-if="showScreenWarning" class="modal-overlay">
      <div class="modal-card">
        <div class="modal-header">
          <Icon icon="lucide:alert-triangle" class="warning-icon" />
          <h3>警告</h3>
        </div>
        <p class="modal-body">
          检测到切屏行为！您已切屏 {{ examStore.screenSwitchCount }} 次，
          超过 {{ examStore.currentExam?.allowScreenSwitch || 3 }} 次将自动交卷。
        </p>
        <div class="modal-footer">
          <button class="btn btn-primary" @click="showScreenWarning = false">
            我知道了
          </button>
        </div>
      </div>
    </div>

    <!-- 确认交卷弹窗 -->
    <div v-if="showSubmitModal" class="modal-overlay">
      <div class="modal-card">
        <div class="modal-header">
          <Icon icon="lucide:help-circle" class="info-icon" />
          <h3>确认交卷</h3>
        </div>
        <p class="modal-body">
          您还有 {{ examStore.unansweredCount }} 道题目未作答，确定要交卷吗？
        </p>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showSubmitModal = false">
            继续答题
          </button>
          <button class="btn btn-primary" @click="confirmSubmit">
            确认交卷
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useExamStore } from '@/stores/exam.js'
import { useAuthStore } from '@/stores/auth.js'
import examTakingApi from '@/api/examTaking.js'

const router = useRouter()
const route = useRoute()
const examStore = useExamStore()
const authStore = useAuthStore()

const currentQuestionIndex = ref(0)
const remainingTime = ref(0)
const showScreenWarning = ref(false)
const showSubmitModal = ref(false)

let timer = null
let autoSaveTimer = null

const currentQuestion = computed(() => {
  return examStore.questions[currentQuestionIndex.value] || null
})

const formatTime = (seconds) => {
  const hours = Math.floor(seconds / 3600)
  const mins = Math.floor((seconds % 3600) / 60)
  const secs = seconds % 60
  return `${hours.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
}

const getQuestionTypeName = (type) => {
  const types = { 1: '单选题', 2: '多选题', 3: '判断题', 4: '填空题', 5: '简答题' }
  return types[type] || '未知题型'
}

const parseOptions = (optionsStr) => {
  if (!optionsStr) return []
  return optionsStr.split('\\n').map(line => {
    const match = line.match(/^([A-D])\.\s*(.+)$/)
    if (match) {
      return { key: match[1], value: match[2] }
    }
    const parts = line.split('.')
    if (parts.length >= 2) {
      return { key: parts[0].trim(), value: parts.slice(1).join('.').trim() }
    }
    return { key: line, value: '' }
  })
}

const isMultiSelected = (key) => {
  const current = examStore.answers[currentQuestion.value.questionId] || ''
  return current.split('|').includes(key)
}

const selectAnswer = async (key) => {
  if (!currentQuestion.value) return
  examStore.setAnswer(currentQuestion.value.questionId, key)
  await saveAnswer()
}

const toggleMultiAnswer = async (key) => {
  if (!currentQuestion.value) return
  const current = examStore.answers[currentQuestion.value.questionId] || ''
  const selected = current ? current.split('|') : []
  const index = selected.indexOf(key)

  if (index > -1) {
    selected.splice(index, 1)
  } else {
    selected.push(key)
  }

  const sorted = selected.sort()
  examStore.setAnswer(currentQuestion.value.questionId, sorted.join('|'))
  await saveAnswer()
}

const saveAnswer = async () => {
  if (!currentQuestion.value) return
  try {
    await examTakingApi.saveAnswer(
      route.params.recordId,
      currentQuestion.value.questionId,
      examStore.answers[currentQuestion.value.questionId] || ''
    )
  } catch (err) {
    console.error('保存答案失败:', err)
  }
}

const goToQuestion = (index) => {
  currentQuestionIndex.value = index
}

const handleSubmit = () => {
  if (examStore.unansweredCount > 0) {
    showSubmitModal.value = true
  } else {
    showSubmitModal.value = true
  }
}

const confirmSubmit = async () => {
  try {
    await examTakingApi.submitExam(route.params.recordId)
    alert('交卷成功！')
    examStore.resetExam()
    router.push('/records')
  } catch (err) {
    alert(err.message || '交卷失败')
  }
}

// 切屏检测
const handleVisibilityChange = async () => {
  if (document.hidden) {
    try {
      await examTakingApi.screenSwitch(route.params.recordId)
      const count = await examTakingApi.getScreenSwitchCount(route.params.recordId)
      examStore.setScreenSwitchCount(count.data)

      if (count.data >= (examStore.currentExam?.allowScreenSwitch || 3)) {
        alert('切屏次数超限，自动交卷！')
        await confirmSubmit()
      } else {
        showScreenWarning.value = true
      }
    } catch (err) {
      console.error('切屏处理失败:', err)
    }
  }
}

// 自动保存
const startAutoSave = () => {
  autoSaveTimer = setInterval(async () => {
    if (currentQuestion.value) {
      await saveAnswer()
    }
  }, 30000) // 每30秒自动保存
}

// 倒计时
const startTimer = () => {
  timer = setInterval(() => {
    if (remainingTime.value > 0) {
      remainingTime.value--
      examStore.setRemainingTime(remainingTime.value)
    } else {
      clearInterval(timer)
      alert('考试时间已到，自动交卷！')
      confirmSubmit()
    }
  }, 1000)
}

const loadQuestions = async () => {
  try {
    const res = await examTakingApi.getQuestions(route.params.recordId)
    examStore.setQuestions(res.data)

    // 加载临时答案
    const tempRes = await examTakingApi.getTempAnswers(route.params.recordId)
    examStore.loadAnswers(tempRes.data)
  } catch (err) {
    console.error('加载题目失败:', err)
  }
}

onMounted(async () => {
  remainingTime.value = examStore.remainingTime

  if (!examStore.currentExam) {
    router.push('/exams')
    return
  }

  await loadQuestions()
  startTimer()
  startAutoSave()

  document.addEventListener('visibilitychange', handleVisibilityChange)
})

onBeforeUnmount(() => {
  clearInterval(timer)
  clearInterval(autoSaveTimer)
  document.removeEventListener('visibilitychange', handleVisibilityChange)
})
</script>

<style scoped>
.exam-taking-page {
  min-height: 100vh;
  background: var(--bg-primary);
}

.exam-header-bar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 24px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.exam-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.exam-meta {
  font-size: 13px;
  color: var(--text-muted);
}

.header-center {
  display: flex;
  align-items: center;
}

.timer-box {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  font-size: 20px;
  font-weight: 600;
  color: var(--primary-color);
}

.timer-box.warning {
  background: rgba(209, 90, 90, 0.1);
  color: var(--error-color);
  animation: pulse 1s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.7; }
}

.timer-icon {
  width: 20px;
  height: 20px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.progress-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 6px;
  font-size: 13px;
  color: var(--text-secondary);
}

.progress-bar {
  width: 120px;
  height: 4px;
  background: var(--border-color);
  border-radius: 2px;
}

.progress-fill {
  height: 100%;
  background: var(--primary-color);
  border-radius: 2px;
  transition: width 0.3s;
}

.submit-btn {
  display: flex;
  align-items: center;
  gap: 6px;
}

.exam-content {
  display: grid;
  grid-template-columns: 220px 1fr;
  gap: 24px;
  padding: 88px 24px 24px;
  max-width: 1400px;
  margin: 0 auto;
}

@media (max-width: 992px) {
  .exam-content {
    grid-template-columns: 1fr;
  }
}

.question-nav-panel {
  position: sticky;
  top: 88px;
  background: var(--bg-card);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-color);
  max-height: calc(100vh - 112px);
  overflow: hidden;
}

.nav-header {
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-color);
  background: linear-gradient(90deg, var(--bg-secondary) 0%, transparent 100%);
}

.nav-header h3 {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.nav-body {
  padding: 16px;
  max-height: calc(100vh - 280px);
  overflow-y: auto;
}

.nav-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  background: var(--bg-card);
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s;
}

.nav-item:hover {
  border-color: var(--primary-light);
  color: var(--primary-color);
}

.nav-item.current {
  border-color: var(--primary-color);
  background: var(--primary-color);
  color: white;
}

.nav-item.answered {
  border-color: var(--success-color);
  background: rgba(61, 154, 90, 0.1);
  color: var(--success-color);
}

.nav-item.answered.current {
  background: var(--primary-color);
  color: white;
}

.nav-footer {
  padding: 12px 16px;
  border-top: 1px solid var(--border-color);
}

.legend {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: var(--text-secondary);
}

.dot {
  width: 12px;
  height: 12px;
  border: 1px solid var(--border-color);
  border-radius: 3px;
  background: var(--bg-card);
}

.dot.current {
  border-color: var(--primary-color);
  background: var(--primary-color);
}

.dot.answered {
  border-color: var(--success-color);
  background: rgba(61, 154, 90, 0.1);
}

.question-area {
  min-height: calc(100vh - 112px);
}

.question-card {
  padding: 28px;
}

.question-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.question-type {
  padding: 4px 12px;
  background: rgba(74, 139, 168, 0.1);
  color: var(--info-color);
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.question-score {
  font-size: 14px;
  color: var(--accent-color);
  font-weight: 600;
}

.question-content {
  margin-bottom: 24px;
}

.question-text {
  font-size: 16px;
  line-height: 1.7;
  color: var(--text-primary);
}

.options-list {
  margin-bottom: 28px;
}

.question-option {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px 18px;
  margin-bottom: 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 0.2s;
}

.question-option:hover {
  border-color: var(--primary-light);
  background: var(--bg-secondary);
}

.question-option.selected {
  border-color: var(--primary-color);
  background: rgba(45, 125, 70, 0.05);
}

.option-key {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border: 2px solid var(--border-color);
  border-radius: 50%;
  font-size: 13px;
  font-weight: 600;
  color: var(--text-secondary);
}

.question-option.selected .option-key {
  border-color: var(--primary-color);
  background: var(--primary-color);
  color: white;
}

.option-text {
  flex: 1;
  font-size: 15px;
  color: var(--text-primary);
}

.fill-blank,
.essay {
  margin-bottom: 28px;
}

.fill-blank .input,
.essay .input {
  width: 100%;
  padding: 14px 16px;
  font-size: 15px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  resize: vertical;
}

.fill-blank .input:focus,
.essay .input:focus {
  outline: none;
  border-color: var(--primary-light);
  box-shadow: 0 0 0 3px rgba(45, 125, 70, 0.1);
}

.question-actions {
  display: flex;
  justify-content: space-between;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

.modal-card {
  width: 100%;
  max-width: 400px;
  padding: 24px;
  background: var(--bg-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
}

.modal-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.modal-header h3 {
  font-size: 18px;
  font-weight: 600;
}

.warning-icon {
  width: 28px;
  height: 28px;
  color: var(--warning-color);
}

.info-icon {
  width: 28px;
  height: 28px;
  color: var(--info-color);
}

.modal-body {
  font-size: 15px;
  color: var(--text-secondary);
  margin-bottom: 20px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
