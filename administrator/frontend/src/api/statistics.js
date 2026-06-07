import request from './request'

export function getOverview() {
  return request({
    url: '/statistics/overview',
    method: 'get'
  })
}

export function getUserStatistics() {
  return request({
    url: '/statistics/users',
    method: 'get'
  })
}