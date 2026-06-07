<template>
  <aside class="sidebar" :class="{ collapsed: appStore.sidebarCollapsed }">
    <div class="sidebar-header">
      <div class="logo">
        <div class="logo-icon">
          <Icon icon="solar:notebook-bold" class="logo-icon-svg" />
        </div>
        <div v-show="!appStore.sidebarCollapsed" class="logo-text">
          <div class="logo-main">在线考试系统</div>
          <div class="logo-sub">教师端</div>
        </div>
      </div>
      <button class="collapse-btn" @click="appStore.toggleSidebar">
        <Icon :icon="appStore.sidebarCollapsed ? 'solar:alt-arrow-right-linear' : 'solar:alt-arrow-left-linear'" />
      </button>
    </div>

    <nav class="nav-menu">
      <router-link to="/dashboard" class="nav-item" :class="{ active: currentRoute === '/dashboard' }">
        <Icon icon="solar:widget-2-bold-duotone" class="nav-icon" />
        <span v-show="!appStore.sidebarCollapsed" class="nav-item-text">仪表盘</span>
      </router-link>
      <router-link to="/questions" class="nav-item" :class="{ active: currentRoute === '/questions' }">
        <Icon icon="solar:notebook-bold-duotone" class="nav-icon" />
        <span v-show="!appStore.sidebarCollapsed" class="nav-item-text">题库管理</span>
        <span v-if="questionCount > 0 && !appStore.sidebarCollapsed" class="nav-badge">{{ formatCount(questionCount) }}</span>
      </router-link>
      <router-link to="/papers" class="nav-item" :class="{ active: currentRoute === '/papers' }">
        <Icon icon="solar:document-add-bold-duotone" class="nav-icon" />
        <span v-show="!appStore.sidebarCollapsed" class="nav-item-text">试卷管理</span>
      </router-link>
      <router-link to="/exams" class="nav-item" :class="{ active: currentRoute === '/exams' }">
        <Icon icon="solar:clipboard-check-bold-duotone" class="nav-icon" />
        <span v-show="!appStore.sidebarCollapsed" class="nav-item-text">考试管理</span>
        <span v-if="ongoingExamCount > 0 && !appStore.sidebarCollapsed" class="nav-badge ongoing">进行中</span>
      </router-link>
      <router-link to="/records" class="nav-item" :class="{ active: currentRoute === '/records' }">
        <Icon icon="solar:chart-2-bold-duotone" class="nav-icon" />
        <span v-show="!appStore.sidebarCollapsed" class="nav-item-text">成绩分析</span>
      </router-link>
    </nav>

    <div class="sidebar-footer">
      <div class="user-info">
        <img :src="userAvatar" alt="Teacher" class="user-avatar" />
        <div v-show="!appStore.sidebarCollapsed" class="user-details">
          <div class="user-name">{{ authStore.teacherName || '教师' }}</div>
          <div class="user-role">{{ authStore.departmentName || '在线考试系统' }}</div>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/stores/auth'
import { useAppStore } from '@/stores/app'

const route = useRoute()
const authStore = useAuthStore()
const appStore = useAppStore()
const currentRoute = computed(() => route.path)

const questionCount = ref(0)
const ongoingExamCount = ref(0)

const userAvatar = computed(() => {
  return `https://picsum.photos/seed/${authStore.teacherNo || 'teacher1'}/80/80`
})

const formatCount = (count) => {
  if (count >= 1000) {
    return (count / 1000).toFixed(1) + 'k'
  }
  return count
}

onMounted(async () => {
  // Could fetch counts from API here
})
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.sidebar {
  width: 240px;
  background: $bg-primary;
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
  transition: width $transition-normal;

  &.collapsed {
    width: 72px;
    padding: 32px 12px;

    .nav-item {
      justify-content: center;
      padding: 14px 0;
    }
  }
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 48px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.collapse-btn {
  width: 28px;
  height: 28px;
  border-radius: 8px;
  background: $bg-tertiary;
  border: 1px solid $border-base;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all $transition-fast;

  &:hover {
    background: $bg-secondary;
    border-color: $border-dark;
  }

  .sidebar.collapsed & {
    position: absolute;
    top: 32px;
    right: -14px;
    background: $bg-primary;
    border-radius: 50%;
    box-shadow: $shadow-md;
  }
}

.logo-icon {
  width: 42px;
  height: 42px;
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 18px;
  color: white;
}

.logo-icon-svg {
  font-size: 24px;
  color: white;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  letter-spacing: -0.5px;
  color: $text-primary;

  span {
    color: $accent-mint;
  }
}

.logo-main {
  font-size: 18px;
  font-weight: 600;
  color: $text-primary;
}

.logo-sub {
  font-size: 12px;
  color: $text-secondary;
  margin-top: 2px;
}

.nav-menu {
  display: flex;
  flex-direction: column;
  gap: 4px;
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
    background: $primary-color;
    transform: scaleY(0);
    transition: transform $transition-normal;
  }

  &:hover {
    background: $bg-tertiary;
    color: $text-primary;
  }

  &.active {
    background: linear-gradient(90deg, rgba($primary-color, 0.1) 0%, transparent 100%);
    color: $primary-color;

    &::before {
      transform: scaleY(1);
    }
  }
}

.nav-icon {
  font-size: 20px;
}

.nav-item-text {
  font-size: 14px;
  font-weight: 500;
}

.nav-badge {
  margin-left: auto;
  background: $primary-color;
  color: white;
  font-size: 11px;
  font-weight: 600;
  padding: 2px 8px;
  border-radius: 20px;
}

.nav-badge.ongoing {
  background: $accent-coral;
}

.sidebar-footer {
  margin-top: auto;
  padding: 20px;
  background: $bg-secondary;
  border-radius: 12px;
  border: 1px solid $border-base;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  object-fit: cover;
  border: 2px solid $primary-color;
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