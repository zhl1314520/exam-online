<template>
  <div class="dashboard fade-in">

    <!-- Statistics cards -->
    <div class="stats-row">
      <div class="stat-card card" v-for="stat in stats" :key="stat.title">
        <div class="stat-icon" :style="{ background: stat.bgColor, color: stat.color }">
          <Icon :icon="stat.icon" class="stat-icon-inner" />
        </div>
        <div class="stat-content">
          <h3 class="stat-value">{{ stat.value }}</h3>
          <p class="stat-label">{{ stat.title }}</p>
        </div>
      </div>
    </div>

    <!-- Content layout -->
    <div class="content-grid">
      <div class="main-panel">
        <div class="panel-card card">
          <div class="panel-header">
            <span class="panel-title">近期考试统计</span>
            <div class="panel-actions">
              <button class="refresh-btn" @click="loadStats">
                <Icon icon="solar:refresh-linear" class="refresh-icon" />
              </button>
            </div>
          </div>
          <div class="panel-content">
            <div class="chart-placeholder">
              <Icon icon="solar:chart-2-bold-duotone" class="chart-icon" />
              <p>考试统计图表区域</p>
            </div>
          </div>
        </div>

        <div class="panel-card card">
          <div class="panel-header">
            <span class="panel-title">最近发布的考试</span>
            <router-link to="/exams" class="view-all">
              查看全部
              <Icon icon="solar:arrow-right-linear" class="arrow-icon" />
            </router-link>
          </div>
          <div class="panel-content">
            <div class="exam-list">
              <div class="exam-item" v-for="exam in recentExams" :key="exam.examId">
                <div class="exam-info">
                  <div class="exam-name">{{ exam.examName }}</div>
                  <div class="exam-time">
                    <Icon icon="solar:clock-circle-linear" class="time-icon" />
                    {{ exam.startTime }}
                  </div>
                </div>
                <div class="exam-meta">
                  <span class="exam-status" :class="getStatusClass(exam.status)">
                    {{ getStatusText(exam.status) }}
                  </span>
                </div>
              </div>
              <div v-if="recentExams.length === 0" class="empty-state">
                <Icon icon="solar:document-bold-duotone" class="empty-icon" />
                <p>暂无考试记录</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="side-panel">
        <div class="panel-card card">
          <div class="panel-header">
            <span class="panel-title">快速操作</span>
          </div>
          <div class="panel-content">
            <div class="quick-actions">
              <router-link to="/questions" class="quick-action">
                <div class="action-icon" style="background: rgba(16, 185, 129, 0.15); color: var(--accent-mint);">
                  <Icon icon="solar:add-circle-bold-duotone" class="action-icon-inner" />
                </div>
                <span>新增题目</span>
              </router-link>
              <router-link to="/papers" class="quick-action">
                <div class="action-icon" style="background: rgba(14, 165, 233, 0.15); color: var(--accent-sky);">
                  <Icon icon="solar:document-add-bold-duotone" class="action-icon-inner" />
                </div>
                <span>创建试卷</span>
              </router-link>
              <router-link to="/exams" class="quick-action">
                <div class="action-icon" style="background: rgba(245, 158, 11, 0.15); color: var(--accent-amber);">
                  <Icon icon="solar:calendar-bold-duotone" class="action-icon-inner" />
                </div>
                <span>发布考试</span>
              </router-link>
              <router-link to="/records" class="quick-action">
                <div class="action-icon" style="background: rgba(255, 107, 107, 0.15); color: var(--accent-coral);">
                  <Icon icon="solar:chart-2-bold-duotone" class="action-icon-inner" />
                </div>
                <span>查看成绩</span>
              </router-link>
            </div>
          </div>
        </div>

        <div class="panel-card card">
          <div class="panel-header">
            <span class="panel-title">成绩分布</span>
          </div>
          <div class="panel-content">
            <div class="chart-placeholder small">
              <Icon icon="solar:pie-chart-2-bold-duotone" class="chart-icon small" />
              <p>成绩分布图表</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { getExamPage } from '@/api/exam'
import { getQuestionPage } from '@/api/question'
import { getPaperPage } from '@/api/examPaper'

const stats = ref([
  { title: '题库题目', value: 0, icon: 'solar:database-bold-duotone', color: '#F59E0B', bgColor: 'rgba(245, 158, 11, 0.15)' },
  { title: '试卷数量', value: 0, icon: 'solar:document-bold-duotone', color: '#10B981', bgColor: 'rgba(16, 185, 129, 0.15)' },
  { title: '考试场次', value: 0, icon: 'solar:calendar-bold-duotone', color: '#0EA5E9', bgColor: 'rgba(14, 165, 233, 0.15)' },
  { title: '参考学生', value: 0, icon: 'solar:users-group-rounded-bold-duotone', color: '#FF6B6B', bgColor: 'rgba(255, 107, 107, 0.15)' }
])

const recentExams = ref([])

const getStatusText = (status) => {
  const map = { 0: '未发布', 1: '已发布', 2: '进行中', 3: '已结束' }
  return map[status] || '未知'
}

