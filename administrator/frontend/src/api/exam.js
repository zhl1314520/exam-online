import request from './request'

export function getExamList() {
  return request({
    url: '/exams/list',
    method: 'get'
  })
}

export function getExamPage(params) {
  return request({
    url: '/exams/page',
    method: 'get',
    params
  })
}

export function getExamById(id) {
  return request({
    url: `/exams/${id}`,
    method: 'get'
  })
}

export function addExam(data) {
  return request({
    url: '/exams/add',
    method: 'post',
    data
  })
}

export function updateExam(data) {
  return request({
    url: '/exams/update',
    method: 'put',
    data
  })
}

export function deleteExam(id) {
  return request({
    url: `/exams/delete/${id}`,
    method: 'delete'
  })
}
