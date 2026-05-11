<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-box">
        <!-- Login header -->
        <div class="login-header">
          <div class="logo">
            <div class="logo-icon">
              <Icon icon="solar:graduation-cap-bold-duotone" class="logo-icon-svg" />
            </div>
            <div class="logo-text">教师端登录</div>
          </div>
          <h1 class="title">欢迎回来</h1>
          <p class="subtitle">登录到在线考试系统教师端</p>
        </div>

        <!-- Login form -->
        <form class="login-form" @submit.prevent="handleLogin">
          <div class="form-group">
            <label class="form-label">
              <Icon icon="solar:user-bold-duotone" class="input-icon" />
              工号
            </label>
            <input
              v-model="loginForm.teacherNo"
              type="text"
              class="input"
              placeholder="请输入工号"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <Icon icon="solar:lock-password-bold-duotone" class="input-icon" />
              密码
            </label>
            <div class="password-input-wrapper">
              <input
                v-model="loginForm.password"
                :type="showPassword ? 'text' : 'password'"
                class="input"
                placeholder="请输入密码"
                required
                @keyup.enter="handleLogin"
              />
              <button
                type="button"
                class="password-toggle"
                @click="showPassword = !showPassword"
              >
                <Icon
                  :icon="showPassword ? 'solar:eye-closed-bold-duotone' : 'solar:eye-bold-duotone'"
                  class="toggle-icon"
                />
              </button>
            </div>
          </div>

          <div v-if="errorMsg" class="error-message">
            <Icon icon="solar:danger-triangle-bold-duotone" class="error-icon" />
            {{ errorMsg }}
          </div>

          <button type="submit" class="login-btn" :disabled="loading">
            <Icon v-if="loading" icon="solar:loader-2-bold-duotone" class="loading-icon" />
            <Icon v-else icon="solar:login-2-bold-duotone" class="btn-icon" />
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>

        <!-- Login footer -->
        <div class="login-footer">
          <p class="copyright">在线考试系统 · 教师端</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const showPassword = ref(false)
const errorMsg = ref('')

const loginForm = reactive({
  teacherNo: '',
  password: ''
})

const handleLogin = async () => {
  errorMsg.value = ''
  loading.value = true

  try {
    const success = await authStore.login(loginForm.teacherNo, loginForm.password)
    if (success) {
      router.push('/dashboard')
    } else {
      errorMsg.value = '登录失败，请检查工号和密码'
    }
  } catch (err) {
    errorMsg.value = err.message || '登录失败，请稍后重试'
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
@use '@/styles/variables.scss' as *;

.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: $bg-primary;
  position: relative;
  overflow: hidden;
}

/* Glow orbs - decorative effect */
.glow-orb {
  position: fixed;
  border-radius: 50%;
  filter: blur(120px);
  opacity: 0.12;
  pointer-events: none;
  z-index: 0;
}

.glow-orb-1 {
  width: 500px;
  height: 500px;
  background: $accent-mint;
  top: -150px;
  left: 20%;
  animation: float 20s infinite ease-in-out;
}

.glow-orb-2 {
  width: 400px;
  height: 400px;
  background: $accent-amber;
  bottom: -100px;
  right: 15%;
  animation: float 25s infinite ease-in-out reverse;
}

.glow-orb-3 {
  width: 300px;
  height: 300px;
  background: $accent-coral;
  top: 40%;
  left: -100px;
  animation: float 15s infinite ease-in-out;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(30px, -30px); }
}

.login-wrapper {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 440px;
  padding: 20px;
}

.login-box {
  background: $bg-card;
  border: 1px solid $border-base;
  border-radius: 24px;
  padding: 48px 40px;
}

/* Login header */
.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 32px;
}

.logo-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, $accent-mint 0%, #059669 100%);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 22px;
  color: $bg-primary;
}

.logo-text {
  font-size: 24px;
  font-weight: 700;
  color: $text-primary;
  letter-spacing: -0.5px;

  span {
    color: $accent-mint;
  }
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: $text-primary;
  margin-bottom: 8px;
  letter-spacing: -0.5px;
}

.subtitle {
  font-size: 15px;
  color: $text-muted;
}

/* Login form */
.login-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
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
  color: $text-secondary;

  .input-icon {
    font-size: 16px;
    color: $text-muted;
  }
}

.input {
  width: 100%;
  padding: 14px 20px;
  background: $bg-tertiary;
  border: 1px solid $border-base;
  border-radius: 12px;
  font-size: 15px;
  color: $text-primary;
  font-family: inherit;
  transition: all $transition-normal;

  &::placeholder {
    color: $text-muted;
  }

  &:focus {
    outline: none;
    border-color: $accent-mint;
    box-shadow: 0 0 0 3px rgba($accent-mint, 0.1);
  }
}

.password-input-wrapper {
  position: relative;

  .input {
    padding-right: 48px;
  }

  .password-toggle {
    position: absolute;
    right: 16px;
    top: 50%;
    transform: translateY(-50%);
    background: none;
    border: none;
    padding: 4px;
    cursor: pointer;
    color: $text-muted;
    transition: color $transition-fast;

    &:hover {
      color: $text-secondary;
    }

    .toggle-icon {
      font-size: 20px;
    }
  }
}

.error-message {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: rgba($danger-color, 0.08);
  border-radius: 10px;
  font-size: 14px;
  color: $danger-color;

  .error-icon {
    font-size: 18px;
  }
}

.login-btn {
  width: 100%;
  padding: 16px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, $accent-mint 0%, #059669 100%);
  color: $bg-primary;
  border: none;
  cursor: pointer;
  transition: all $transition-normal;
  font-family: inherit;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;

  &:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba($accent-mint, 0.3);
  }

  &:disabled {
    opacity: 0.7;
    cursor: not-allowed;
  }

  .loading-icon {
    animation: spin 1s linear infinite;
  }

  .btn-icon {
    font-size: 20px;
  }
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Login footer */
.login-footer {
  text-align: center;
  margin-top: 32px;

  .copyright {
    font-size: 13px;
    color: $text-muted;
  }
}
</style>