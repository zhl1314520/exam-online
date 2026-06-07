<template>
  <div class="exam-taking-page">
    <!-- Header -->
    <header class="exam-header">
      <div class="exam-title">
        <h1>{{ examStore.currentExam?.examName }}</h1>
        <span class="exam-badge">进行中</span>
      </div>
      <div class="exam-actions">
        <div class="timer" :class="{ warning: remainingTime < 300 }">
          <Icon icon="solar:clock-circle-bold" />
          <span>{{ formatTime(remainingTime) }}</span>
        </div>
        <button class="btn btn-danger" @click="handleSubmit" :disabled="isSubmitted">
          {{ isSubmitted ? '已交卷' : '交卷' }}
        </button>
      </div>
    </header>

    <!-- Main -->
    <div class="exam-container">
      <!-- Question -->
      <div class="question-area">
        <div v-if="currentQuestion" class="question-card">
          <div class="question-header">
            <span class="question-type">{{ getQuestionTypeName(currentQuestion.questionType) }}</span>
            <span class="question-progress">第 {{ currentQuestionIndex + 1 }} 题 / 共 {{ examStore.questions.length }} 题</span>
          </div>

          <div class="question-content">
            {{ currentQuestion.questionContent }}
          </div>

          <!-- Options -->
          <div v-if="currentQuestion.questionType === 1" class="options-list">
            <label
              v-for="option in parseOptions(currentQuestion.options)"
              :key="option.key"
              class="option-item"
              :class="{ selected: examStore.answers[currentQuestion.questionId] === option.key }"
              @click="selectAnswer(option.key)"
            >
              <div class="option-label">{{ option.key }}</div>
              <div class="option-text">{{ option.value }}</div>
            </label>
          </div>

          <!-- Multi-select -->
          <div v-else-if="currentQuestion.questionType === 2" class="options-list">
            <label
              v-for="option in parseOptions(currentQuestion.options)"
              :key="option.key"
              class="option-item"
              :class="{ selected: isMultiSelected(option.key) }"
              @click="toggleMultiAnswer(option.key)"
            >
              <div class="option-label">{{ option.key }}</div>
              <div class="option-text">{{ option.value }}</div>
            </label>
          </div>

          <!-- True/False -->
          <div v-else-if="currentQuestion.questionType === 3" class="options-list">
            <label
              class="option-item"
              :class="{ selected: examStore.answers[currentQuestion.questionId] === '1' }"
              @click="selectAnswer('1')"
            >
              <div class="option-label">T</div>
              <div class="option-text">正确</div>
            </label>
            <label
              class="option-item"
              :class="{ selected: examStore.answers[currentQuestion.questionId] === '0' }"
              @click="selectAnswer('0')"
            >
              <div class="option-label">F</div>
              <div class="option-text">错误</div>
            </label>
          </div>

          <!-- Fill blank -->
          <div v-else-if="currentQuestion.questionType === 4" class="fill-blank">
            <input
              v-model="examStore.answers[currentQuestion.questionId]"
              type="text"
              class="input"
              placeholder="请输入答案"
              @blur="saveAnswer"
            />
          </div>

          <!-- Essay -->
          <div v-else-if="currentQuestion.questionType === 5" class="essay">
            <textarea
              v-model="examStore.answers[currentQuestion.questionId]"
              class="input"
              rows="6"
              placeholder="请输入答案"
              @blur="saveAnswer"
            ></textarea>
          </div>

          <div class="question-nav">
            <button
              v-if="currentQuestionIndex > 0"
              class="btn btn-secondary"
              @click="goToQuestion(currentQuestionIndex - 1)"
            >
              <Icon icon="solar:arrow-left-outline" />
              上一题
            </button>
            <button
              v-if="currentQuestionIndex < examStore.questions.length - 1"
              class="btn btn-primary"
              @click="goToQuestion(currentQuestionIndex + 1)"
            >
              下一题
              <Icon icon="solar:arrow-right-outline" />
            </button>
            <button
              v-else
              class="btn btn-primary"
              @click="handleSubmit"
              :disabled="isSubmitted"
            >
              {{ isSubmitted ? '已交卷' : '交卷' }}
              <Icon v-if="!isSubmitted" icon="solar:check-circle-bold" />
            </button>
          </div>
        </div>
      </div>

      <!-- Sidebar -->
      <aside class="exam-sidebar">
        <div class="sidebar-card">
          <div class="sidebar-title">
            <Icon icon="solar:checklist-minimalistic-bold" />
            答题卡
          </div>
          <div class="answer-grid">
            <div
              v-for="(q, index) in examStore.questions"
              :key="q.questionId"
              class="answer-num"
              :class="{
                current: currentQuestionIndex === index,
                answered: examStore.answers[q.questionId]
              }"
              @click="goToQuestion(index)"
            >
              {{ index + 1 }}
            </div>
          </div>
          <div class="answer-legend">
            <div class="legend-item">
              <div class="legend-dot current"></div>
              <span>当前</span>
            </div>
            <div class="legend-item">
              <div class="legend-dot answered"></div>
              <span>已答</span>
            </div>
            <div class="legend-item">
              <div class="legend-dot"></div>
              <span>未答</span>
            </div>
          </div>
        </div>

        <div class="sidebar-card">
          <div class="sidebar-title">
            <Icon icon="solar:info-circle-bold" />
            考试信息
          </div>
          <div class="exam-info-list">
            <div class="info-item">
              <span class="info-label">考试时长</span>
              <span class="info-value">{{ examStore.currentExam?.duration }}分钟</span>
            </div>
            <div class="info-item">
              <span class="info-label">总分</span>
              <span class="info-value">{{ examStore.currentExam?.totalScore }}分</span>
            </div>
            <div class="info-item">
              <span class="info-label">已答/总题</span>
              <span class="info-value">{{ examStore.answeredCount }} / {{ examStore.questions.length }}</span>
            </div>
            <div>
              <div class="info-item">
                <span class="info-label">完成进度</span>
                <span class="info-value">{{ examStore.progress }}%</span>
              </div>
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: examStore.progress + '%' }"></div>
              </div>
            </div>
          </div>
        </div>
      </aside>
    </div>

    <!-- Submit Modal -->
    <div v-if="showSubmitModal" class="modal-overlay">
      <div class="modal-card">
        <div class="modal-header">
          <Icon icon="solar:help-circle-bold" class="info-icon" />
          <h3>确认交卷</h3>
        </div>
        <p class="modal-body">
          您还有 {{ examStore.unansweredCount }} 道题目未作答，确定要交卷吗？
        </p>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showSubmitModal = false">
            继续答题
          </button>
          <button class="btn btn-danger" @click="confirmSubmit" :disabled="isSubmitting">
            {{ isSubmitting ? '提交中...' : '确认交卷' }}
          </button>
        </div>
      </div>
    </div>

    <!-- Result Modal -->
    <div v-if="showResultModal" class="modal-overlay">
      <div class="modal-card result-modal">
        <div class="modal-header">
          <Icon icon="solar:check-circle-bold" class="success-icon" />
          <h3>交卷成功</h3>
        </div>
        <div class="modal-body result-body">
          <div class="score-display">
            <div class="score-value">{{ submitResult.totalScore }}</div>
            <div class="score-label">得分</div>
          </div>
          <div class="score-detail">
            <div class="detail-item">
              <span>客观题得分</span>
              <span class="detail-value">{{ submitResult.objectiveScore }}</span>
            </div>
            <div v-if="submitResult.hasSubjective" class="detail-item">
              <span>主观题得分</span>
              <span class="detail-value waiting">待批改</span>
            </div>
          </div>
          <p v-if="submitResult.hasSubjective" class="result-tip">
            本次考试包含主观题，主观题部分需等待教师批改后显示最终成绩。
          </p>
        </div>
        <div class="modal-footer">
          <button class="btn btn-primary" @click="goToRecords">
            查看考试记录
          </button>
        </div>
      </div>
    </div>

    <!-- Screen Warning -->
    <div v-if="showScreenWarning" class="modal-overlay">
      <div class="modal-card">
        <div class="modal-header">
          <Icon icon="solar:danger-triangle-bold" class="warning-icon" />
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useExamStore } from '@/stores/exam.js'
import examTakingApi from '@/api/examTaking.js'

