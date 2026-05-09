<template>
  <div class="exam-record-page fade-in">
    <header class="page-header">
      <h1 class="page-title">考试记录</h1>
      <p class="page-desc">查看您已完成的所有考试记录</p>
    </header>

    <div v-if="loading" class="loading-container">
      <Icon icon="lucide:loader-2" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <div v-else-if="records.length === 0" class="empty-state">
      <Icon icon="lucide:file-x" class="empty-icon" />
      <h3>暂无考试记录</h3>
      <p>您还没有完成任何考试</p>
    </div>

    <div v-else class="record-list">
      <div
        v-for="record in records"
        :key="record.recordId"
        class="record-card card noise-bg"
      >
        <div class="record-header">
          <h3 class="record-name">{{ record.examName }}</h3>
          <span
            class="status-tag"
            :class="record.totalScore >= record.passScore ? 'status-success' : 'status-error'"
          >
            {{ record.totalScore >= record.passScore ? '通过' : '未通过' }}
          </span>
        </div>

        <div class="record-meta">
          <span class="meta-item">
            <Icon icon="lucide:calendar" />
            {{ formatDate(record.submitTime || record.startTime) }}
          </span>
          <span class="meta-item">
            <Icon icon="lucide:clock" />
            {{ record.duration }}分钟
          </span>
        </div>

        <div class="record-score">
          <div class="score-display">
            <span class="score-value">{{ record.totalScore }}</span>
            <span class="score-total">/ {{ record.totalScoreOfPaper }}</span>
          </div>
          <div class="score-bar">
            <div
              class="score-fill"
              :class="record.totalScore >= record.passScore ? 'pass' : 'fail'"
              :style="{ width: (record.totalScore / record.totalScoreOfPaper * 100) + '%' }"
            ></div>
          </div>
          <div class="score-info">
            <span>及格线: {{ record.passScore }}</span>
            <span>客观题: {{ record.objectiveScore }}分</span>
          </div>
        </div>

        <div class="record-actions">
          <button class="btn btn-secondary" @click="viewDetail(record.recordId)">
            <Icon icon="lucide:eye" />
            查看答卷
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
import recordApi from '@/api/record.js'

const router = useRouter()
const records = ref([])
const loading = ref(true)

const fetchRecords = async () => {
  try {
    const res = await recordApi.getAllExamRecords()
    records.value = res.data || []
  } catch (err) {
    console.error('获取考试记录失败:', err)
  } finally {
    loading.value = false
  }
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

const viewDetail = (recordId) => {
  router.push(`/records/${recordId}`)
}

onMounted(fetchRecords)
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

.record-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
  gap: 20px;
}

.record-card {
  padding: 24px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.record-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.record-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 12px;
}

.record-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  line-height: 1.4;
  flex: 1;
}

.record-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-secondary);
}

.record-score {
  padding: 16px;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  margin-bottom: 16px;
}

.score-display {
  display: flex;
  align-items: baseline;
  gap: 4px;
  margin-bottom: 10px;
}

.score-value {
  font-size: 32px;
  font-weight: 700;
  color: var(--primary-color);
}

.score-total {
  font-size: 16px;
  color: var(--text-muted);
}

.score-bar {
  height: 6px;
  background: var(--border-color);
  border-radius: 3px;
  margin-bottom: 10px;
}

.score-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.3s;
}

.score-fill.pass {
  background: var(--success-color);
}

.score-fill.fail {
  background: var(--error-color);
}

.score-info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: var(--text-muted);
}

.record-actions {
  display: flex;
  justify-content: flex-end;
}
</style>
