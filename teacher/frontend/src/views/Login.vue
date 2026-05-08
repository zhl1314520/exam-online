<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="decoration-orb orb-1"></div>
      <div class="decoration-orb orb-2"></div>
      <div class="decoration-orb orb-3"></div>
    </div>

    <div class="login-wrapper">
      <div class="login-box">
        <!-- 登录头部 -->
        <div class="login-header">
          <div class="logo">
            <div class="logo-icon">E</div>
            <div class="logo-text">Exam<span>Flow</span></div>
          </div>
          <h1 class="title">欢迎回来</h1>
          <p class="subtitle">登录到在线考试系统教师端</p>
        </div>

        <!-- 登录表单 -->
        <el-form
          ref="formRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
        >
          <el-form-item prop="teacherNo">
            <div class="input-wrapper">
              <el-icon class="input-icon"><User /></el-icon>
              <el-input
                v-model="loginForm.teacherNo"
                placeholder="请输入工号"
                size="large"
              />
            </div>
          </el-form-item>
          <el-form-item prop="password">
            <div class="input-wrapper">
              <el-icon class="input-icon"><Lock /></el-icon>
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                show-password
                @keyup.enter="handleLogin"
              />
            </div>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="login-btn"
              :loading="loading"
              @click="handleLogin"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 登录底部 -->
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
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const formRef = ref(null)

const loginForm = reactive({
  teacherNo: '',
  password: ''
})

const loginRules = {
  teacherNo: [{ required: true, message: '请输入工号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const success = await authStore.login(loginForm.teacherNo, loginForm.password)
        if (success) {
          ElMessage.success('登录成功')
          router.push('/dashboard')
        } else {
          ElMessage.error('登录失败，请检查工号和密码')
        }
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.decoration-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.4;
}

.orb-1 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #3b82f6 0%, #0ea5e9 100%);
  top: -100px;
  left: -100px;
  animation: float 20s infinite ease-in-out;
}

.orb-2 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  bottom: -50px;
  right: -50px;
  animation: float 25s infinite ease-in-out reverse;
}

.orb-3 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  top: 50%;
  right: 20%;
  animation: float 15s infinite ease-in-out;
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(30px, -30px);
  }
}

.login-wrapper {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 440px;
  padding: 20px;
}

.login-box {
  background: #ffffff;
  border-radius: 24px;
  padding: 48px 40px;
  box-shadow: 0 20px 40px -12px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.8);
}

/* 登录头部 */
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
  background: linear-gradient(135deg, $success-color 0%, darken($success-color, 10%) 100%);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 22px;
  color: white;
}

.logo-text {
  font-size: 24px;
  font-weight: 700;
  color: $text-primary;
  letter-spacing: -0.5px;

  span {
    color: $success-color;
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

/* 登录表单 */
.login-form {
  :deep(.el-form-item) {
    margin-bottom: 24px;
  }
}

.input-wrapper {
  position: relative;
  width: 100%;

  .input-icon {
    position: absolute;
    left: 16px;
    top: 50%;
    transform: translateY(-50%);
    color: $text-muted;
    z-index: 1;
    font-size: 18px;
  }

  :deep(.el-input__wrapper) {
    padding: 8px 16px 8px 48px;
    border-radius: 12px;
    box-shadow: 0 0 0 1px $border-base inset;
    background: $bg-secondary;

    &:hover {
      box-shadow: 0 0 0 1px $border-dark inset;
    }

    &.is-focus {
      box-shadow: 0 0 0 1px $primary-color inset, 0 0 0 3px rgba($primary-color, 0.1);
    }
  }

  :deep(.el-input__inner) {
    font-size: 15px;
  }
}

.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  margin-top: 8px;
  background: linear-gradient(135deg, $primary-color 0%, darken($primary-color, 10%) 100%);
  border: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba($primary-color, 0.35);
  }

  &:active {
    transform: translateY(0);
  }
}

/* 登录底部 */
.login-footer {
  text-align: center;
  margin-top: 32px;

  .copyright {
    font-size: 13px;
    color: $text-muted;
  }
}
</style>
