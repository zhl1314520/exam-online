import request from './request'

export function getQuestionPage(params) {
  return request.get('/questions/page', { params })
}

export function getQuestionList() {
  return request.get('/questions/list')
}

export function getQuestionById(id) {
  return request.get(`/questions/${id}`)
}

export function addQuestion(data) {
  return request.post('/questions/add', data)
}

export function updateQuestion(data) {
  return request.put('/questions/update', data)
}

export function deleteQuestion(id) {
  return request.delete(`/questions/delete/${id}`)
}