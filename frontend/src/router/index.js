import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
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
        meta: { title: '仪表盘' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人信息' }
      },
      {
        path: 'change-password',
        name: 'ChangePassword',
        component: () => import('@/views/ChangePassword.vue'),
        meta: { title: '修改密码' }
      },
      {
        path: 'students',
        name: 'Students',
        component: () => import('@/views/students/StudentList.vue'),
        meta: { title: '学生管理' }
      },
      {
        path: 'teachers',
        name: 'Teachers',
        component: () => import('@/views/teachers/TeacherList.vue'),
        meta: { title: '教师管理' }
      },
      {
        path: 'classes',
        name: 'Classes',
        component: () => import('@/views/classes/ClassList.vue'),
        meta: { title: '班级管理' }
      },
      {
        path: 'departments',
        name: 'Departments',
        component: () => import('@/views/departments/DepartmentList.vue'),
        meta: { title: '院系管理' }
      },
      {
        path: 'logs',
        name: 'Logs',
        component: () => import('@/views/logs/LogList.vue'),
        meta: { title: '系统日志' }
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/statistics/Statistics.vue'),
        meta: { title: '数据统计' }
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