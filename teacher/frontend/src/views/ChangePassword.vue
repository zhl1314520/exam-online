<template>
  <div class="change-password-page">
    <el-card class="password-card">
      <template #header>
        <div class="card-header">
          <h2>修改密码</h2>
          <p class="subtitle">请妥善保管您的新密码</p>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        class="password-form"
      >
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input
            v-model="form.oldPassword"
            type="password"
            show-password
            placeholder="请输入旧密码"
            size="large"
          />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="form.newPassword"
            type="password"
            show-password
            placeholder="请输入新密码"
            size="large"
          />
        </el-form-item>

        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            show-password
            placeholder="请再次输入新密码"
            size="large"
          />
        </el-form-item>

        <el-form-item>
          <div class="form-actions">
            <el-button type="primary" @click="handleSubmit" :loading="loading" size="large">
              确认修改
            </el-button>
            <el-button @click="handleReset" size="large">重置</el-button>
          </div>
        </el-form-item>
      </el-form>

      <div class="password-tips">
        <h4>密码安全提示：</h4>
        <ul>
          <li>密码长度至少为 6 位</li>
          <li>建议包含字母、数字和特殊字符</li>
          <li>请勿使用与其他网站相同的密码</li>
          <li>定期更换密码可提高账号安全性</li>
        </ul>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { changePassword } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 验证确认密码
const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'))
  } else if (value !== form.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await changePassword({
          oldPassword: form.oldPassword,
          newPassword: form.newPassword
        })
        ElMessage.success('密码修改成功，请重新登录')
        // 修改成功后退出登录
        setTimeout(() => {
          authStore.logout()
          router.push('/login')
        }, 1500)
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '密码修改失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleReset = () => {
  formRef.value?.resetFields()
}
</script>

<style scoped>
.change-password-page {
  padding: 24px;
  max-width: 600px;
  margin: 0 auto;
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  color: var(--el-text-color-primary);
}

.subtitle {
  margin: 0;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.password-form {
  margin-top: 20px;
}

.form-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-top: 20px;
}

.password-tips {
  margin-top: 32px;
  padding: 16px;
  background: var(--el-fill-color-light);
  border-radius: 8px;
}

.password-tips h4 {
  margin: 0 0 12px 0;
  color: var(--el-text-color-primary);
  font-size: 14px;
}

.password-tips ul {
  margin: 0;
  padding-left: 20px;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}

.password-tips li {
  margin-bottom: 6px;
}

.password-tips li:last-child {
  margin-bottom: 0;
}
</style>
