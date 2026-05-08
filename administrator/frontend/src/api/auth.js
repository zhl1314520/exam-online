import request from './request'

export function login(data) {
  return request({
    url: '/admin/login',
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