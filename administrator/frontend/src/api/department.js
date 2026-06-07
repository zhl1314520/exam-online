import request from './request'

export function getDepartmentList() {
  return request({
    url: '/departments/list',
    method: 'get'
  })
}

export function getDepartmentPage(params) {
  return request({
    url: '/departments/page',
    method: 'get',
    params
  })
}

export function getDepartmentById(id) {
  return request({
    url: `/departments/${id}`,
    method: 'get'
  })
}

export function addDepartment(data) {
  return request({
    url: '/departments/add',
    method: 'post',
    data
  })
}

export function updateDepartment(data) {
  return request({
    url: '/departments/update',
    method: 'put',
    data
  })
}

export function deleteDepartment(id) {
  return request({
    url: `/departments/delete/${id}`,
    method: 'delete'
  })
}