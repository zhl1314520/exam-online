import { defineStore } from 'pinia'
import { login as loginApi, getProfile } from '@/api/auth'
import router from '@/router'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    admin: JSON.parse(localStorage.getItem('admin') || 'null'),
    isLoggedIn: !!localStorage.getItem('token')
  }),

  getters: {
    isAdmin: (state) => state.isLoggedIn && state.admin,
    adminName: (state) => state.admin?.realName || '',
    adminId: (state) => state.admin?.adminId || null
  },

  actions: {
    async login(username, password) {
      try {
        const res = await loginApi({ username, password })
        this.token = res.data.token
        this.admin = res.data.admin
        this.isLoggedIn = true
        localStorage.setItem('token', this.token)
        localStorage.setItem('admin', JSON.stringify(this.admin))
        return true
      } catch (error) {
        return false
      }
    },

    async fetchProfile(adminId) {
      try {
        const res = await getProfile(adminId)
        this.admin = res.data
        localStorage.setItem('admin', JSON.stringify(this.admin))
      } catch (error) {
        console.error('获取管理员信息失败', error)
      }
    },

    logout() {
      this.token = ''
      this.admin = null
      this.isLoggedIn = false
      localStorage.removeItem('token')
      localStorage.removeItem('admin')
      router.push('/login')
    },

    updateAdmin(admin) {
      this.admin = admin
      localStorage.setItem('admin', JSON.stringify(admin))
    }
  }
})