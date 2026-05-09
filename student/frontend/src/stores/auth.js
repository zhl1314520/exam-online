import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import authApi from '@/api/auth.js'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))

  const isLoggedIn = computed(() => !!token.value)
  const studentName = computed(() => user.value?.realName || user.value?.studentNo || '')

  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUser = (newUser) => {
    user.value = newUser
    localStorage.setItem('user', JSON.stringify(newUser))
  }

  const login = async (credentials) => {
    const res = await authApi.login(credentials)
    setToken(res.data.token)
    setUser(res.data)
    return res
  }

  const logout = () => {
    token.value = ''
    user.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  const fetchUserInfo = async () => {
    const res = await authApi.getCurrentUser()
    setUser(res.data)
    return res.data
  }

  const updatePassword = async (data) => {
    return authApi.updatePassword(data)
  }

  return {
    token,
    user,
    isLoggedIn,
    studentName,
    login,
    logout,
    fetchUserInfo,
    updatePassword
  }
})
