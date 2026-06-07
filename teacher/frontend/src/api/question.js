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

export function batchImportQuestions(questions) {
  return request.post('/questions/batch/import', questions)
}

export function parseExcelFile(formData) {
  return request.post('/questions/parse/excel', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function parseWordFile(formData) {
  return request.post('/questions/parse/word', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function getQuestionStatistics() {
  return request.get('/questions/statistics')
}

export function getQuestionTrend() {
  return request.get('/questions/trend')
}

export function downloadExcelTemplate() {
  return request.get('/questions/template/excel', {
    responseType: 'blob'
  })
}

export function downloadWordTemplate() {
  return request.get('/questions/template/word', {
    responseType: 'blob'
  })
}
