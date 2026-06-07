import request from './request'

export function getSubjectList() {
  return request({
    url: '/subjects/list',
    method: 'get'
  })
}

export function getSubjectPage(params) {
  return request({
    url: '/subjects/page',
    method: 'get',
    params
  })
}

export function getSubjectById(id) {
  return request({
    url: `/subjects/${id}`,
    method: 'get'
  })
}

export function getSubjectsByDepartment(departmentId) {
  return request({
    url: `/subjects/department/${departmentId}`,
    method: 'get'
  })
}

export function addSubject(data) {
  return request({
    url: '/subjects/add',
    method: 'post',
    data
  })
}

export function updateSubject(data) {
  return request({
    url: '/subjects/update',
    method: 'put',
    data
  })
}

export function deleteSubject(id) {
  return request({
    url: `/subjects/delete/${id}`,
    method: 'delete'
  })
}
