<template>
  <div class="login-container">
    <!-- 噪点纹理背景 -->
    <div class="noise-overlay"></div>

    <!-- 渐变背景 -->
    <div class="gradient-bg"></div>

    <!-- 不对称登录卡片 -->
    <div class="login-card-area">
      <div class="login-card animate-slide-in">
        <!-- Logo区域 -->
        <div class="logo-section">
          <Icon icon="mdi:school" class="logo-icon" />
          <h1 class="logo-text">在线考试系统</h1>
          <p class="logo-subtitle">统一登录入口</p>
        </div>

        <!-- 角色选择 -->
        <div class="role-section">
          <div
            v-for="role in roleOptions"
            :key="role.value"
            class="role-item"
            :class="{ active: selectedRole === role.value }"
            @click="selectedRole = role.value"
          >
            <Icon :icon="role.icon" class="role-icon" />
            <span class="role-label">{{ role.label }}</span>
          </div>
        </div>

        <!-- 登录表单 -->
        <div class="form-section">
          <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            @submit.prevent="handleLogin"
          >
            <el-form-item prop="username">
              <div class="input-wrapper">
                <Icon icon="mdi:account" class="input-icon" />
                <el-input
                  v-model="loginForm.username"
                  :placeholder="usernamePlaceholder"
                  size="large"
                  class="custom-input"
                />
              </div>
            </el-form-item>

            <el-form-item prop="password">
              <div class="input-wrapper">
                <Icon icon="mdi:lock" class="input-icon" />
                <el-input
                  v-model="loginForm.password"
                  type="password"
                  placeholder="请输入密码"
                  size="large"
                  class="custom-input"
                  show-password
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
                <Icon icon="mdi:login" class="btn-icon" />
                <span>{{ loading ? '登录中...' : '登录系统' }}</span>
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 底部提示 -->
        <div class="footer-section">
          <p class="footer-text">
            <Icon icon="mdi:shield-check" class="shield-icon" />
            系统已启用安全认证保护
          </p>
        </div>
      </div>

      <!-- 右侧装饰区域 -->
      <div class="decoration-area animate-fade-in-delayed">
        <div class="decoration-circle circle-1"></div>
        <div class="decoration-circle circle-2"></div>
        <div class="decoration-circle circle-3"></div>
        <div class="stats-preview">
          <div class="stat-item">
            <Icon icon="mdi:account-group" class="stat-icon" />
            <span class="stat-value">1,280+</span>
            <span class="stat-label">活跃学生</span>
          </div>
          <div class="stat-item">
            <Icon icon="mdi:file-document" class="stat-icon" />
            <span class="stat-value">85+</span>
            <span class="stat-label">教师团队</span>
          </div>
          <div class="stat-item">
            <Icon icon="mdi:chart-line" class="stat-icon" />
            <span class="stat-value">99.9%</span>
            <span class="stat-label">系统稳定</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import { login as adminLogin, teacherLogin, studentLogin } from '@/api/auth'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(false)
const loginFormRef = ref(null)
const selectedRole = ref('admin')

const roleOptions = [
  { value: 'admin', label: '管理员', icon: 'mdi:shield-account' },
  { value: 'teacher', label: '教师', icon: 'mdi:account-tie' },
  { value: 'student', label: '学生', icon: 'mdi:school' }
]

const usernamePlaceholder = computed(() => {
  switch (selectedRole.value) {
    case 'teacher': return '请输入工号'
    case 'student': return '请输入学号'
    default: return '请输入用户名'
  }
})

const loginForm = reactive({
  username: '',
  password: ''
})

const getUsernameFieldName = () => {
  switch (selectedRole.value) {
    case 'teacher': return 'teacherNo'
    case 'student': return 'studentNo'
    default: return 'username'
  }
}

const loginRules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度为3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
  ]
}

