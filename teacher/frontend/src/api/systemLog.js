import request from './request'

export function getRecentLogs(limit = 10) {
  return request.get('/system-logs/recent', { params: { limit } })
}
