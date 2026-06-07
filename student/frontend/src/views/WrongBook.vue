<template>
  <div class="wrong-book-page">
    <header class="page-header">
      <h1 class="page-title">错题本</h1>
      <p class="page-desc">自动收录您的错题，便于复习</p>
    </header>

    <div v-if="loading" class="loading-container">
      <Icon icon="solar:loader-2-bold" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <div v-else-if="wrongQuestions.length === 0" class="empty-state">
      <Icon icon="solar:book-check-bold" class="empty-icon" />
      <h3>暂无错题</h3>
      <p>您的答题表现很好，继续保持！</p>
    </div>

    <div v-else>
      <div class="stats-cards">
        <div class="stat-card">
          <div class="stat-icon error">
            <Icon icon="solar:book-remove-bold" />
          </div>
          <div class="stat-info">
            <span class="stat-value">{{ wrongQuestions.length }}</span>
            <span class="stat-label">错题总数</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon warning">
            <Icon icon="solar:repeat-bold" />
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
          class="wrong-card"
        >
          <div class="wrong-header">
            <div class="wrong-subject">
              <Icon icon="solar:tag-bold" />
              {{ item.subjectName }}
            </div>
            <div class="wrong-count">
              <Icon icon="solar:refresh-bold" />
              错误 {{ item.wrongCount }} 次
            </div>
            <button
              class="delete-btn"
              @click="deleteWrong(item.wrongId)"
            >
              <Icon icon="solar:trash-bin-2-bold" />
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
              <Icon icon="solar:lamp-bold" />
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
import { ElMessage, ElMessageBox } from 'element-plus'
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
  try {
    await ElMessageBox.confirm('确定要删除这道错题吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await wrongApi.deleteWrongQuestion(wrongId)
    wrongQuestions.value = wrongQuestions.value.filter(q => q.wrongId !== wrongId)
    ElMessage.success('删除成功')
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error(err.message || '删除失败')
    }
  }
}

onMounted(fetchWrongQuestions)
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

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: var(--bg-card);
  border-radius: 12px;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.stat-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 12px;
  font-size: 24px;
  color: white;
}

.stat-icon.error {
  background: linear-gradient(135deg, var(--accent-coral), #DC2626);
}

.stat-icon.warning {
  background: linear-gradient(135deg, var(--accent-amber), #F59E0B);
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
  font-size: 13px;
  color: var(--text-secondary);
}

.wrong-questions-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.wrong-card {
  background: var(--bg-card);
  border-radius: 12px;
  padding: 24px;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
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
  background: rgba(2, 132, 199, 0.1);
  border-radius: 6px;
  font-size: 12px;
  color: var(--accent-sky);
}

.wrong-count {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: var(--accent-coral);
}

.delete-btn {
  margin-left: auto;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: 1px solid var(--border);
  border-radius: 8px;
  color: var(--text-muted);
  cursor: pointer;
  transition: all 0.2s;
}

.delete-btn:hover {
  border-color: var(--accent-coral);
  color: var(--accent-coral);
  background: rgba(225, 29, 72, 0.05);
}

.wrong-question {
  margin-bottom: 16px;
}

.question-type {
  display: inline-block;
  padding: 4px 10px;
  background: var(--bg-tertiary);
  border-radius: 6px;
  font-size: 12px;
  color: var(--text-secondary);
  margin-bottom: 8px;
}

.question-content {
  font-size: 15px;
  color: var(--text-primary);
  line-height: 1.6;
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
  border-radius: 8px;
  background: var(--bg-tertiary);
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
  color: var(--text-primary);
  border: 1px solid var(--border);
}

.option-text {
  flex: 1;
  font-size: 14px;
  color: var(--text-primary);
}

.wrong-answers {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 16px;
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
  color: var(--text-secondary);
  min-width: 70px;
}

.answer-value {
  font-weight: 500;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 14px;
}

.answer-row.wrong .answer-value {
  background: rgba(225, 29, 72, 0.1);
  color: var(--accent-coral);
}

.answer-row.correct .answer-value {
  background: rgba(5, 150, 105, 0.1);
  color: var(--accent-mint);
}

.knowledge-point {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  padding-top: 12px;
  border-top: 1px solid var(--border);
  font-size: 13px;
  color: var(--accent-amber);
}
</style>
