<template>
  <header class="header">
    <div class="header-left">
      <div class="toggle-btn" @click="$emit('toggle')">
        <Icon icon="mdi:menu" />
      </div>
      <div class="page-title-wrapper">
        <h1 class="page-title">{{ pageTitle }}</h1>
        <div class="breadcrumb-divider"></div>
        <span class="breadcrumb-text">在线考试管理系统</span>
      </div>
    </div>

    <div class="header-right">
      <div class="admin-info">
        <div class="admin-avatar">
          <Icon icon="mdi:account-circle" class="avatar-icon" />
        </div>
        <div class="admin-details">
          <span class="admin-name">{{ authStore.adminName }}</span>
          <span class="admin-role">管理员</span>
        </div>
      </div>

      <div class="header-divider"></div>

      <el-dropdown trigger="click" @command="handleCommand">
        <div class="action-btn">
          <Icon icon="mdi:dots-vertical" />
        </div>
        <template #dropdown>
          <el-dropdown-menu class="minimal-dropdown">
            <el-dropdown-item command="profile">
              <Icon icon="mdi:account-outline" class="dropdown-icon" />
              <span>个人信息</span>
            </el-dropdown-item>
            <el-dropdown-item command="password">
              <Icon icon="mdi:lock-outline" class="dropdown-icon" />
              <span>修改密码</span>
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <Icon icon="mdi:logout" class="dropdown-icon logout-icon" />
              <span class="logout-text">退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/stores/auth'

defineProps({
  collapsed: Boolean
})

defineEmits(['toggle'])

const route = useRoute()
const authStore = useAuthStore()

const pageTitle = computed(() => {
  const titles = {
    '/dashboard': '仪表盘',
    '/students': '学生管理',
    '/teachers': '教师管理',
    '/classes': '班级管理',
    '/departments': '院系管理',
    '/logs': '系统日志',
    '/statistics': '数据统计'
  }
  return titles[route.path] || '在线考试系统'
})

const handleCommand = (command) => {
  if (command === 'logout') {
    authStore.logout()
  }
}
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.header {
  height: 64px;
  background: $bg-card;
  border-bottom: $border-width solid $border-color;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 $spacing-xl;
  position: sticky;
  top: 0;
  z-index: 50;
}

.header-left {
  display: flex;
  align-items: center;
  gap: $spacing-lg;
}

.toggle-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: $radius-md;
  color: $gray;
  cursor: pointer;
  transition: all $transition-fast;
  font-size: 20px;

  &:hover {
    color: $dark;
    background: rgba($black, 0.04);
  }
}

.page-title-wrapper {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.page-title {
  font-size: $font-size-xl;
  font-weight: $font-weight-semibold;
  color: $dark;
  letter-spacing: -0.01em;
}

.breadcrumb-divider {
  width: 1px;
  height: 16px;
  background: $border-color;
}

.breadcrumb-text {
  font-size: $font-size-sm;
  color: $gray-light;
  font-weight: $font-weight-normal;
}

.header-right {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.admin-info {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-sm $spacing-md;
  background: rgba($black, 0.02);
  border-radius: $radius-lg;
  transition: all $transition-fast;

  &:hover {
    background: rgba($black, 0.04);
  }
}

.admin-avatar {
  width: 36px;
  height: 36px;
  border-radius: $radius-md;
  background: $accent;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  font-size: 20px;
  color: $white-bright;
}

.admin-details {
  display: flex;
  flex-direction: column;
  gap: $spacing-xs;
}

.admin-name {
  font-size: $font-size-md;
  font-weight: $font-weight-medium;
  color: $dark;
}

.admin-role {
  font-size: $font-size-xs;
  color: $gray-light;
}

.header-divider {
  width: 1px;
  height: 24px;
  background: $border-color;
}

.action-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: $radius-md;
  color: $gray;
  cursor: pointer;
  transition: all $transition-fast;
  font-size: 20px;

  &:hover {
    color: $dark;
    background: rgba($black, 0.04);
  }
}

.dropdown-icon {
  font-size: 18px;
  color: $gray;
  margin-right: $spacing-sm;
}

.logout-icon {
  color: $error;
}

.logout-text {
  color: $error;
  font-weight: $font-weight-medium;
}

// Dropdown样式覆盖
.minimal-dropdown {
  :deep(.el-dropdown-menu__item) {
    padding: $spacing-md $spacing-lg;
    font-size: $font-size-md;
    display: flex;
    align-items: center;
    transition: all $transition-fast;

    &:hover {
      background: rgba($accent, 0.04);
      color: $accent;

      .dropdown-icon {
        color: $accent;
      }
    }
  }
}
</style>