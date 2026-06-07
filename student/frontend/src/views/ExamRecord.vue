<template>
  <div class="exam-record-page">
    <header class="page-header">
      <h1 class="page-title">考试记录</h1>
      <p class="page-desc">查看您已完成的所有考试记录</p>
    </header>

    <div v-if="loading" class="loading-container">
      <Icon icon="solar:loader-2-bold" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <div v-else-if="records.length === 0" class="empty-state">
      <Icon icon="solar:file-remove-bold" class="empty-icon" />
      <h3>暂无考试记录</h3>
      <p>您还没有完成任何考试</p>
    </div>

    <div v-else class="record-list">
      <div
        v-for="record in records"
        :key="record.recordId"
        class="record-card"
      >
        <div class="record-header">
          <h3 class="record-name">{{ record.examName }}</h3>
          <span
            class="status-badge"
            :class="record.totalScore >= record.passScore ? 'pass' : 'fail'"
          >
            {{ record.totalScore >= record.passScore ? '通过' : '未通过' }}
          </span>
        </div>

        <div class="record-meta">
          <span class="meta-item">
            <Icon icon="solar:calendar-bold" />
            {{ formatDate(record.submitTime || record.startTime) }}
          </span>
          <span class="meta-item">
            <Icon icon="solar:clock-circle-bold" />
            {{ record.duration }}分钟
          </span>
        </div>

        <div class="record-score">
          <div class="score-display">
            <span class="score-value" :class="getScoreClass(record.totalScore)">{{ record.totalScore }}</span>
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
          <button class="btn btn-primary" @click="viewDetail(record.recordId)">
            <Icon icon="solar:eye-bold" />
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

const getScoreClass = (score) => {
  if (score >= 90) return 'score-high'
  if (score >= 60) return 'score-mid'
  return 'score-low'
}

const viewDetail = (recordId) => {
  router.push(`/records/${recordId}`)
}

onMounted(fetchRecords)
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

.record-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
  gap: 24px;
}

.record-card {
  background: var(--bg-card);
  border-radius: 12px;
  padding: 24px;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
  transition: all 0.2s;
}

.record-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.record-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 16px;
}

.record-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  line-height: 1.4;
  flex: 1;
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
  background: var(--bg-tertiary);
  border-radius: 10px;
  margin-bottom: 20px;
}

.score-display {
  display: flex;
  align-items: baseline;
  gap: 4px;
  margin-bottom: 12px;
}

.score-value {
  font-size: 32px;
  font-weight: 700;
}

.score-value.score-high {
  color: var(--accent-mint);
}

.score-value.score-mid {
  color: var(--accent-amber);
}

.score-value.score-low {
  color: var(--accent-coral);
}

.score-total {
  font-size: 16px;
  color: var(--text-muted);
}

.score-bar {
  height: 6px;
  background: var(--bg-primary);
  border-radius: 3px;
  margin-bottom: 12px;
  overflow: hidden;
}

.score-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.3s;
}

.score-fill.pass {
  background: linear-gradient(90deg, var(--accent-mint), rgba(5, 150, 105, 0.6));
}

.score-fill.fail {
  background: linear-gradient(90deg, var(--accent-coral), rgba(225, 29, 72, 0.6));
}

.score-info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: var(--text-secondary);
}

.record-actions {
  display: flex;
  justify-content: flex-end;
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
</style>
