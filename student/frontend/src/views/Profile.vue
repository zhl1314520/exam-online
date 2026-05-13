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
          <Icon icon="mdi:account" class="avatar-icon" />
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
              <Icon icon="mdi:lock" class="input-icon" />
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
              <Icon icon="mdi:key" class="input-icon" />
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
              <Icon icon="mdi:check-circle" class="input-icon" />
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
              <Icon icon="mdi:content-save" />
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

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.page-header {
  margin-bottom: $spacing-xl;
}

.page-title {
  font-size: $font-size-2xl;
  font-weight: 600;
  color: $dark;
  margin-bottom: $spacing-xs;
}

.page-desc {
  font-size: $font-size-md;
  color: $gray;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: $spacing-xl;
}

.user-card {
  display: flex;
  align-items: center;
  gap: $spacing-xl;
  padding: $spacing-xl;
  background: $bg-card;
  border-radius: $radius-lg;
  box-shadow: $shadow-sm;
  border: 1px solid $border-color;
}

.user-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, $accent, $accent-dark);
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
  color: $dark;
}

.user-id {
  font-size: $font-size-sm;
  color: $gray;
}

.user-class {
  font-size: $font-size-sm;
  color: $accent;
  font-weight: 500;
}

.info-section,
.password-section {
  background: $bg-card;
  border-radius: $radius-lg;
  box-shadow: $shadow-sm;
  border: 1px solid $border-color;
  overflow: hidden;
}

.section-header {
  padding: $spacing-lg;
  border-bottom: 1px solid $border-color;
  background: linear-gradient(90deg, $light 0%, transparent 100%);

  h3 {
    font-size: $font-size-md;
    font-weight: 600;
    color: $dark;
  }
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: $spacing-lg;
  padding: $spacing-xl;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: $spacing-md;
  background: $light;
  border-radius: $radius-md;
}

.info-label {
  font-size: $font-size-xs;
  color: $gray;
}

.info-value {
  font-size: $font-size-md;
  font-weight: 500;
  color: $dark;
}

.password-form {
  padding: $spacing-xl;
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
  color: $gray;
  z-index: 1;
}

.custom-input {
  :deep(.el-input__wrapper) {
    padding-left: 44px;
    border-radius: $radius-md;
    background: $light;
    border: 1px solid transparent;
    box-shadow: none;
    transition: all $ease-smooth 0.2s;

    &:hover, &:focus-within {
      border-color: $accent;
      background: $white;
    }
  }

  :deep(.el-input__inner) {
    color: $dark;
    font-size: $font-size-md;

    &::placeholder {
      color: $gray-light;
    }
  }
}

.submit-btn {
  width: 100%;
}
</style>