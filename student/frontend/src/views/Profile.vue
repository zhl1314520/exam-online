<template>
  <div class="profile-page fade-in">
    <header class="page-header">
      <h1 class="page-title">个人信息</h1>
      <p class="page-desc">管理您的账户信息</p>
    </header>

    <div class="profile-content">
      <!-- 用户信息卡片 -->
      <div class="user-card card noise-bg">
        <div class="user-avatar">
          <Icon icon="lucide:user" class="avatar-icon" />
        </div>
        <div class="user-info">
          <h2 class="user-name">{{ authStore.user?.realName }}</h2>
          <p class="user-id">学号: {{ authStore.user?.studentNo }}</p>
          <p class="user-class">{{ authStore.user?.className }}</p>
        </div>
      </div>

      <!-- 基本信息 -->
      <div class="info-section card">
        <div class="section-header">
          <h3>基本信息</h3>
        </div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">学号</span>
            <span class="info-value">{{ authStore.user?.studentNo }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">姓名</span>
            <span class="info-value">{{ authStore.user?.realName }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">班级</span>
            <span class="info-value">{{ authStore.user?.className || '未设置' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">性别</span>
            <span class="info-value">{{ authStore.user?.gender === 1 ? '男' : authStore.user?.gender === 2 ? '女' : '未设置' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">邮箱</span>
            <span class="info-value">{{ authStore.user?.email || '未设置' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">电话</span>
            <span class="info-value">{{ authStore.user?.phone || '未设置' }}</span>
          </div>
        </div>
      </div>

      <!-- 修改密码 -->
      <div class="password-section card">
        <div class="section-header">
          <h3>修改密码</h3>
        </div>
        <form class="password-form" @submit.prevent="handlePasswordChange">
          <div class="form-group">
            <label class="form-label">
              <Icon icon="lucide:lock" />
              旧密码
            </label>
            <input
              v-model="passwordForm.oldPassword"
              type="password"
              class="input"
              placeholder="请输入旧密码"
              required
            />
          </div>
          <div class="form-group">
            <label class="form-label">
              <Icon icon="lucide:key" />
              新密码
            </label>
            <input
              v-model="passwordForm.newPassword"
              type="password"
              class="input"
              placeholder="请输入新密码"
              required
            />
          </div>
          <div class="form-group">
            <label class="form-label">
              <Icon icon="lucide:check" />
              确认新密码
            </label>
            <input
              v-model="passwordForm.confirmPassword"
              type="password"
              class="input"
              placeholder="请再次输入新密码"
              required
            />
          </div>
          <div v-if="passwordError" class="error-message">
            <Icon icon="lucide:alert-circle" />
            {{ passwordError }}
          </div>
          <div v-if="passwordSuccess" class="success-message">
            <Icon icon="lucide:check-circle" />
            {{ passwordSuccess }}
          </div>
          <button type="submit" class="btn btn-primary" :disabled="passwordLoading">
            <Icon v-if="passwordLoading" icon="lucide:loader-2" class="loading" />
            <Icon v-else icon="lucide:save" />
            {{ passwordLoading ? '修改中...' : '修改密码' }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/stores/auth.js'

const router = useRouter()
const authStore = useAuthStore()

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordError = ref('')
const passwordSuccess = ref('')
const passwordLoading = ref(false)

const handlePasswordChange = async () => {
  passwordError.value = ''
  passwordSuccess.value = ''

  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    passwordError.value = '两次输入的新密码不一致'
    return
  }

  if (passwordForm.value.newPassword.length < 6) {
    passwordError.value = '新密码长度不能少于6位'
    return
  }

  passwordLoading.value = true
  try {
    await authStore.updatePassword({
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword
    })
    passwordSuccess.value = '密码修改成功'
    passwordForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
  } catch (err) {
    passwordError.value = err.response?.data?.message || '密码修改失败'
  } finally {
    passwordLoading.value = false
  }
}

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

onMounted(async () => {
  if (!authStore.user?.realName) {
    await authStore.fetchUserInfo()
  }
})
</script>

<style scoped>
.page-header {
  margin-bottom: 28px;
}

.page-title {
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 6px;
}

.page-desc {
  font-size: 15px;
  color: var(--text-secondary);
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.user-card {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 28px;
  background: linear-gradient(135deg, var(--bg-card) 0%, var(--bg-secondary) 100%);
}

.user-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
  border-radius: 50%;
}

.avatar-icon {
  width: 40px;
  height: 40px;
  color: white;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-name {
  font-size: 22px;
  font-weight: 600;
  color: var(--text-primary);
}

.user-id {
  font-size: 14px;
  color: var(--text-secondary);
}

.user-class {
  font-size: 14px;
  color: var(--primary-color);
  font-weight: 500;
}

.info-section {
  overflow: hidden;
}

.section-header {
  padding: 20px 24px;
  border-bottom: 1px solid var(--border-color);
  background: linear-gradient(90deg, var(--bg-secondary) 0%, transparent 100%);
}

.section-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  padding: 24px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 16px;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
}

.info-label {
  font-size: 13px;
  color: var(--text-muted);
}

.info-value {
  font-size: 15px;
  font-weight: 500;
  color: var(--text-primary);
}

.password-section {
  overflow: hidden;
}

.password-form {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
}

.error-message {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: rgba(209, 90, 90, 0.08);
  color: var(--error-color);
  border-radius: var(--radius-md);
  font-size: 14px;
}

.success-message {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: rgba(61, 154, 90, 0.08);
  color: var(--success-color);
  border-radius: var(--radius-md);
  font-size: 14px;
}

.loading {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
