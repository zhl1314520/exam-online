import request from './request'

export function login(data) {
  return request.post('/auth/login', data)
}

export function getProfile() {
  return request.get('/teachers/profile')
}

export function changePassword(data) {
  return request.post('/teachers/change-password', data)
}