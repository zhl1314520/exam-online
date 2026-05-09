<template>
  <div class="layout">
    <header class="nav-bar">
      <div class="nav-content">
        <div class="nav-brand">
          <Icon icon="lucide:graduation-cap" class="brand-icon" />
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
          <div class="user-menu" @click="showUserMenu = !showUserMenu">
            <Icon icon="lucide:user-circle" class="user-icon" />
            <span class="user-name">{{ authStore.studentName }}</span>
            <Icon icon="lucide:chevron-down" class="chevron-icon" />

            <div v-if="showUserMenu" class="user-dropdown">
              <router-link to="/profile" class="dropdown-item">
                <Icon icon="lucide:settings" />
                <span>个人信息</span>
              </router-link>
              <div class="dropdown-divider"></div>
              <div class="dropdown-item" @click="handleLogout">
                <Icon icon="lucide:log-out" />
                <span>退出登录</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/stores/auth.js'

const router = useRouter()
const authStore = useAuthStore()

const showUserMenu = ref(false)

const menuItems = [
  { path: '/exams', label: '待考考试', icon: 'lucide:calendar-clock' },
  { path: '/records', label: '考试记录', icon: 'lucide:file-text' },
  { path: '/wrong-book', label: '错题本', icon: 'lucide:book-x' },
  { path: '/score-report', label: '成绩单', icon: 'lucide:chart-bar' }
]

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background: var(--bg-primary);
}

.nav-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 600;
  color: var(--primary-color);
}

.brand-icon {
  width: 28px;
  height: 28px;
}

.nav-menu {
  display: flex;
  gap: 8px;
}

.nav-icon {
  width: 18px;
  height: 18px;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-menu {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: background 0.2s;
}

.user-menu:hover {
  background: var(--bg-secondary);
}

.user-icon {
  width: 28px;
  height: 28px;
  color: var(--text-secondary);
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
}

.chevron-icon {
  width: 16px;
  height: 16px;
  color: var(--text-muted);
}

.user-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 8px;
  min-width: 160px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-lg);
  z-index: 100;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  font-size: 14px;
  color: var(--text-primary);
  cursor: pointer;
  transition: background 0.2s;
}

.dropdown-item:hover {
  background: var(--bg-secondary);
}

.dropdown-divider {
  height: 1px;
  background: var(--border-color);
  margin: 4px 0;
}

.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
}

@media (max-width: 768px) {
  .nav-menu span {
    display: none;
  }

  .nav-menu {
    gap: 4px;
  }

  .user-name {
    display: none;
  }
}
</style>
