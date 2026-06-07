<template>
  <div class="profile-page">
    <header class="page-header">
      <h1 class="page-title">个人信息</h1>
      <p class="page-desc">管理您的账户信息</p>
    </header>

    <div class="profile-content">
      <!-- 用户信息卡片 -->
      <div class="user-card">
        <div class="user-avatar">
          <Icon icon="solar:user-bold" class="avatar-icon" />
        </div>
        <div class="user-info">
          <h2 class="user-name">{{ authStore.user?.realName }}</h2>
          <p class="user-id">学号: {{ authStore.user?.studentNo }}</p>
          <p class="user-class">{{ authStore.user?.className }}</p>
        </div>
      </div>

      <!-- 基本信息 -->
      <div class="info-section">
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
      <div class="password-section">
        <div class="section-header">
          <h3>修改密码</h3>
        </div>
        <el-form
          ref="passwordFormRef"
          :model="passwordForm"
          :rules="passwordRules"
          class="password-form"
          @submit.prevent="handlePasswordChange"
        >
          <el-form-item prop="oldPassword">
            <div class="input-wrapper">
              <Icon icon="solar:lock-bold" class="input-icon" />
              <el-input
                v-model="passwordForm.oldPassword"
                type="password"
                placeholder="请输入旧密码"
                size="large"
                class="custom-input"
                show-password
              />
            </div>
          </el-form-item>

          <el-form-item prop="newPassword">
            <div class="input-wrapper">
              <Icon icon="solar:key-bold" class="input-icon" />
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码"
                size="large"
                class="custom-input"
                show-password
              />
            </div>
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <div class="input-wrapper">
              <Icon icon="solar:check-circle-bold" class="input-icon" />
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
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
              class="submit-btn"
              :loading="passwordLoading"
              @click="handlePasswordChange"
            >
              <Icon icon="solar:disk-bold" />
              {{ passwordLoading ? '修改中...' : '修改密码' }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/stores/auth.js'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()

const passwordFormRef = ref(null)

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的新密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const passwordLoading = ref(false)

const handlePasswordChange = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (!valid) return

    passwordLoading.value = true
    try {
      await authStore.updatePassword({
        oldPassword: passwordForm.oldPassword,
        newPassword: passwordForm.newPassword
      })
      ElMessage.success('密码修改成功')
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    } catch (err) {
      ElMessage.error(err.response?.data?.message || '密码修改失败')
    } finally {
      passwordLoading.value = false
    }
  })
}

onMounted(async () => {
  if (!authStore.user?.realName) {
    await authStore.fetchUserInfo()
  }
})
</script>

<style scoped>
.page-header {
  margin-bottom: 32px;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.page-desc {
  font-size: 14px;
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
  padding: 24px;
  background: var(--bg-card);
  border-radius: 12px;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.user-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, var(--accent-sky), var(--accent-mint));
  border-radius: 50%;
}

.avatar-icon {
  font-size: 40px;
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
  color: var(--accent-sky);
  font-weight: 500;
}

.info-section,
.password-section {
  background: var(--bg-card);
  border-radius: 12px;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
  overflow: hidden;
}

.section-header {
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
  background: var(--bg-tertiary);
}

.section-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 16px;
  padding: 24px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 10px;
}

.info-label {
  font-size: 12px;
  color: var(--text-secondary);
}

.info-value {
  font-size: 15px;
  font-weight: 500;
  color: var(--text-primary);
}

.password-form {
  padding: 24px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 14px;
  font-size: 20px;
  color: var(--text-muted);
  z-index: 1;
}

.custom-input {
  width: 100%;
}

.custom-input :deep(.el-input__wrapper) {
  padding-left: 44px;
  border-radius: 10px;
  background: var(--bg-tertiary);
  border: 1px solid var(--border);
  box-shadow: none;
  transition: all 0.2s;
}

.custom-input :deep(.el-input__wrapper:hover),
.custom-input :deep(.el-input__wrapper:focus-within) {
  border-color: var(--accent-sky);
  background: var(--bg-card);
}

.custom-input :deep(.el-input__inner) {
  color: var(--text-primary);
  font-size: 15px;
}

.custom-input :deep(.el-input__inner::placeholder) {
  color: var(--text-muted);
}

.submit-btn {
  width: 100%;
  background: linear-gradient(135deg, var(--accent-sky), var(--accent-mint)) !important;
  border: none !important;
}

.submit-btn:hover {
  opacity: 0.9;
}
</style>
