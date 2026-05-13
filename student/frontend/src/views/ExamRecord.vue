<template>
  <div class="exam-record-page">
    <header class="page-header">
      <h1 class="page-title">考试记录</h1>
      <p class="page-desc">查看您已完成的所有考试记录</p>
    </header>

    <div v-if="loading" class="loading-container">
      <Icon icon="mdi:loading" class="loading-icon" />
      <p>加载中...</p>
    </div>

    <div v-else-if="records.length === 0" class="empty-state">
      <Icon icon="mdi:file-document-remove" class="empty-icon" />
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
          <el-tag :type="record.totalScore >= record.passScore ? 'success' : 'danger'" size="small">
            {{ record.totalScore >= record.passScore ? '通过' : '未通过' }}
          </el-tag>
        </div>

        <div class="record-meta">
          <span class="meta-item">
            <Icon icon="mdi:calendar" />
            {{ formatDate(record.submitTime || record.startTime) }}
          </span>
          <span class="meta-item">
            <Icon icon="mdi:clock" />
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
          <el-button type="primary" @click="viewDetail(record.recordId)">
            <Icon icon="mdi:eye" />
            查看答卷
          </el-button>
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

.record-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
  gap: $spacing-lg;
}

.record-card {
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

.record-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: $spacing-md;
}

.record-name {
  font-size: $font-size-md;
  font-weight: 600;
  color: $dark;
  line-height: 1.4;
  flex: 1;
}

.record-meta {
  display: flex;
  gap: $spacing-lg;
  margin-bottom: $spacing-lg;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  font-size: $font-size-sm;
  color: $gray;
}

.record-score {
  padding: $spacing-md;
  background: $light;
  border-radius: $radius-md;
  margin-bottom: $spacing-lg;
}

.score-display {
  display: flex;
  align-items: baseline;
  gap: 4px;
  margin-bottom: $spacing-sm;
}

.score-value {
  font-size: 32px;
  font-weight: 700;
  color: $accent;
}

.score-total {
  font-size: $font-size-lg;
  color: $gray-light;
}

.score-bar {
  height: 6px;
  background: $light;
  border-radius: 3px;
  margin-bottom: $spacing-sm;
}

.score-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.3s;
}

.score-fill.pass {
  background: $success;
}

.score-fill.fail {
  background: $error;
}

.score-info {
  display: flex;
  justify-content: space-between;
  font-size: $font-size-xs;
  color: $gray;
}

.record-actions {
  display: flex;
  justify-content: flex-end;
}
</style>