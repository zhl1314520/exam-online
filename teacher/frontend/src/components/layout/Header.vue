<template>
  <header class="header">
    <div class="header-left">
      <h1 class="page-title">{{ currentRouteTitle }}</h1>
      <p class="page-subtitle">{{ currentRouteSubtitle }}</p>
    </div>
    <div class="header-right">
      <div class="user-dropdown" @click="toggleDropdown" ref="dropdownRef">
        <div class="user-info">
          <img :src="userAvatar" alt="Teacher" class="user-avatar" />
          <span class="username">{{ authStore.teacherName || '教师' }}</span>
          <Icon icon="solar:alt-arrow-down-linear" class="dropdown-arrow" :class="{ open: dropdownOpen }" />
        </div>
        <teleport to="body">
          <div v-if="dropdownOpen" class="dropdown-menu" :style="getMenuStyle()" @click.stop>
            <div class="dropdown-item" @click="navigateTo('/profile')">
              <Icon icon="solar:user-bold-duotone" class="dropdown-icon" />
              个人信息
            </div>
            <div class="dropdown-item" @click="navigateTo('/change-password')">
              <Icon icon="solar:lock-password-bold-duotone" class="dropdown-icon" />
              修改密码
            </div>
            <div class="dropdown-divider"></div>
            <div class="dropdown-item danger" @click="handleLogout">
              <Icon icon="solar:logout-2-bold-duotone" class="dropdown-icon" />
              退出登录
            </div>
          </div>
        </teleport>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed, ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const dropdownOpen = ref(false)
const dropdownRef = ref(null)

const routeMetaMap = {
  '/dashboard': { title: '仪表盘', subtitle: '查看系统概览和数据统计' },
  '/questions': { title: '题库管理', subtitle: '管理课程题目，支持多种题型与批量导入' },
  '/papers': { title: '试卷管理', subtitle: '手动组卷或自动生成，灵活配置试卷结构' },
  '/exams': { title: '考试管理', subtitle: '创建和管理考试，监控考试状态' },
  '/records': { title: '成绩分析', subtitle: '查看考试成绩分布和学生表现' },
  '/profile': { title: '个人资料', subtitle: '查看和管理您的个人信息' },
  '/change-password': { title: '修改密码', subtitle: '定期更换密码以保障账号安全' }
}

const currentRouteMeta = computed(() => {
  return routeMetaMap[route.path] || { title: '仪表盘', subtitle: '' }
})

const currentRouteTitle = computed(() => currentRouteMeta.value.title)
const currentRouteSubtitle = computed(() => currentRouteMeta.value.subtitle)

const userAvatar = computed(() => {
  return `https://picsum.photos/seed/${authStore.teacherNo || 'teacher1'}/80/80`
})

// 动态计算菜单位置
const getMenuStyle = () => {
  if (!dropdownRef.value || !dropdownOpen.value) return { display: 'none' }

  const rect = dropdownRef.value.getBoundingClientRect()
  return {
    position: 'absolute',
    top: `${rect.bottom + window.scrollY}px`,
    right: `${window.innerWidth - rect.right}px`,
    zIndex: 9999
  }
}

const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value
}

const navigateTo = (path) => {
  dropdownOpen.value = false
  router.push(path)
}

const handleLogout = () => {
  dropdownOpen.value = false
  authStore.logout()
  router.push('/login')
}

// Close dropdown when clicking outside
const handleClickOutside = (event) => {
  // 检查是否点击在下拉菜单内
  const menu = document.querySelector('.dropdown-menu')
  if (menu && menu.contains(event.target)) {
    return
  }

  // 检查是否点击在用户下拉区域
  if (dropdownRef.value && dropdownRef.value.contains(event.target)) {
    return
  }

  dropdownOpen.value = false
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 32px 40px;
  background: transparent;
  position: relative;
  z-index: 1;
}

.header-left {
  .page-title {
    font-size: 32px;
    font-weight: 700;
    letter-spacing: -1px;
    margin-bottom: 8px;
    color: $text-primary;
  }

  .page-subtitle {
    color: $text-secondary;
    font-size: 15px;
  }
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-dropdown {
  position: relative;
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 16px;
  border-radius: 10px;
  background: $bg-tertiary;
  border: 1px solid $border-base;
  transition: all $transition-fast;

  &:hover {
    background: $bg-secondary;
    border-color: $border-dark;
  }

  .user-avatar {
    width: 36px;
    height: 36px;
    border-radius: 10px;
    object-fit: cover;
    border: 2px solid $primary-color;
  }

  .username {
    font-size: 14px;
    font-weight: 500;
    color: $text-primary;
  }

  .dropdown-arrow {
    font-size: 16px;
    color: $text-muted;
    transition: transform $transition-fast;

    &.open {
      transform: rotate(180deg);
    }
  }
}

.dropdown-menu {
  min-width: 180px;
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: 12px;
  padding: 8px;
  box-shadow: $shadow-lg;
  animation: fadeIn 0.2s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  color: $text-secondary;
  cursor: pointer;
  transition: all $transition-fast;

  &:hover {
    background: $bg-tertiary;
    color: $text-primary;
  }

  &.danger:hover {
    background: rgba($danger-color, 0.1);
    color: $danger-color;
  }

  .dropdown-icon {
    font-size: 18px;
    color: $text-muted;
  }

  &:hover .dropdown-icon {
    color: $text-primary;
  }

  &.danger:hover .dropdown-icon {
    color: $danger-color;
  }
}

.dropdown-divider {
  height: 1px;
  background: $border-base;
  margin: 8px 0;
}
</style>