const router = useRouter()
const route = useRoute()
const examStore = useExamStore()

const currentQuestionIndex = ref(0)
const remainingTime = ref(0)
const showScreenWarning = ref(false)
const showSubmitModal = ref(false)
const showResultModal = ref(false)
const isSubmitting = ref(false)
const isSubmitted = ref(false)
const submitResult = ref({
  totalScore: 0,
  objectiveScore: 0,
  subjectiveScore: 0,
  hasSubjective: false
})

let timer = null
let autoSaveTimer = null

const goToRecords = () => {
  examStore.resetExam()
  router.push('/records')
}

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
  const lines = optionsStr.includes('\r\n') ? optionsStr.split('\r\n') : optionsStr.split('\n')
  return lines.map(line => {
    line = line.trim()
    if (!line) return null
    const match = line.match(/^([A-D])\.\s*(.+)$/)
    if (match) {
      return { key: match[1], value: match[2] }
    }
    const parts = line.split('.')
    if (parts.length >= 2) {
      return { key: parts[0].trim(), value: parts.slice(1).join('.').trim() }
    }
    return { key: line, value: '' }
  }).filter(Boolean)
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
  if (isSubmitted.value || isSubmitting.value) return
  showSubmitModal.value = true
}

const confirmSubmit = async () => {
  if (isSubmitting.value || isSubmitted.value) return
  isSubmitting.value = true
  try {
    const res = await examTakingApi.submitExam(route.params.recordId)
    isSubmitted.value = true
    showSubmitModal.value = false
    clearInterval(timer)
    clearInterval(autoSaveTimer)
    document.removeEventListener('visibilitychange', handleVisibilityChange)
    submitResult.value = {
      totalScore: res.data?.totalScore || 0,
      objectiveScore: res.data?.objectiveScore || 0,
      subjectiveScore: res.data?.subjectiveScore || 0,
      hasSubjective: res.data?.hasSubjective || false
    }
    showResultModal.value = true
  } catch (err) {
    alert(err.message || '交卷失败')
  } finally {
    isSubmitting.value = false
  }
}

