import request from './request'

export function getLogList() {
  return request({
    url: '/logs/list',
    method: 'get'
  })
}

export function getLogPage(params) {
  return request({
    url: '/logs/page',
    method: 'get',
    params
  })
}

export function getLogById(id) {
  return request({
    url: `/logs/${id}`,
    method: 'get'
  })
}