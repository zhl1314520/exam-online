import request from './request'

export function login(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

// 教师登录接口（通过 /api-teacher 前缀代理到 teacher 后端）
export function teacherLogin(data) {
  return request({
    url: '/teacher/auth/login',
    method: 'post',
    data
  })
}

// 学生登录接口（通过 /api-student 前缀代理到 student 后端）
export function studentLogin(data) {
  return request({
    url: '/student/auth/login',
    method: 'post',
    data
  })
}

// 统一登录接口，支持管理员、教师、学生
export function unifiedLogin(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function getProfile(adminId) {
  return request({
    url: '/admin/profile',
    method: 'get',
    params: { adminId }
  })
}

export function updatePassword(adminId, oldPassword, newPassword) {
  return request({
    url: '/admin/password',
    method: 'put',
    params: { adminId, oldPassword, newPassword }
  })
}