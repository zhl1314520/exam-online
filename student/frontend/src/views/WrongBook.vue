<template>
  <div class="wrong-book-page fade-in">
    <header class="page-header">
      <h1 class="page-title">错题本</h1>
      <p class="page-desc">自动收录您的错题，便于复习</p>
    </header>

    <div v-if="loading" class="loading-container">
      <Icon icon="lucide:loader-2" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <div v-else-if="wrongQuestions.length === 0" class="empty-state">
      <Icon icon="lucide:book-check" class="empty-icon" />
      <h3>暂无错题</h3>
      <p>您的答题表现很好，继续保持！</p>
    </div>

    <div v-else>
      <div class="stats-cards">
        <div class="stat-card card">
          <div class="stat-icon">
            <Icon icon="lucide:book-x" />
          </div>
          <div class="stat-info">
            <span class="stat-value">{{ wrongQuestions.length }}</span>
            <span class="stat-label">错题总数</span>
          </div>
        </div>
        <div class="stat-card card">
          <div class="stat-icon">
            <Icon icon="lucide:repeat" />
          </div>
          <div class="stat-info">
            <span class="stat-value">{{ repeatedWrongCount }}</span>
            <span class="stat-label">重复错误</span>
          </div>
        </div>
      </div>

      <div class="wrong-questions-list">
        <div
          v-for="item in wrongQuestions"
          :key="item.wrongId"
          class="wrong-card card"
        >
          <div class="wrong-header">
            <div class="wrong-subject">
              <Icon icon="lucide:tag" />
              {{ item.subjectName }}
            </div>
            <div class="wrong-count">
              <Icon icon="lucide:rotate-ccw" />
              错误 {{ item.wrongCount }} 次
            </div>
            <button class="delete-btn" @click="deleteWrong(item.wrongId)">
              <Icon icon="lucide:trash-2" />
            </button>
          </div>

          <div class="wrong-question">
            <span class="question-type">{{ getQuestionTypeName(item.questionType) }}</span>
            <p class="question-content">{{ item.questionContent }}</p>
          </div>

          <div v-if="item.options" class="options-list">
            <div
              v-for="option in parseOptions(item.options)"
              :key="option.key"
              class="option-item"
            >
              <span class="option-key">{{ option.key }}</span>
              <span class="option-text">{{ option.value }}</span>
            </div>
          </div>

          <div class="wrong-answers">
            <div class="answer-row wrong">
              <span class="answer-label">您的答案</span>
              <span class="answer-value">{{ item.studentAnswer || '未作答' }}</span>
            </div>
            <div class="answer-row correct">
              <span class="answer-label">正确答案</span>
              <span class="answer-value">{{ item.correctAnswer }}</span>
            </div>
            <div v-if="item.knowledgePoint" class="knowledge-point">
              <Icon icon="lucide:lightbulb" />
              <span>{{ item.knowledgePoint }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import wrongApi from '@/api/wrong.js'

const wrongQuestions = ref([])
const loading = ref(true)

const repeatedWrongCount = computed(() => {
  return wrongQuestions.value.filter(q => q.wrongCount > 1).length
})

const fetchWrongQuestions = async () => {
  try {
    const res = await wrongApi.getWrongQuestions()
    wrongQuestions.value = res.data || []
  } catch (err) {
    console.error('获取错题本失败:', err)
  } finally {
    loading.value = false
  }
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

const deleteWrong = async (wrongId) => {
  if (!confirm('确定要删除这道错题吗？')) return
  try {
    await wrongApi.deleteWrongQuestion(wrongId)
    wrongQuestions.value = wrongQuestions.value.filter(q => q.wrongId !== wrongId)
  } catch (err) {
    alert(err.message || '删除失败')
  }
}

onMounted(fetchWrongQuestions)
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
  color: var(--success-color);
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

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 28px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
}

.stat-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: var(--radius-lg);
  background: linear-gradient(135deg, var(--error-color), #e07b39);
  color: white;
  font-size: 24px;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
}

.stat-label {
  font-size: 14px;
  color: var(--text-secondary);
}

.wrong-questions-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.wrong-card {
  padding: 20px;
}

.wrong-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.wrong-subject {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  background: rgba(74, 139, 168, 0.1);
  border-radius: 4px;
  font-size: 12px;
  color: var(--info-color);
}

.wrong-count {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: var(--error-color);
}

.delete-btn {
  margin-left: auto;
  padding: 8px;
  border: none;
  background: none;
  color: var(--text-muted);
  cursor: pointer;
  border-radius: var(--radius-sm);
  transition: all 0.2s;
}

.delete-btn:hover {
  background: rgba(209, 90, 90, 0.1);
  color: var(--error-color);
}

.wrong-question {
  margin-bottom: 16px;
}

.question-type {
  display: inline-block;
  padding: 2px 8px;
  background: var(--bg-secondary);
  border-radius: 4px;
  font-size: 12px;
  color: var(--text-muted);
  margin-bottom: 8px;
}

.question-content {
  font-size: 15px;
  color: var(--text-primary);
  line-height: 1.5;
}

.options-list {
  margin-bottom: 16px;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px;
  margin-bottom: 8px;
  border-radius: var(--radius-sm);
  background: var(--bg-secondary);
}

.option-key {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--bg-card);
  font-size: 12px;
  font-weight: 600;
}

.option-text {
  flex: 1;
  font-size: 14px;
}

.wrong-answers {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 14px;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
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
  border-radius: 4px;
}

.answer-row.wrong .answer-value {
  background: rgba(209, 90, 90, 0.1);
  color: var(--error-color);
}

.answer-row.correct .answer-value {
  background: rgba(61, 154, 90, 0.1);
  color: var(--success-color);
}

.knowledge-point {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid var(--border-color);
  font-size: 13px;
  color: var(--warning-color);
}
</style>