const getStatusClass = (status) => {
  const map = { 0: 'draft', 1: 'published', 2: 'ongoing', 3: 'ended' }
  return map[status] || ''
}

const loadStats = async () => {
  try {
    const [questionRes, paperRes, examRes] = await Promise.all([
      getQuestionPage({ page: 1, size: 1 }),
      getPaperPage({ page: 1, size: 1 }),
      getExamPage({ page: 1, size: 1 })
    ])

    stats.value[0].value = questionRes.data?.total || 0
    stats.value[1].value = paperRes.data?.total || 0
    stats.value[2].value = examRes.data?.total || 0
    stats.value[3].value = 0
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

const loadRecentExams = async () => {
  try {
    const res = await getExamPage({ page: 1, size: 5 })
    recentExams.value = res.data?.list || []
  } catch (error) {
    console.error('加载考试列表失败', error)
  }
}

onMounted(() => {
  loadStats()
  loadRecentExams()
})
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.dashboard {
  max-width: 1400px;
  position: relative;
}

/* Glow orbs - decorative colored blur effects */
.glow-orb {
  position: fixed;
  border-radius: 50%;
  filter: blur(120px);
  opacity: 0.12;
  pointer-events: none;
  z-index: 0;
}

.glow-orb-1 {
  width: 500px;
  height: 500px;
  background: $accent-mint;
  top: -150px;
  left: 20%;
}

.glow-orb-2 {
  width: 400px;
  height: 400px;
  background: $accent-amber;
  bottom: -100px;
  right: 15%;
}

/* Statistics cards - 原型样式 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
}

.stat-content {
  h3 {
    font-size: 24px;
    font-weight: 700;
    color: $text-primary;
    margin-bottom: 4px;
    font-family: 'JetBrains Mono', monospace;
  }

  p {
    font-size: 13px;
    color: $text-muted;
  }
}

/* Content layout */
.content-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

.main-panel {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.side-panel {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.panel-card {
  overflow: hidden;
}

.panel-header {
  padding: 20px 24px;
  border-bottom: 1px solid $border-base;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-title {
  font-size: 16px;
  font-weight: 600;
  color: $text-primary;
}

.panel-actions {
  display: flex;
  gap: 8px;
}

.refresh-btn {
  padding: 8px;
  background: transparent;
  border: 1px solid $border-base;
  border-radius: $radius-sm;
  color: $text-muted;
  cursor: pointer;
  transition: all $transition-fast;

  &:hover {
    background: $bg-tertiary;
    border-color: $border-dark;
    color: $text-secondary;
  }

  .refresh-icon {
    font-size: 18px;
  }
}

.view-all {
  font-size: 13px;
  color: $accent-mint;
  text-decoration: none;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: all $transition-fast;

  &:hover {
    color: $primary-dark;

    .arrow-icon {
      transform: translateX(2px);
    }
  }

  .arrow-icon {
    font-size: 14px;
    transition: transform $transition-fast;
  }
}

.panel-content {
  padding: 24px;
}

/* Chart placeholder */
.chart-placeholder {
  height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: $bg-tertiary;
  border-radius: $radius-md;
  color: $text-muted;

  &.small {
    height: 200px;
  }

  .chart-icon {
    font-size: 48px;

    &.small {
      font-size: 32px;
    }
  }

  p {
    margin-top: 12px;
    font-size: 14px;
  }
}

/* Exam list */
.exam-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.exam-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: $bg-tertiary;
  border-radius: $radius-md;
  border: 1px solid transparent;
  transition: all $transition-fast;

  &:hover {
    border-color: $border-base;
    background: $bg-hover;
  }
}

.exam-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.exam-name {
  font-size: 14px;
  font-weight: 500;
  color: $text-primary;
}

.exam-time {
  font-size: 12px;
  color: $text-muted;
  display: flex;
  align-items: center;
  gap: 4px;

  .time-icon {
    font-size: 14px;
  }
}

.exam-status {
  font-size: 12px;
  font-weight: 500;
  padding: 4px 12px;
  border-radius: 20px;

  &.draft {
    background: rgba($text-muted, 0.15);
    color: $text-muted;
  }

  &.published {
    background: rgba($success-color, 0.15);
    color: $success-color;
  }

  &.ongoing {
    background: rgba($warning-color, 0.15);
    color: $warning-color;
  }

  &.ended {
    background: rgba($danger-color, 0.15);
    color: $danger-color;
  }
}

.empty-state {
  padding: 40px;
  text-align: center;
  color: $text-muted;

  .empty-icon {
    font-size: 32px;
  }

  p {
    margin-top: 12px;
    font-size: 14px;
  }
}

/* Quick actions - 原型样式 */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.quick-action {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px;
  background: $bg-tertiary;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
  text-decoration: none;
  border: 1px solid transparent;

  &:hover {
    border-color: $border-base;
  }

  .action-icon {
    width: 44px;
    height: 44px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;

    .action-icon-inner {
      font-size: 24px;
    }
  }

  span {
    font-size: 12px;
    color: $text-secondary;
  }
}
</style>