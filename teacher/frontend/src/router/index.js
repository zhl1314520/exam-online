import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '教师登录' }
  },
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
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const token = authStore.token

  if (to.path === '/login') {
    next()
  } else if (!token) {
    next('/login')
  } else {
    next()
  }
})

export default router