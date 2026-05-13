<template>
  <div class="layout">
    <header class="nav-bar">
      <div class="nav-content">
        <div class="nav-brand">
          <Icon icon="mdi:school" class="brand-icon" />
          <span class="brand-text">在线考试系统</span>
        </div>

        <nav class="nav-menu">
          <router-link
            v-for="item in menuItems"
            :key="item.path"
            :to="item.path"
            class="nav-item"
            :class="{ active: $route.path === item.path }"
          >
            <Icon :icon="item.icon" class="nav-icon" />
            <span>{{ item.label }}</span>
          </router-link>
        </nav>

        <div class="nav-right">
          <el-dropdown @command="handleCommand">
            <div class="user-menu">
              <Icon icon="mdi:account-circle" class="user-icon" />
              <span class="user-name">{{ authStore.studentName }}</span>
              <Icon icon="mdi:chevron-down" class="chevron-icon" />
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <Icon icon="mdi:account" class="dropdown-icon" />
                  <span>个人信息</span>
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <Icon icon="mdi:logout" class="dropdown-icon" />
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/stores/auth.js'

const router = useRouter()
const authStore = useAuthStore()

const menuItems = [
  { path: '/exams', label: '待考考试', icon: 'mdi:calendar-clock' },
  { path: '/records', label: '考试记录', icon: 'mdi:file-document' },
  { path: '/wrong-book', label: '错题本', icon: 'mdi:book-remove' },
  { path: '/score-report', label: '成绩单', icon: 'mdi:chart-bar' }
]

const handleCommand = (command) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    authStore.logout()
    router.push('/login')
  }
}
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.layout {
  min-height: 100vh;
  background: $bg-primary;
}

.nav-bar {
  background: $bg-card;
  border-bottom: 1px solid $border-color;
  box-shadow: $shadow-sm;
}

.nav-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 $spacing-xl;
  height: 64px;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  font-size: 18px;
  font-weight: 600;
  color: $accent;
}

.brand-icon {
  font-size: 28px;
  color: $accent;
}

.nav-menu {
  display: flex;
  gap: $spacing-xs;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  padding: $spacing-sm $spacing-md;
  color: $gray;
  text-decoration: none;
  border-radius: $radius-md;
  font-size: $font-size-md;
  font-weight: $font-weight-medium;
  transition: all $transition-fast;

  &:hover {
    color: $accent;
    background: rgba($accent, 0.05);
  }

  &.active {
    color: $accent;
    background: rgba($accent, 0.1);
  }
}

.nav-icon {
  font-size: 20px;
}

.nav-right {
  display: flex;
  align-items: center;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  padding: $spacing-sm $spacing-md;
  border-radius: $radius-md;
  cursor: pointer;
  transition: background $transition-fast;

  &:hover {
    background: $light;
  }
}

.user-icon {
  font-size: 28px;
  color: $gray;
}

.user-name {
  font-size: $font-size-md;
  font-weight: $font-weight-medium;
  color: $dark;
}

.chevron-icon {
  font-size: 16px;
  color: $gray-light;
}

.dropdown-icon {
  font-size: 18px;
  margin-right: $spacing-sm;
}

.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: $spacing-xl;
}

@media (max-width: 768px) {
  .nav-menu span {
    display: none;
  }

  .user-name {
    display: none;
  }
}
</style>