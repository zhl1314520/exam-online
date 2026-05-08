import { defineStore } from 'pinia'
import { login as loginApi } from '@/api/auth'
import router from '@/router'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    teacher: JSON.parse(localStorage.getItem('teacher') || 'null'),
    isLoggedIn: !!localStorage.getItem('token')
  }),

  getters: {
    isTeacher: (state) => state.isLoggedIn && state.teacher,
    teacherName: (state) => state.teacher?.realName || '',
    teacherId: (state) => state.teacher?.teacherId || null
  },

  actions: {
    async login(teacherNo, password) {
      try {
        const res = await loginApi({ teacherNo, password })
        this.token = res.data.token
        this.teacher = res.data.teacher
        this.isLoggedIn = true
        localStorage.setItem('token', this.token)
        localStorage.setItem('teacher', JSON.stringify(this.teacher))
        return true
      } catch (error) {
        return false
      }
    },

    logout() {
      this.token = ''
      this.teacher = null
      this.isLoggedIn = false
      localStorage.removeItem('token')
      localStorage.removeItem('teacher')
      router.push('/login')
    },

    updateTeacher(teacher) {
      this.teacher = teacher
      localStorage.setItem('teacher', JSON.stringify(teacher))
    }
  }
})