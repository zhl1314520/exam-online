import request from './request'

export function getExamPage(params) {
  return request.get('/exams/page', { params })
}

export function getExamList() {
  return request.get('/exams/list')
}

export function getExamById(id) {
  return request.get(`/exams/${id}`)
}

export function addExam(data) {
  return request.post('/exams/add', data)
}

export function updateExam(data) {
  return request.put('/exams/update', data)
}

export function deleteExam(id) {
  return request.delete(`/exams/delete/${id}`)
}

export function publishExam(id) {
  return request.post(`/exams/${id}/publish`)
}

export function startExam(id) {
  return request.post(`/exams/${id}/start`)
}

export function endExam(id) {
  return request.post(`/exams/${id}/end`)
}