const handleVisibilityChange = async () => {
  if (document.hidden) {
    if (isSubmitted.value) return
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

const startAutoSave = () => {
  autoSaveTimer = setInterval(async () => {
    if (currentQuestion.value) {
      await saveAnswer()
    }
  }, 30000)
}

const startTimer = () => {
  timer = setInterval(() => {
    if (remainingTime.value > 0) {
      remainingTime.value--
      examStore.setRemainingTime(remainingTime.value)
    } else {
      clearInterval(timer)
      if (!isSubmitted.value) {
        alert('考试时间已到，自动交卷！')
        confirmSubmit()
      }
    }
  }, 1000)
}

const loadQuestions = async () => {
  try {
    const res = await examTakingApi.getQuestions(route.params.recordId)
    examStore.setQuestions(res.data)

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
  font-family: 'Space Grotesk', sans-serif;
  background: var(--bg-primary);
  min-height: 100vh;
}

/* Header */
.exam-header {
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border);
  padding: 16px 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: var(--shadow-sm);
}

.exam-title {
  display: flex;
  align-items: center;
  gap: 16px;
}

.exam-title h1 {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.exam-badge {
  padding: 4px 12px;
  background: rgba(217, 119, 6, 0.1);
  color: var(--accent-amber);
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.exam-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.timer {
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: 'JetBrains Mono', monospace;
  font-size: 20px;
  font-weight: 600;
  color: var(--accent-sky);
}

.timer.warning {
  color: var(--accent-coral);
  animation: pulse 1s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.7; }
}

.timer .iconify {
  font-size: 20px;
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

.btn-danger {
  background: linear-gradient(135deg, var(--accent-coral), #DC2626);
  color: white;
}

.btn-danger:hover {
  opacity: 0.9;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Main Layout */
.exam-container {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  padding: 32px 40px;
  gap: 32px;
}

/* Question Area */
.question-area {
  flex: 1;
}

.question-card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 32px;
  box-shadow: var(--shadow-sm);
}

.question-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.question-type {
  padding: 6px 14px;
  background: rgba(2, 132, 199, 0.1);
  color: var(--accent-sky);
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.question-progress {
  font-size: 14px;
  color: var(--text-secondary);
}

.question-content {
  font-size: 17px;
  line-height: 1.7;
  margin-bottom: 32px;
  color: var(--text-primary);
}

/* Options */
.options-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.option-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 20px 24px;
  background: var(--bg-tertiary);
  border: 2px solid transparent;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
}

.option-item:hover {
  border-color: var(--accent-sky);
  background: rgba(2, 132, 199, 0.05);
}

.option-item.selected {
  border-color: var(--accent-sky);
  background: rgba(2, 132, 199, 0.08);
}

.option-label {
  width: 32px;
  height: 32px;
  background: var(--bg-card);
  border: 2px solid var(--border);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
  color: var(--text-secondary);
  flex-shrink: 0;
}

.option-item.selected .option-label {
  background: var(--accent-sky);
  border-color: var(--accent-sky);
  color: white;
}

.option-text {
  font-size: 15px;
  line-height: 1.6;
  padding-top: 4px;
  color: var(--text-primary);
}

/* Fill blank & Essay */
.fill-blank,
.essay {
  margin-bottom: 32px;
}

.fill-blank .input,
.essay .input {
  width: 100%;
  padding: 14px 16px;
  font-size: 15px;
  border: 1px solid var(--border);
  border-radius: 10px;
  background: var(--bg-secondary);
  color: var(--text-primary);
  resize: vertical;
  font-family: inherit;
}

.fill-blank .input:focus,
.essay .input:focus {
  outline: none;
  border-color: var(--accent-sky);
  box-shadow: 0 0 0 3px rgba(2, 132, 199, 0.1);
}

/* Navigation */
.question-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid var(--border);
}

/* Sidebar */
.exam-sidebar {
  width: 280px;
  flex-shrink: 0;
}

.sidebar-card {
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: var(--shadow-sm);
}

.sidebar-title {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--text-primary);
}

.answer-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 8px;
}

.answer-num {
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-tertiary);
  border: 1px solid var(--border);
  border-radius: 6px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  color: var(--text-secondary);
}

.answer-num:hover {
  border-color: var(--accent-sky);
  color: var(--accent-sky);
}

.answer-num.current {
  background: var(--accent-sky);
  color: white;
  border-color: var(--accent-sky);
}

.answer-num.answered {
  background: var(--accent-mint);
  color: white;
  border-color: var(--accent-mint);
}

.answer-legend {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 16px;
  font-size: 12px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--text-secondary);
}

