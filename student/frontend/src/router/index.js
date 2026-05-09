import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { public: true }
  },
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
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (to.meta.public) {
    next()
  } else if (!authStore.isLoggedIn) {
    next('/login')
  } else {
    next()
  }
})

export default router
