<template>
  <div class="record-detail-page">
    <header class="page-header">
      <div class="header-left">
        <button class="btn btn-secondary back-btn" @click="$router.back()">
          <Icon icon="solar:arrow-left-bold" />
          返回
        </button>
        <h1 class="page-title">答卷详情</h1>
      </div>
      <div class="header-right">
        <span
          class="status-badge"
          :class="record?.totalScore >= record?.passScore ? 'pass' : 'fail'"
        >
          {{ record?.totalScore >= record?.passScore ? '通过' : '未通过' }}
        </span>
      </div>
    </header>

    <div v-if="loading" class="loading-container">
      <Icon icon="solar:loader-2-bold" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <div v-else-if="record" class="record-detail">
      <!-- 考试概览卡片 -->
      <div class="overview-card">
        <div class="overview-grid">
          <div class="overview-item">
            <span class="overview-label">考试名称</span>
            <span class="overview-value">{{ record.examName }}</span>
          </div>
          <div class="overview-item">
            <span class="overview-label">试卷名称</span>
            <span class="overview-value">{{ record.paperName }}</span>
          </div>
          <div class="overview-item">
            <span class="overview-label">开始时间</span>
            <span class="overview-value">{{ formatTime(record.startTime) }}</span>
          </div>
          <div class="overview-item">
            <span class="overview-label">交卷时间</span>
            <span class="overview-value">{{ formatTime(record.submitTime) }}</span>
          </div>
          <div class="overview-item">
            <span class="overview-label">考试时长</span>
            <span class="overview-value">{{ record.duration }}分钟</span>
          </div>
          <div class="overview-item">
            <span class="overview-label">总切屏次数</span>
            <span class="overview-value" :class="{ 'warning': record.screenSwitchCount > 0 }">
              {{ record.screenSwitchCount }}次
            </span>
          </div>
        </div>

        <div class="score-section">
          <div class="score-box">
            <span class="score-number" :class="getScoreClass(record.totalScore)">{{ record.totalScore }}</span>
            <span class="score-label">总分</span>
          </div>
          <div class="score-details">
            <div class="score-detail-item">
              <span class="detail-label">客观题得分</span>
              <span class="detail-value">{{ record.objectiveScore }}分</span>
            </div>
            <div class="score-detail-item">
              <span class="detail-label">主观题得分</span>
              <span class="detail-value">{{ record.subjectiveScore }}分</span>
            </div>
            <div class="score-detail-item">
              <span class="detail-label">及格线</span>
              <span class="detail-value">{{ record.passScore }}分</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 题目列表 -->
      <div class="questions-section">
        <h2 class="section-title">答题详情</h2>

        <div
          v-for="(answer, index) in record.answers"
          :key="answer.answerId"
          class="question-card"
          :class="{ 'correct': answer.isCorrect === 1, 'incorrect': answer.isCorrect === 0 }"
        >
          <div class="question-header">
            <span class="question-number">第{{ index + 1 }}题</span>
            <span class="question-type">{{ getQuestionTypeName(answer.questionType) }}</span>
            <span class="question-score">
              <span class="score-obtained" :class="getScoreClass(answer.score)">{{ answer.score }}</span>
              <span class="score-full">/{{ answer.questionScore || '?' }}分</span>
            </span>
          </div>

          <div class="question-content">
            <p>{{ answer.questionContent }}</p>
          </div>

          <div v-if="answer.options" class="options-display">
            <div
              v-for="option in parseOptions(answer.options)"
              :key="option.key"
              class="option-item"
              :class="{
                'correct-option': isCorrectOption(option.key, answer.correctAnswer),
                'wrong-selected': isWrongSelected(option.key, answer.studentAnswer, answer.correctAnswer)
              }"
            >
              <span class="option-key">{{ option.key }}</span>
              <span class="option-text">{{ option.value }}</span>
            </div>
          </div>

          <div class="answer-display">
            <div class="answer-row">
              <span class="answer-label">您的答案</span>
              <span
                class="answer-value"
                :class="{ 'correct': answer.isCorrect === 1, 'incorrect': answer.isCorrect === 0 }"
              >
                {{ answer.studentAnswer || '未作答' }}
              </span>
            </div>
            <div class="answer-row" v-if="answer.correctAnswer">
              <span class="answer-label">正确答案</span>
              <span class="answer-value correct">{{ answer.correctAnswer }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Icon } from '@iconify/vue'
import recordApi from '@/api/record.js'

const route = useRoute()
const record = ref(null)
const loading = ref(true)