const getRedirectUrl = () => {
  switch (selectedRole.value) {
    case 'teacher':
      return 'http://localhost:3001/'
    case 'student':
      return 'http://localhost:3000/exams'
    default:
      return '/dashboard'
  }
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  console.log('登录表单值:', loginForm)
  console.log('选择的角色:', selectedRole.value)

  await loginFormRef.value.validate(async (valid) => {
    console.log('表单验证结果:', valid)
    if (!valid) return

    loading.value = true
    try {
      let result
      let token, user
      const fieldData = {}

      console.log('开始登录，角色:', selectedRole.value)

      switch (selectedRole.value) {
        case 'admin':
          fieldData.username = loginForm.username
          fieldData.password = loginForm.password
          console.log('管理员登录数据:', fieldData)
          result = await adminLogin(fieldData)
          token = result.data?.token || result.token
          user = result.data?.admin || result.admin
          break

        case 'teacher':
          fieldData.teacherNo = loginForm.username
          fieldData.password = loginForm.password
          console.log('教师登录数据:', fieldData)
          result = await teacherLogin(fieldData)
          token = result.data?.token || result.token
          user = result.data?.teacher || result.teacher
          console.log('教师登录响应:', result)
          break

        case 'student':
          fieldData.studentNo = loginForm.username
          fieldData.password = loginForm.password
          console.log('学生登录数据:', fieldData)
          result = await studentLogin(fieldData)
          token = result.data?.token || result.token
          user = result.data?.student || result.student
          break
      }

      console.log('登录结果:', { token, user })

      if (token) {
        ElMessage.success('登录成功')
        localStorage.setItem('role', selectedRole.value)
        if (selectedRole.value === 'admin') {
          authStore.setSession(token, user)
          router.push('/dashboard')
        } else {
          // 教师/学生登录成功后跳转到对应端，只传token
          const redirectUrl = getRedirectUrl()
          window.location.href = `${redirectUrl}?token=${token}`
        }
      } else {
        ElMessage.error('登录失败，请检查用户名和密码')
      }
    } catch (error) {
      console.error('登录错误:', error)
      ElMessage.error(error.message || error.response?.data?.message || '登录失败，请稍后重试')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.login-container {
  width: 100vw;
  height: 100vh;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
}

// 渐变背景
.gradient-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    135deg,
    rgba($primary-color, 0.05) 0%,
    rgba($primary-color, 0.08) 50%,
    rgba(#60A5FA, 0.03) 100%
  );
  z-index: 0;
}

// 噪点纹理
.noise-overlay {
  position: absolute;
  inset: 0;
  opacity: 0.03;
  z-index: 1;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
}

// 登录卡片区域 - 不对称布局
.login-card-area {
  position: relative;
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
  padding-left: 15%;
  gap: 80px;
}

.login-card {
  width: 420px;
  background: $bg-card;
  border-radius: $radius-xl;
  padding: $spacing-xl;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1);
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, $primary-color, $primary-color);
    border-radius: $radius-xl $radius-xl 0 0;
  }
}

// Logo区域
.logo-section {
  text-align: center;
  margin-bottom: $spacing-xl;
}

.logo-icon {
  font-size: 48px;
  color: $primary-color;
  margin-bottom: $spacing-md;
}

.logo-text {
  font-size: $font-size-2xl;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: $spacing-xs;
}

.logo-subtitle {
  font-size: $font-size-sm;
  color: #6A6A6A;
}

// 角色选择区域
.role-section {
  display: flex;
  justify-content: center;
  gap: $spacing-md;
  margin-bottom: $spacing-lg;
  padding: 0 $spacing-md;
}

.role-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: $spacing-xs;
  padding: $spacing-md $spacing-lg;
  border-radius: $radius-lg;
  border: 2px solid $border-base;
  cursor: pointer;
  transition: all $transition-normal;
  background: $bg-primary;

  &:hover {
    border-color: rgba($primary-color, 0.5);
  }

  &.active {
    border-color: $primary-color;
    background: rgba($primary-color, 0.05);
  }
}

