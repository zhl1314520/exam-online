import request from './request'

export function getTeacherList() {
  return request({
    url: '/teachers/list',
    method: 'get'
  })
}

export function getTeacherPage(params) {
  return request({
    url: '/teachers/page',
    method: 'get',
    params
  })
}

export function getTeacherById(id) {
  return request({
    url: `/teachers/${id}`,
    method: 'get'
  })
}

export function addTeacher(data) {
  return request({
    url: '/teachers/add',
    method: 'post',
    data
  })
}

export function updateTeacher(data) {
  return request({
    url: '/teachers/update',
    method: 'put',
    data
  })
}

export function deleteTeacher(id) {
  return request({
    url: `/teachers/delete/${id}`,
    method: 'delete'
  })
}