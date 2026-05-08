<template>
  <aside class="sidebar">
    <div class="logo">
      <div class="logo-icon">E</div>
      <div class="logo-text">Exam<span>Flow</span></div>
    </div>

    <nav class="nav-section">
      <div class="nav-label">概览</div>
      <router-link to="/dashboard" class="nav-item" :class="{ active: currentRoute === '/dashboard' }">
        <el-icon size="20"><Grid /></el-icon>
        <span class="nav-item-text">仪表盘</span>
      </router-link>
    </nav>

    <nav class="nav-section">
      <div class="nav-label">题库</div>
      <router-link to="/questions" class="nav-item" :class="{ active: currentRoute === '/questions' }">
        <el-icon size="20"><Document /></el-icon>
        <span class="nav-item-text">题库管理</span>
      </router-link>
    </nav>

    <nav class="nav-section">
      <div class="nav-label">试卷</div>
      <router-link to="/papers" class="nav-item" :class="{ active: currentRoute === '/papers' }">
        <el-icon size="20"><Collection /></el-icon>
        <span class="nav-item-text">试卷管理</span>
      </router-link>
    </nav>

    <nav class="nav-section">
      <div class="nav-label">考试</div>
      <router-link to="/exams" class="nav-item" :class="{ active: currentRoute === '/exams' }">
        <el-icon size="20"><Calendar /></el-icon>
        <span class="nav-item-text">考试管理</span>
      </router-link>
      <router-link to="/records" class="nav-item" :class="{ active: currentRoute === '/records' }">
        <el-icon size="20"><TrendCharts /></el-icon>
        <span class="nav-item-text">成绩分析</span>
      </router-link>
    </nav>

    <div class="sidebar-footer">
      <div class="user-info">
        <el-avatar :size="40" :icon="UserFilled" class="user-avatar" />
        <div class="user-details">
          <div class="user-name">{{ authStore.teacherName || '教师' }}</div>
          <div class="user-role">在线考试系统</div>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { Grid, Document, Collection, Calendar, TrendCharts, UserFilled } from '@element-plus/icons-vue'

const route = useRoute()
const authStore = useAuthStore()
const currentRoute = computed(() => route.path)
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.sidebar {
  width: 280px;
  background: linear-gradient(180deg, $bg-secondary 0%, $bg-primary 100%);
  border-right: 1px solid $border-base;
  padding: 32px 20px;
  position: fixed;
  height: 100vh;
  display: flex;
  flex-direction: column;
  z-index: 100;
  left: 0;
  top: 0;
  bottom: 0;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 12px;
  margin-bottom: 48px;
}

.logo-icon {
  width: 42px;
  height: 42px;
  background: linear-gradient(135deg, $success-color 0%, darken($success-color, 10%) 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 18px;
  color: white;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  letter-spacing: -0.5px;
  color: $text-primary;

  span {
    color: $success-color;
  }
}

.nav-section {
  margin-bottom: 32px;
}

.nav-label {
  font-size: 11px;
  font-weight: 500;
  color: $text-muted;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  padding: 0 12px;
  margin-bottom: 12px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 16px;
  border-radius: 10px;
  color: $text-secondary;
  cursor: pointer;
  transition: all $transition-normal;
  margin-bottom: 4px;
  position: relative;
  overflow: hidden;
  text-decoration: none;

  &::before {
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    width: 3px;
    height: 100%;
    background: $success-color;
    transform: scaleY(0);
    transition: transform $transition-normal;
  }

  &:hover {
    background: $bg-tertiary;
    color: $text-primary;
  }

  &.active {
    background: linear-gradient(90deg, rgba($success-color, 0.1) 0%, transparent 100%);
    color: $text-primary;

    &::before {
      transform: scaleY(1);
    }
  }
}

.nav-item-text {
  font-size: 14px;
  font-weight: 500;
}

.sidebar-footer {
  margin-top: auto;
  padding: 20px;
  background: $bg-tertiary;
  border-radius: 12px;
  border: 1px solid $border-base;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  border-radius: 10px;
  border: 2px solid $success-color;
}

.user-details {
  flex: 1;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: $text-primary;
}

.user-role {
  font-size: 12px;
  color: $text-muted;
}
</style>
