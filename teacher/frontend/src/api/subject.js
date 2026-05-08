import request from './request'

export function getSubjectList() {
  return request.get('/subjects/list')
}

export function getSubjectById(id) {
  return request.get(`/subjects/${id}`)
}