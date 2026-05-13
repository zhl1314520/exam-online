<template>
  <div class="wrong-book-page">
    <header class="page-header">
      <h1 class="page-title">错题本</h1>
      <p class="page-desc">自动收录您的错题，便于复习</p>
    </header>

    <div v-if="loading" class="loading-container">
      <Icon icon="mdi:loading" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <div v-else-if="wrongQuestions.length === 0" class="empty-state">
      <Icon icon="mdi:book-check" class="empty-icon" />
      <h3>暂无错题</h3>
      <p>您的答题表现很好，继续保持！</p>
    </div>

    <div v-else>
      <div class="stats-cards">
        <div class="stat-card">
          <div class="stat-icon error">
            <Icon icon="mdi:book-remove" />
          </div>
          <div class="stat-info">
            <span class="stat-value">{{ wrongQuestions.length }}</span>
            <span class="stat-label">错题总数</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon warning">
            <Icon icon="mdi:repeat" />
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
              <Icon icon="mdi:tag" />
              {{ item.subjectName }}
            </div>
            <div class="wrong-count">
              <Icon icon="mdi:reload" />
              错误 {{ item.wrongCount }} 次
            </div>
            <el-button
              type="danger"
              size="small"
              plain
              circle
              @click="deleteWrong(item.wrongId)"
            >
              <Icon icon="mdi:delete" />
            </el-button>
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
              <Icon icon="mdi:lightbulb" />
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
  color: $success;
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

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: $spacing-lg;
  margin-bottom: $spacing-xl;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-lg;
  background: $bg-card;
  border-radius: $radius-lg;
  box-shadow: $shadow-sm;
  border: 1px solid $border-color;
}

.stat-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: $radius-lg;
  font-size: 24px;
  color: white;

  &.error {
    background: linear-gradient(135deg, $error, #ff6b6b);
  }

  &.warning {
    background: linear-gradient(135deg, $warning, #ffc107);
  }
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: $dark;
}

.stat-label {
  font-size: $font-size-sm;
  color: $gray;
}

.wrong-questions-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.wrong-card {
  background: $bg-card;
  border-radius: $radius-lg;
  padding: $spacing-xl;
  box-shadow: $shadow-sm;
  border: 1px solid $border-color;
}

.wrong-header {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  margin-bottom: $spacing-md;
}

.wrong-subject {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  padding: $spacing-xs $spacing-sm;
  background: rgba($info, 0.1);
  border-radius: $radius-sm;
  font-size: $font-size-xs;
  color: $info;
}

.wrong-count {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  font-size: $font-size-xs;
  color: $error;
}

.wrong-question {
  margin-bottom: $spacing-md;
}

.question-type {
  display: inline-block;
  padding: 2px 8px;
  background: $light;
  border-radius: $radius-sm;
  font-size: $font-size-xs;
  color: $gray;
  margin-bottom: $spacing-sm;
}

.question-content {
  font-size: $font-size-md;
  color: $dark;
  line-height: 1.5;
}

.options-list {
  margin-bottom: $spacing-md;
}

.option-item {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  padding: $spacing-sm $spacing-md;
  margin-bottom: $spacing-xs;
  border-radius: $radius-sm;
  background: $light;
}

.option-key {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: $bg-card;
  font-size: $font-size-xs;
  font-weight: 600;
  color: $dark;
}

.option-text {
  flex: 1;
  font-size: $font-size-sm;
  color: $dark;
}

.wrong-answers {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
  padding: $spacing-md;
  background: $light;
  border-radius: $radius-md;
}

.answer-row {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.answer-label {
  font-size: $font-size-xs;
  color: $gray;
  min-width: 70px;
}

.answer-value {
  font-weight: 500;
  padding: $spacing-xs $spacing-sm;
  border-radius: $radius-sm;
}

.answer-row.wrong .answer-value {
  background: rgba($error, 0.1);
  color: $error;
}

.answer-row.correct .answer-value {
  background: rgba($success, 0.1);
  color: $success;
}

.knowledge-point {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  margin-top: $spacing-sm;
  padding-top: $spacing-sm;
  border-top: 1px solid $border-color;
  font-size: $font-size-xs;
  color: $warning;
}
</style>