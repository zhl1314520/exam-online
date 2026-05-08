<template>
  <header class="header">
    <div class="header-left">
      <h1 class="page-title">{{ currentRouteTitle }}</h1>
      <p class="page-subtitle">{{ currentRouteSubtitle }}</p>
    </div>
    <div class="header-right">
      <el-dropdown @command="handleCommand" class="user-dropdown">
        <span class="user-info">
          <el-avatar :size="36" :icon="UserFilled" class="user-avatar" />
          <span class="username">{{ authStore.teacherName }}</span>
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人信息
            </el-dropdown-item>
            <el-dropdown-item command="password">
              <el-icon><Lock /></el-icon>
              修改密码
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UserFilled, ArrowDown, User, Lock, SwitchButton } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const routeMetaMap = {
  '/dashboard': { title: '仪表盘', subtitle: '查看系统概览和数据统计' },
  '/questions': { title: '题库管理', subtitle: '管理课程题目，支持多种题型与批量导入' },
  '/papers': { title: '试卷管理', subtitle: '手动组卷或自动生成，灵活配置试卷结构' },
  '/exams': { title: '考试管理', subtitle: '创建和管理考试，监控考试状态' },
  '/records': { title: '成绩分析', subtitle: '查看考试成绩分布和学生表现' }
}

const currentRouteMeta = computed(() => {
  return routeMetaMap[route.path] || { title: '仪表盘', subtitle: '' }
})

const currentRouteTitle = computed(() => currentRouteMeta.value.title)
const currentRouteSubtitle = computed(() => currentRouteMeta.value.subtitle)

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'password':
      router.push('/change-password')
      break
    case 'logout':
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        authStore.logout()
        ElMessage.success('已退出登录')
      })
      break
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 32px 40px;
  background: transparent;
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
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 16px;
  border-radius: 10px;
  transition: all $transition-fast;

  &:hover {
    background: $bg-tertiary;
  }

  .user-avatar {
    border-radius: 10px;
    border: 2px solid $border-dark;
  }

  .username {
    font-size: 14px;
    font-weight: 500;
    color: $text-primary;
  }
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
