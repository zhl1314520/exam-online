import request from './request'

export function getClassList() {
  return request({
    url: '/classes/list',
    method: 'get'
  })
}

export function getClassPage(params) {
  return request({
    url: '/classes/page',
    method: 'get',
    params
  })
}

export function getClassById(id) {
  return request({
    url: `/classes/${id}`,
    method: 'get'
  })
}

export function getClassesByDepartment(departmentId) {
  return request({
    url: `/classes/department/${departmentId}`,
    method: 'get'
  })
}

export function addClass(data) {
  return request({
    url: '/classes/add',
    method: 'post',
    data
  })
}

export function updateClass(data) {
  return request({
    url: '/classes/update',
    method: 'put',
    data
  })
}

export function deleteClass(id) {
  return request({
    url: `/classes/delete/${id}`,
    method: 'delete'
  })
}