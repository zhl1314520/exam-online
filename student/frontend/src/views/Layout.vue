<template>
  <div class="app-container">
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <div class="logo">
          <div class="logo-icon">
            <Icon icon="solar:book-bold" style="color: white; font-size: 22px;" />
          </div>
          <div class="logo-text">
            在线考试
            <span>学生端</span>
          </div>
        </div>
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

      <div class="sidebar-footer">
        <button class="logout-btn" @click="handleLogout">
          <Icon icon="solar:logout-2-outline" class="logout-icon" />
          <span>退出登录</span>
        </button>
      </div>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/stores/auth.js'

const authStore = useAuthStore()

const menuItems = [
  { path: '/exams', label: '首页', icon: 'solar:home-2-bold' },
  { path: '/pending-exams', label: '待考考试', icon: 'solar:calendar-bold' },
  { path: '/records', label: '考试记录', icon: 'solar:document-text-bold' },
  { path: '/wrong-book', label: '错题本', icon: 'solar:bookmark-bold' },
  { path: '/score-report', label: '成绩单', icon: 'solar:chart-2-bold' },
  { path: '/profile', label: '个人信息', icon: 'solar:user-id-bold' }
]

const handleLogout = () => {
  authStore.logout()
  window.location.href = 'http://localhost:5173/login'
}
</script>

<style lang="scss" scoped>
.app-container {
  display: flex;
  min-height: 100vh;
}

/* Sidebar */
.sidebar {
  width: 260px;
  background: var(--bg-secondary);
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  position: fixed;
  height: 100vh;
  box-shadow: var(--shadow-sm);
}

.sidebar-header {
  padding: 24px;
  border-bottom: 1px solid var(--border);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, var(--accent-sky), var(--accent-mint));
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-text {
  font-weight: 600;
  font-size: 18px;
  color: var(--text-primary);
}

.logo-text span {
  color: var(--text-secondary);
  font-size: 12px;
  font-weight: 400;
}

.nav-menu {
  padding: 16px;
  flex: 1;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 8px;
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 14px;
  transition: all 0.2s;
  margin-bottom: 4px;
}

.nav-item:hover {
  background: var(--bg-tertiary);
  color: var(--text-primary);
}

.nav-item.active {
  background: var(--bg-tertiary);
  color: var(--accent-sky);
}

.nav-icon {
  font-size: 20px;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid var(--border);
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
  padding: 12px 16px;
  border-radius: 8px;
  background: transparent;
  border: none;
  color: var(--text-secondary);
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.logout-btn:hover {
  background: var(--bg-tertiary);
  color: var(--accent-coral);
}

.logout-icon {
  font-size: 20px;
}

/* Main Content */
.main-content {
  flex: 1;
  margin-left: 260px;
  padding: 32px 40px;
  background: var(--bg-primary);
  min-height: 100vh;
}
</style>
