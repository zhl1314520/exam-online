import request from './request'

export function getExamRecords(examId) {
  return request.get(`/records/exam/${examId}`)
}

export function getExamRecordsPage(examId, params) {
  if (examId) {
    return request.get(`/records/exam/${examId}/page`, { params })
  }
  return request.get('/records/page', { params })
}

export function getExamStatistics(examId) {
  return request.get(`/records/${examId}/statistics`)
}

export function getRecordById(id) {
  return request.get(`/records/${id}`)
}

export function getRecordAnswers(id) {
  return request.get(`/records/${id}/answers`)
}