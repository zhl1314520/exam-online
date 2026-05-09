<template>
  <div class="login-page warm-gradient">
    <div class="login-container">
      <div class="login-card card noise-bg">
        <div class="login-header">
          <div class="logo">
            <Icon icon="lucide:graduation-cap" class="logo-icon" />
          </div>
          <h1 class="login-title">在线考试系统</h1>
          <p class="login-subtitle">学生端登录</p>
        </div>

        <form class="login-form" @submit.prevent="handleLogin">
          <div class="input-group">
            <label class="input-label">
              <Icon icon="lucide:user" class="input-icon" />
              学号
            </label>
            <input
              v-model="form.username"
              type="text"
              class="input"
              placeholder="请输入学号"
              :disabled="loading"
            />
          </div>

          <div class="input-group">
            <label class="input-label">
              <Icon icon="lucide:lock" class="input-icon" />
              密码
            </label>
            <input
              v-model="form.password"
              type="password"
              class="input"
              placeholder="请输入密码"
              :disabled="loading"
            />
          </div>

          <div v-if="error" class="error-message">
            <Icon icon="lucide:alert-circle" />
            {{ error }}
          </div>

          <button type="submit" class="btn btn-primary btn-lg login-btn" :disabled="loading">
            <Icon v-if="loading" icon="lucide:loader-2" class="loading" />
            <Icon v-else icon="lucide:log-in" />
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>

        <div class="login-footer">
          <p>© 2024 在线考试系统</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/stores/auth.js'

const router = useRouter()
const authStore = useAuthStore()

const form = ref({
  username: '',
  password: ''
})

const loading = ref(false)
const error = ref('')

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    error.value = '请输入学号和密码'
    return
  }

  loading.value = true
  error.value = ''

  try {
    await authStore.login(form.value)
    router.push('/exams')
  } catch (err) {
    error.value = err.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 420px;
}

.login-card {
  padding: 40px 36px;
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 72px;
  height: 72px;
  background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
  border-radius: var(--radius-xl);
  margin-bottom: 20px;
  box-shadow: var(--shadow-md);
}

.logo-icon {
  width: 36px;
  height: 36px;
  color: white;
}

.login-title {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 8px;
  letter-spacing: -0.5px;
}

.login-subtitle {
  font-size: 15px;
  color: var(--text-secondary);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.input-group {
  margin-bottom: 4px;
}

.input-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.input-icon {
  width: 16px;
  height: 16px;
  color: var(--text-muted);
}

.login-btn {
  margin-top: 8px;
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

.login-footer {
  margin-top: 32px;
  text-align: center;
  font-size: 13px;
  color: var(--text-muted);
}

.loading {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
