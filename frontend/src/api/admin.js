import request from './request'

// 管理员相关 API
export function updateAdmin(data) {
  return request({
    url: '/admin/update',
    method: 'put',
    data
  })
}
