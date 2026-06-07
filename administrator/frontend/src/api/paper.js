import request from './request'

export function getPaperList() {
  return request({
    url: '/papers/list',
    method: 'get'
  })
}

export function getPaperById(id) {
  return request({
    url: `/papers/${id}`,
    method: 'get'
  })
}