.role-icon {
  font-size: 24px;
  color: $text-secondary;
  transition: color $transition-normal;

  .role-item.active & {
    color: $primary-color;
  }
}

.role-label {
  font-size: $font-size-sm;
  color: $text-secondary;
  font-weight: $font-weight-medium;
  transition: color $transition-normal;

  .role-item.active & {
    color: $primary-color;
  }
}

// 表单区域
.form-section {
  margin-bottom: $spacing-lg;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: $spacing-md;
  font-size: 20px;
  color: #6A6A6A;
  z-index: 1;
}

.custom-input {
  :deep(.el-input__wrapper) {
    padding-left: 44px;
    border-radius: $radius-md;
    background: #E5E5E5;
    border: 1px solid transparent;
    box-shadow: none;
    transition: all $ease-smooth 0.2s;

    &:hover, &:focus-within {
      border-color: $primary-color;
      background: #FFFFFF;
    }
  }

  :deep(.el-input__inner) {
    color: #1E293B;
    font-size: $font-size-md;

    &::placeholder {
      color: #6A6A6A-light;
    }
  }
}

// 登录按钮
.login-btn {
  width: 100%;
  height: 48px;
  border-radius: $radius-md;
  background: linear-gradient(135deg, $primary-color 0%, #2563EB 100%);
  border: none;
  font-size: $font-size-md;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: $spacing-sm;
  transition: all $ease-spring 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba($primary-color, 0.3);
  }

  &:active {
    transform: translateY(0);
  }

  .btn-icon {
    font-size: 20px;
  }
}

// 底部提示
.footer-section {
  text-align: center;
}

.footer-text {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: $spacing-sm;
  font-size: $font-size-xs;
  color: #6A6A6A;
}

.shield-icon {
  font-size: 16px;
  color: #10B981;
}

// 右侧装饰区域
.decoration-area {
  position: relative;
  flex: 1;
  max-width: 500px;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.6;

  &.circle-1 {
    width: 200px;
    height: 200px;
    background: linear-gradient(135deg, rgba($primary-color, 0.2), rgba($primary-color, 0.1));
    top: -50px;
    right: 100px;
  }

  &.circle-2 {
    width: 120px;
    height: 120px;
    background: linear-gradient(135deg, rgba($primary-color, 0.3), rgba(#60A5FA, 0.15));
    bottom: 100px;
    right: 200px;
  }

  &.circle-3 {
    width: 80px;
    height: 80px;
    background: rgba($primary-color, 0.25);
    top: 150px;
    right: 50px;
  }
}

.stats-preview {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
  padding: $spacing-xl;
  background: rgba(#FFFFFF, 0.8);
  border-radius: $radius-lg;
  backdrop-filter: blur(10px);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.stat-icon {
  font-size: 28px;
  color: $primary-color;
}

.stat-value {
  font-size: $font-size-xl;
  font-weight: 600;
  color: #1E293B;
}

.stat-label {
  font-size: $font-size-sm;
  color: #6A6A6A;
}

// 动画
.animate-slide-in {
  animation: slideIn 0.6s $ease-spring forwards;
}

.animate-fade-in-delayed {
  animation: fadeInDelayed 0.8s $ease-smooth forwards;
  animation-delay: 0.2s;
  opacity: 0;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInDelayed {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

// 响应式
@media (max-width: 1200px) {
  .login-card-area {
    padding-left: 10%;
    gap: 40px;
  }

  .decoration-area {
    max-width: 300px;
  }

  .decoration-circle {
    display: none;
  }
}

@media (max-width: 768px) {
  .login-card-area {
    justify-content: center;
    padding-left: 0;
    flex-direction: column;
  }

  .login-card {
    width: 90%;
    max-width: 400px;
  }

  .decoration-area {
    display: none;
  }
}
</style>