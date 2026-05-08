<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card" v-for="stat in stats" :key="stat.title">
        <div class="stat-icon" :style="{ background: stat.bgColor, color: stat.color }">
          <el-icon size="24">
            <component :is="stat.icon" />
          </el-icon>
        </div>
        <div class="stat-content">
          <h3 class="stat-value">{{ stat.value }}</h3>
          <p class="stat-label">{{ stat.title }}</p>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="content-grid">
      <div class="main-panel">
        <div class="panel-card">
          <div class="panel-header">
            <span class="panel-title">近期考试统计</span>
            <div class="panel-actions">
              <el-button text>
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </div>
          <div class="panel-content">
            <div class="chart-placeholder">
              <el-icon size="48" color="#cbd5e1"><TrendCharts /></el-icon>
              <p>考试统计图表区域</p>
            </div>
          </div>
        </div>

        <div class="panel-card">
          <div class="panel-header">
            <span class="panel-title">最近发布的考试</span>
            <router-link to="/exams" class="view-all">查看全部</router-link>
          </div>
          <div class="panel-content">
            <div class="exam-list">
              <div class="exam-item" v-for="exam in recentExams" :key="exam.examId">
                <div class="exam-info">
                  <div class="exam-name">{{ exam.examName }}</div>
                  <div class="exam-time">
                    <el-icon size="14"><Clock /></el-icon>
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
                <el-icon size="32" color="#cbd5e1"><Document /></el-icon>
                <p>暂无考试记录</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="side-panel">
        <div class="panel-card">
          <div class="panel-header">
            <span class="panel-title">快速操作</span>
          </div>
          <div class="panel-content">
            <div class="quick-actions">
              <router-link to="/questions" class="quick-action">
                <div class="action-icon" style="background: rgba(59, 130, 246, 0.1); color: #3b82f6;">
                  <el-icon size="20"><Plus /></el-icon>
                </div>
                <span>新增题目</span>
              </router-link>
              <router-link to="/papers" class="quick-action">
                <div class="action-icon" style="background: rgba(16, 185, 129, 0.1); color: #10b981;">
                  <el-icon size="20"><DocumentAdd /></el-icon>
                </div>
                <span>创建试卷</span>
              </router-link>
              <router-link to="/exams" class="quick-action">
                <div class="action-icon" style="background: rgba(245, 158, 11, 0.1); color: #f59e0b;">
                  <el-icon size="20"><Calendar /></el-icon>
                </div>
                <span>发布考试</span>
              </router-link>
              <router-link to="/records" class="quick-action">
                <div class="action-icon" style="background: rgba(239, 68, 68, 0.1); color: #ef4444;">
                  <el-icon size="20"><TrendCharts /></el-icon>
                </div>
                <span>查看成绩</span>
              </router-link>
            </div>
          </div>
        </div>

        <div class="panel-card">
          <div class="panel-header">
            <span class="panel-title">成绩分布</span>
          </div>
          <div class="panel-content">
            <div class="chart-placeholder small">
              <el-icon size="32" color="#cbd5e1"><PieChart /></el-icon>
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
import { Document, Collection, Calendar, TrendCharts, Refresh, Clock, Plus, DocumentAdd, PieChart } from '@element-plus/icons-vue'
import { getExamPage } from '@/api/exam'
import { getQuestionPage } from '@/api/question'
import { getPaperPage } from '@/api/examPaper'

const stats = ref([
  { title: '题库题目', value: 0, icon: 'Document', color: '#3b82f6', bgColor: 'rgba(59, 130, 246, 0.1)' },
  { title: '试卷数量', value: 0, icon: 'Collection', color: '#10b981', bgColor: 'rgba(16, 185, 129, 0.1)' },
  { title: '考试场次', value: 0, icon: 'Calendar', color: '#f59e0b', bgColor: 'rgba(245, 158, 11, 0.1)' },
  { title: '参考学生', value: 0, icon: 'TrendCharts', color: '#ef4444', bgColor: 'rgba(239, 68, 68, 0.1)' }
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
@import '@/styles/variables.scss';

.dashboard {
  max-width: 1400px;
}

/* 统计卡片 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: $radius-lg;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all $transition-normal;

  &:hover {
    box-shadow: $shadow-lg;
    transform: translateY(-2px);
  }
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: $radius-md;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-content {
  h3 {
    font-size: 28px;
    font-weight: 700;
    color: $text-primary;
    margin-bottom: 4px;
    font-family: $font-mono;
  }

  p {
    font-size: 14px;
    color: $text-muted;
  }
}

/* 内容网格 */
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
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: $radius-lg;
  overflow: hidden;
}

.panel-header {
  padding: 20px 24px;
  border-bottom: 1px solid $border-light;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-title {
  font-size: 16px;
  font-weight: 600;
  color: $text-primary;
}

.view-all {
  font-size: 13px;
  color: $primary-color;
  text-decoration: none;
  font-weight: 500;

  &:hover {
    text-decoration: underline;
  }
}

.panel-content {
  padding: 24px;
}

/* 图表占位符 */
.chart-placeholder {
  height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: $bg-secondary;
  border-radius: $radius-md;
  color: $text-muted;

  &.small {
    height: 200px;
  }

  p {
    margin-top: 12px;
    font-size: 14px;
  }
}

/* 考试列表 */
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
  background: $bg-secondary;
  border-radius: $radius-md;
  border: 1px solid transparent;
  transition: all $transition-fast;

  &:hover {
    border-color: $border-dark;
    background: $bg-tertiary;
  }
}

.exam-name {
  font-size: 14px;
  font-weight: 500;
  color: $text-primary;
  margin-bottom: 6px;
}

.exam-time {
  font-size: 12px;
  color: $text-muted;
  display: flex;
  align-items: center;
  gap: 4px;
}

.exam-status {
  font-size: 12px;
  font-weight: 500;
  padding: 4px 12px;
  border-radius: 20px;

  &.draft {
    background: rgba($text-muted, 0.1);
    color: $text-muted;
  }

  &.published {
    background: rgba($success-color, 0.1);
    color: $success-color;
  }

  &.ongoing {
    background: rgba($warning-color, 0.1);
    color: $warning-color;
  }

  &.ended {
    background: rgba($danger-color, 0.1);
    color: $danger-color;
  }
}

.empty-state {
  padding: 40px;
  text-align: center;
  color: $text-muted;

  p {
    margin-top: 12px;
    font-size: 14px;
  }
}

/* 快速操作 */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.quick-action {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 20px;
  background: $bg-secondary;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  text-decoration: none;
  border: 1px solid transparent;

  &:hover {
    background: $bg-tertiary;
    border-color: $border-dark;
  }

  .action-icon {
    width: 44px;
    height: 44px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  span {
    font-size: 13px;
    color: $text-secondary;
    font-weight: 500;
  }
}
</style>
