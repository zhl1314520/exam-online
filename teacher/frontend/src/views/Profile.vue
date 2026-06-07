<template>
  <div class="profile-page">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <h2>个人资料</h2>
        </div>
      </template>

      <div v-if="loading" class="loading">
        <el-skeleton style="width: 100%" animated>
          <template #template>
            <div style="padding: 14px;">
              <el-skeleton-item variant="text" style="width: 50%; margin-bottom: 16px" />
              <div style="display: flex; justify-content: space-between;">
                <el-skeleton-item variant="rectangular" style="width: 45%; height: 80px" />
                <el-skeleton-item variant="rectangular" style="width: 45%; height: 80px" />
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>

      <div v-else-if="profile" class="profile-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="真实姓名">{{ profile.realName }}</el-descriptions-item>
          <el-descriptions-item label="工号">{{ profile.teacherNo }}</el-descriptions-item>
          <el-descriptions-item label="性别">
            {{ profile.gender === 1 ? '男' : profile.gender === 0 ? '女' : '未知' }}
          </el-descriptions-item>
          <el-descriptions-item label="部门">{{ profile.departmentName }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ profile.email }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ profile.phone }}</el-descriptions-item>
          <el-descriptions-item label="账号状态">
            <el-tag :type="profile.status === 1 ? 'success' : 'danger'">
              {{ profile.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(profile.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="最后登录时间">
            {{ formatDateTime(profile.lastLoginTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <div v-else class="error">
        <el-alert type="error" title="获取用户信息失败，请重试" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProfile } from '@/api/auth'

const loading = ref(true)
const profile = ref(null)

// 格式化日期时间
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return ''
  const date = new Date(dateTimeStr)
  return date.toLocaleString('zh-CN')
}

// 获取用户信息
const fetchProfile = async () => {
  loading.value = true
  try {
    const response = await getProfile()
    profile.value = response.data
  } catch (error) {
    console.error('获取用户信息失败:', error)
    profile.value = null
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchProfile()
})
</script>

<style scoped>
.profile-page {
  padding: $spacing-lg;
}

.profile-card {
  max-width: 900px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.loading {
  min-height: 300px;
}

.error {
  padding: $spacing-lg;
}

.el-descriptions {
  margin-top: $spacing-md;
}

.el-descriptions-item {
  :deep(.cell) {
    word-break: break-all;
  }
}

.el-tag {
  margin-right: 0;
}
</style>