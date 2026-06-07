import request from './request'

export function getStudentPage(params) {
  return request.get('/students/page', { params })
}

export function getStudentList() {
  return request.get('/students/list')
}

export function getStudentsByClass(classId) {
  return request.get(`/students/class/${classId}`)
}

export function getStudentById(id) {
  return request.get(`/students/${id}`)
}