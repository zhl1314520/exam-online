import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { getProfile } from '@/api/auth'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/components/layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '仪表盘', icon: 'Dashboard' }
      },
      {
        path: 'questions',
        name: 'Questions',
        component: () => import('@/views/questions/QuestionList.vue'),
        meta: { title: '题库管理', icon: 'Document' }
      },
      {
        path: 'papers',
        name: 'Papers',
        component: () => import('@/views/papers/PaperList.vue'),
        meta: { title: '试卷管理', icon: 'Collection' }
      },
      {
        path: 'exams',
        name: 'Exams',
        component: () => import('@/views/exams/ExamList.vue'),
        meta: { title: '考试管理', icon: 'Calendar' }
      },
      {
        path: 'records',
        name: 'Records',
        component: () => import('@/views/records/RecordList.vue'),
        meta: { title: '成绩管理', icon: 'TrendCharts' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人资料', icon: 'User' }
      },
      {
        path: 'change-password',
        name: 'ChangePassword',
        component: () => import('@/views/ChangePassword.vue'),
        meta: { title: '修改密码', icon: 'Lock' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()

  // 检查URL中是否有从统一登录跳转过来的token
  const urlParams = new URLSearchParams(window.location.search)
  const urlToken = urlParams.get('token')

  if (urlToken && !authStore.token) {
    // 从统一登录跳转过来，先存储token，再用token获取用户信息
    try {
      authStore.token = urlToken
      authStore.isLoggedIn = true
      localStorage.setItem('token', urlToken)
      // 用token从后端获取用户信息
      const res = await getProfile()
      authStore.teacher = res.data
      localStorage.setItem('teacher', JSON.stringify(res.data))
      // 清除URL中的token参数
      window.history.replaceState({}, document.title, window.location.pathname)
      next()
      return
    } catch (e) {
      console.error('获取教师信息失败', e)
      authStore.logout()
      window.location.href = 'http://localhost:5173/login'
      return
    }
  }

  if (!authStore.token) {
    // 未登录，跳转到管理端统一登录
    window.location.href = 'http://localhost:5173/login'
    return
  } else {
    next()
  }
})

export default router