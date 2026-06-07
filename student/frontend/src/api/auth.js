import request from './request.js'

export default {
  // 登录
  login(data) {
    return request.post('/auth/login', data)
  },

  // 获取当前用户信息
  getCurrentUser() {
    return request.get('/auth/me')
  },

  // 修改密码
  updatePassword(data) {
    return request.post('/auth/password', data)
  }
}
