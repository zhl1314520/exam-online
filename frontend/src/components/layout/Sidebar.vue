<template>
  <aside class="sidebar" :class="{ 'sidebar-collapsed': collapsed }">
    <!-- Logo区域 -->
    <div class="sidebar-header">
      <div class="logo-area">
        <div class="logo-icon-wrapper">
          <Icon icon="mdi:school" class="logo-icon" />
        </div>
        <div v-if="!collapsed" class="logo-content">
          <span class="logo-text">考试系统</span>
          <span class="logo-badge">Admin</span>
        </div>
      </div>
    </div>

    <!-- 导航区域 -->
    <nav class="sidebar-nav">
      <div
        v-for="item in menuItems"
        :key="item.path"
        class="nav-item"
        :class="{ 'active': currentPath === item.path }"
        @click="navigateTo(item.path)"
      >
        <div class="nav-icon-wrapper">
          <Icon :icon="item.icon" class="nav-icon" />
        </div>
        <span v-if="!collapsed" class="nav-text">{{ item.title }}</span>
        <div v-if="currentPath === item.path && !collapsed" class="active-indicator"></div>
      </div>
    </nav>

    <!-- 底部折叠按钮 -->
    <div class="sidebar-footer">
      <div class="collapse-btn" @click="$emit('toggle')">
        <Icon :icon="collapsed ? 'mdi:chevron-right' : 'mdi:chevron-left'" />
      </div>
    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Icon } from '@iconify/vue'

defineProps({
  collapsed: Boolean
})

defineEmits(['toggle'])

const router = useRouter()
const route = useRoute()

const currentPath = computed(() => route.path)

const menuItems = [
  { path: '/dashboard', title: '仪表盘', icon: 'mdi:view-dashboard-outline' },
  { path: '/students', title: '学生管理', icon: 'mdi:account-group-outline' },
  { path: '/teachers', title: '教师管理', icon: 'mdi:account-tie-outline' },
  { path: '/classes', title: '班级管理', icon: 'mdi:google-classroom' },
  { path: '/departments', title: '院系管理', icon: 'mdi:office-building-outline' },
  { path: '/logs', title: '系统日志', icon: 'mdi:text-box-outline' },
  { path: '/statistics', title: '数据统计', icon: 'mdi:chart-bar' }
]

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.sidebar {
  width: 240px;
  height: 100vh;
  background: $bg-sidebar;
  position: fixed;
  left: 0;
  top: 0;
  display: flex;
  flex-direction: column;
  transition: width $transition-normal $ease-minimal;
  z-index: 100;
  border-right: $border-width solid rgba($white-bright, 0.08);

  &.sidebar-collapsed {
    width: 72px;

    .logo-badge {
      display: none;
    }

    .nav-text {
      display: none;
    }

    .nav-icon-wrapper {
      margin: 0;
    }
  }
}

.sidebar-header {
  padding: $spacing-xl $spacing-lg;
  border-bottom: $border-width solid $border-color-dark;
}

.logo-area {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.logo-icon-wrapper {
  width: 40px;
  height: 40px;
  border-radius: $radius-md;
  background: $accent;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all $transition-fast;
}

.logo-icon {
  font-size: 24px;
  color: $white-bright;
}

.logo-content {
  display: flex;
  flex-direction: column;
  gap: $spacing-xs;
}

.logo-text {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $white-bright;
  letter-spacing: 0.02em;
}

.logo-badge {
  font-size: $font-size-xs;
  font-weight: $font-weight-medium;
  color: $gray-light;
  letter-spacing: 0.05em;
}

.sidebar-nav {
  flex: 1;
  padding: $spacing-md $spacing-sm;
  overflow-y: auto;
  overflow-x: hidden;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: $spacing-md $spacing-md;
  margin-bottom: $spacing-xs;
  border-radius: $radius-md;
  color: $gray-light;
  cursor: pointer;
  transition: all $transition-fast $ease-minimal;
  position: relative;

  &:hover {
    color: $white-bright;
    background: rgba($white-bright, 0.04);

    .nav-icon-wrapper {
      background: rgba($white-bright, 0.08);
    }
  }

  &.active {
    color: $accent;
    background: rgba($accent, 0.08);

    .nav-icon-wrapper {
      background: $accent;

      .nav-icon {
        color: $white-bright;
      }
    }

    .nav-text {
      font-weight: $font-weight-medium;
    }
  }
}

.nav-icon-wrapper {
  width: 32px;
  height: 32px;
  border-radius: $radius-sm;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: $spacing-md;
  transition: all $transition-fast $ease-minimal;
}

.nav-icon {
  font-size: 20px;
  transition: color $transition-fast;
}

.nav-text {
  font-size: $font-size-md;
  font-weight: $font-weight-normal;
  white-space: nowrap;
  letter-spacing: 0.01em;
}

.active-indicator {
  position: absolute;
  right: $spacing-md;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 4px;
  border-radius: $radius-full;
  background: $accent;
}

.sidebar-footer {
  padding: $spacing-md;
  border-top: $border-width solid $border-color-dark;
}

.collapse-btn {
  width: 100%;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: $radius-md;
  background: rgba($white-bright, 0.04);
  color: $gray-light;
  cursor: pointer;
  transition: all $transition-fast;
  font-size: 20px;

  &:hover {
    background: rgba($white-bright, 0.08);
    color: $white-bright;
  }
}

// 滚动条样式
.sidebar-nav::-webkit-scrollbar {
  width: 4px;
}

.sidebar-nav::-webkit-scrollbar-thumb {
  background: rgba($white-bright, 0.16);
  border-radius: $radius-full;
}
</style>