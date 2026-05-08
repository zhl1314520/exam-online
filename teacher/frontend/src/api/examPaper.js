import request from './request'

export function getPaperPage(params) {
  return request.get('/papers/page', { params })
}

export function getPaperList() {
  return request.get('/papers/list')
}

export function getPaperById(id) {
  return request.get(`/papers/${id}`)
}

export function addPaper(data) {
  return request.post('/papers/add', data)
}

export function updatePaper(data) {
  return request.put('/papers/update', data)
}

export function deletePaper(id) {
  return request.delete(`/papers/delete/${id}`)
}

export function getPaperQuestions(id) {
  return request.get(`/papers/${id}/questions`)
}

export function addQuestionToPaper(id, data) {
  return request.post(`/papers/${id}/questions`, data)
}