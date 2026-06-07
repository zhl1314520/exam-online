import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import authApi from '@/api/auth.js'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/views/Layout.vue'),
    redirect: '/exams',
    children: [
      {
        path: '/exams',
        name: 'ExamList',
        component: () => import('@/views/ExamList.vue'),
        meta: { title: '首页' }
      },
      {
        path: '/pending-exams',
        name: 'PendingExams',
        component: () => import('@/views/PendingExams.vue'),
        meta: { title: '待考考试' }
      },
      {
        path: '/exam-taking/:recordId',
        name: 'ExamTaking',
        component: () => import('@/views/ExamTaking.vue'),
        meta: { title: '在线考试', hideNav: true }
      },
      {
        path: '/records',
        name: 'ExamRecord',
        component: () => import('@/views/ExamRecord.vue'),
        meta: { title: '考试记录' }
      },
      {
        path: '/records/:recordId',
        name: 'RecordDetail',
        component: () => import('@/views/RecordDetail.vue'),
        meta: { title: '答卷详情' }
      },
      {
        path: '/wrong-book',
        name: 'WrongBook',
        component: () => import('@/views/WrongBook.vue'),
        meta: { title: '错题本' }
      },
      {
        path: '/score-report',
        name: 'ScoreReport',
        component: () => import('@/views/ScoreReport.vue'),
        meta: { title: '成绩单' }
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人信息' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/exams'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()

  // 检查URL中是否有从统一登录跳转过来的token
  const urlParams = new URLSearchParams(window.location.search)
  const urlToken = urlParams.get('token')

  if (urlToken && !authStore.token) {
    // 从统一登录跳转过来，先存储token，再用token获取用户信息
    try {
      authStore.token = urlToken
      localStorage.setItem('token', urlToken)
      // 用token从后端获取用户信息
      const res = await authApi.getCurrentUser()
      authStore.user = res.data
      localStorage.setItem('user', JSON.stringify(res.data))
      // 清除URL中的token参数
      window.history.replaceState({}, document.title, window.location.pathname)
      next()
      return
    } catch (e) {
      console.error('获取学生信息失败', e)
      authStore.logout()
      window.location.href = 'http://localhost:5173/login'
      return
    }
  }

  if (!authStore.isLoggedIn) {
    // 未登录，跳转到管理端统一登录
    window.location.href = 'http://localhost:5173/login'
    return
  } else {
    next()
  }
})

export default router