const fetchRecordDetail = async () => {
  try {
    const res = await recordApi.getRecordDetail(route.params.recordId)
    record.value = res.data
  } catch (err) {
    console.error('获取答卷详情失败:', err)
  } finally {
    loading.value = false
  }
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getQuestionTypeName = (type) => {
  const types = { 1: '单选题', 2: '多选题', 3: '判断题', 4: '填空题', 5: '简答题' }
  return types[type] || '未知题型'
}

const getScoreClass = (score) => {
  if (score >= 90) return 'score-high'
  if (score >= 60) return 'score-mid'
  return 'score-low'
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

const isCorrectOption = (key, correctAnswer) => {
  if (!correctAnswer) return false
  return correctAnswer.split('|').includes(key)
}

const isWrongSelected = (key, studentAnswer, correctAnswer) => {
  if (!studentAnswer) return false
  const selected = studentAnswer.split('|')
  const correct = correctAnswer ? correctAnswer.split('|') : []
  return selected.includes(key) && !correct.includes(key)
}

onMounted(fetchRecordDetail)
</script>

<style scoped>
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 28px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 6px;
}

.page-title {
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.pass {
  background: rgba(5, 150, 105, 0.1);
  color: var(--accent-mint);
}

.status-badge.fail {
  background: rgba(225, 29, 72, 0.1);
  color: var(--accent-coral);
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

.overview-card {
  padding: 28px;
  margin-bottom: 28px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  box-shadow: var(--shadow-sm);
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 28px;
}

.overview-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.overview-label {
  font-size: 13px;
  color: var(--text-muted);
}

.overview-value {
  font-size: 15px;
  font-weight: 500;
  color: var(--text-primary);
}

.overview-value.warning {
  color: var(--accent-coral);
}

.score-section {
  display: flex;
  align-items: center;
  gap: 40px;
  padding: 24px;
  background: var(--bg-tertiary);
  border-radius: 12px;
}

.score-box {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.score-number {
  font-size: 48px;
  font-weight: 700;
}

.score-number.score-high {
  color: var(--accent-mint);
}

.score-number.score-mid {
  color: var(--accent-amber);
}

.score-number.score-low {
  color: var(--accent-coral);
}

.score-label {
  font-size: 14px;
  color: var(--text-secondary);
}

.score-details {
  display: flex;
  gap: 32px;
}

.score-detail-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-label {
  font-size: 13px;
  color: var(--text-muted);
}

.detail-value {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 20px;
}

.question-card {
  padding: 20px;
  margin-bottom: 16px;
  background: var(--bg-card);
  border: 1px solid var(--border);
  border-radius: 12px;
  box-shadow: var(--shadow-sm);
}

.question-card.correct {
  border-left: 4px solid var(--accent-mint);
}

.question-card.incorrect {
  border-left: 4px solid var(--accent-coral);
}

.question-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.question-number {
  padding: 2px 8px;
  background: var(--bg-tertiary);
  border-radius: 6px;
  font-size: 12px;
  font-weight: 500;
  color: var(--text-secondary);
}

.question-type {
  font-size: 13px;
  color: var(--accent-sky);
}

.question-score {
  margin-left: auto;
}

.score-obtained {
  font-size: 18px;
  font-weight: 700;
}

.score-obtained.score-high {
  color: var(--accent-mint);
}

.score-obtained.score-mid {
  color: var(--accent-amber);
}

.score-obtained.score-low {
  color: var(--accent-coral);
}

.score-full {
  font-size: 14px;
  color: var(--text-muted);
}

.question-content {
  font-size: 15px;
  color: var(--text-primary);
  line-height: 1.6;
  margin-bottom: 16px;
}

.options-display {
  margin-bottom: 16px;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px;
  margin-bottom: 8px;
  border-radius: 8px;
  background: var(--bg-tertiary);
}

.option-item.correct-option {
  background: rgba(5, 150, 105, 0.1);
  border: 1px solid var(--accent-mint);
}

.option-item.wrong-selected {
  background: rgba(225, 29, 72, 0.1);
  border: 1px solid var(--accent-coral);
}

.option-key {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  background: var(--bg-card);
  font-size: 13px;
  font-weight: 600;
  border: 1px solid var(--border);
}

.option-text {
  flex: 1;
  font-size: 14px;
  color: var(--text-primary);
}

.answer-display {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 14px;
  background: var(--bg-tertiary);
  border-radius: 10px;
}

.answer-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.answer-label {
  font-size: 13px;
  color: var(--text-muted);
  min-width: 70px;
}

.answer-value {
  font-weight: 500;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 14px;
}

.answer-value.correct {
  background: rgba(5, 150, 105, 0.1);
  color: var(--accent-mint);
}

.answer-value.incorrect {
  background: rgba(225, 29, 72, 0.1);
  color: var(--accent-coral);
}
</style>
