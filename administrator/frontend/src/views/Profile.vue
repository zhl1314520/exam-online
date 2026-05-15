<template>
  <div class="profile-container">
    <div class="profile-header">
      <h2 class="page-title">个人信息</h2>
      <el-button type="primary" size="small" @click="editProfile">
        <Icon icon="mdi:pencil" />
        编辑信息
      </el-button>
    </div>

    <div class="profile-card">
      <div class="avatar-area">
        <div class="avatar">
          <Icon icon="mdi:account-circle" class="avatar-icon" />
        </div>
      </div>

      <div class="form-grid">
        <div class="form-item">
          <span class="form-label">管理员账号</span>
          <span class="form-value">{{ admin.username }}</span>
        </div>

        <div class="form-item">
          <span class="form-label">真实姓名</span>
          <span class="form-value">{{ admin.realName || '未设置' }}</span>
        </div>

        <div class="form-item">
          <span class="form-label">邮箱</span>
          <span class="form-value">{{ admin.email || '未设置' }}</span>
        </div>

        <div class="form-item">
          <span class="form-label">手机号</span>
          <span class="form-value">{{ admin.phone || '未设置' }}</span>
        </div>

        <div class="form-item">
          <span class="form-label">状态</span>
          <span class="form-value">
            <el-tag :type="admin.status === 1 ? 'success' : 'danger'">
              {{ admin.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </span>
        </div>

        <div class="form-item">
          <span class="form-label">创建时间</span>
          <span class="form-value">{{ formatTime(admin.createTime) }}</span>
        </div>

        <div class="form-item">
          <span class="form-label">最后登录时间</span>
          <span class="form-value">{{ formatTime(admin.lastLoginTime) }}</span>
        </div>
      </div>
    </div>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑个人信息"
      width="500px"
      class="edit-dialog"
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        :rules="editFormRules"
        label-width="100px"
      >
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="editForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" placeholder="请输入邮箱" />
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="editForm.phone" placeholder="请输入手机号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEditSubmit" :loading="editLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { getProfile as getProfileApi } from '@/api/auth'
import { updateAdmin } from '@/api/admin'

const router = useRouter()
const authStore = useAuthStore()

const admin = ref({
  username: '',
  realName: '',
  email: '',
  phone: '',
  status: 1,
  createTime: '',
  lastLoginTime: ''
})

const editDialogVisible = ref(false)
const editLoading = ref(false)
const editFormRef = ref()

const editForm = reactive({
  adminId: null,
  realName: '',
  email: '',
  phone: ''
})

const editFormRules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ]
}

const loadProfile = async () => {
  try {
    const res = await getProfileApi(authStore.adminId)
    admin.value = res.data
  } catch (error) {
    console.error('获取个人信息失败', error)
  }
}

const editProfile = () => {
  editForm.adminId = admin.value.adminId
  editForm.realName = admin.value.realName || ''
  editForm.email = admin.value.email || ''
  editForm.phone = admin.value.phone || ''
  editDialogVisible.value = true
}

const handleEditSubmit = async () => {
  try {
    await editFormRef.value.validate()
    editLoading.value = true
    await updateAdmin(editForm)
    editDialogVisible.value = false
    await loadProfile()
    authStore.updateAdmin(admin.value)
    ElMessage.success('信息更新成功')
  } catch (error) {
    console.error('更新信息失败', error)
  } finally {
    editLoading.value = false
  }
}

const formatTime = (time) => {
  if (!time) return '暂无'
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  loadProfile()
})
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.profile-container {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-lg $spacing-xl;
  background: $bg-card;
  border-radius: $radius-lg;
  border: $border-width solid $border-color;
}

.page-title {
  font-size: $font-size-xl;
  font-weight: $font-weight-semibold;
  color: $text-primary;
}

.profile-card {
  background: $bg-card;
  border-radius: $radius-lg;
  border: $border-width solid $border-color;
  padding: $spacing-2xl;
}

.avatar-area {
  display: flex;
  justify-content: center;
  padding: $spacing-2xl 0 $spacing-xl;

  .avatar {
    width: 100px;
    height: 100px;
    border-radius: $radius-xl;
    background: $primary-color;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .avatar-icon {
    font-size: 48px;
    color: $bg-primary;
  }
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: $spacing-xl;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;

  .form-label {
    font-size: $font-size-sm;
    font-weight: $font-weight-medium;
    color: $text-muted;
  }

  .form-value {
    font-size: $font-size-md;
    color: $text-primary;
    padding: $spacing-sm;
    background: rgba($black, 0.02);
    border-radius: $radius-sm;
  }
}

// 编辑对话框样式
.edit-dialog {
  :deep(.el-dialog__header) {
    padding: $spacing-xl $spacing-xl 0;
  }

  :deep(.el-dialog__body) {
    padding: $spacing-xl $spacing-xl $spacing-lg;
  }

  :deep(.el-dialog__footer) {
    padding: 0 $spacing-xl $spacing-xl;
  }
}

// 响应式
@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