.legend-dot {
  width: 10px;
  height: 10px;
  border-radius: 2px;
  border: 1px solid var(--border);
  background: var(--bg-tertiary);
}

.legend-dot.current {
  background: var(--accent-sky);
  border-color: var(--accent-sky);
}

.legend-dot.answered {
  background: var(--accent-mint);
  border-color: var(--accent-mint);
}

.exam-info-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}

.info-label {
  color: var(--text-secondary);
}

.info-value {
  color: var(--text-primary);
  font-weight: 500;
}

.progress-bar {
  height: 6px;
  background: var(--bg-tertiary);
  border-radius: 3px;
  margin-top: 8px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--accent-sky), var(--accent-mint));
  border-radius: 3px;
  transition: width 0.3s;
}

/* Modal */
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
  border-radius: 12px;
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
  color: var(--text-primary);
}

.warning-icon {
  font-size: 28px;
  color: var(--accent-amber);
}

.info-icon {
  font-size: 28px;
  color: var(--accent-sky);
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

/* Result Modal */
.result-modal {
  max-width: 360px;
}

.success-icon {
  font-size: 28px;
  color: var(--accent-mint);
}

.result-body {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.score-display {
  text-align: center;
}

.score-value {
  font-size: 48px;
  font-weight: 700;
  color: var(--accent-sky);
  line-height: 1;
}

.score-label {
  font-size: 14px;
  color: var(--text-secondary);
  margin-top: 4px;
}

.score-detail {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: var(--text-secondary);
}

.detail-value {
  font-weight: 600;
  color: var(--text-primary);
}

.detail-value.waiting {
  color: var(--accent-amber);
}

.result-tip {
  font-size: 12px;
  color: var(--text-muted);
  text-align: center;
  line-height: 1.5;
}
</style>
