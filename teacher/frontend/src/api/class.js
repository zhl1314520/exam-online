import request from './request'

export function getClassList() {
  return request.get('/classes/list')
}
