import request from './request'

export function getStudentList() {
  return request({
    url: '/students/list',
    method: 'get'
  })
}

export function getStudentPage(params) {
  return request({
    url: '/students/page',
    method: 'get',
    params: {
      pageNum: params.pageNum || params.page || 1,
      pageSize: params.pageSize || params.size || 10,
      keyword: params.keyword || undefined,
      classId: params.classId || undefined
    }
  })
}

export function getStudentById(id) {
  return request({
    url: `/students/${id}`,
    method: 'get'
  })
}

export function addStudent(data) {
  return request({
    url: '/students/add',
    method: 'post',
    data
  })
}

export function updateStudent(data) {
  return request({
    url: '/students/update',
    method: 'put',
    data
  })
}

export function deleteStudent(id) {
  return request({
    url: `/students/delete/${id}`,
    method: 'delete'
  })
}

export function batchAddStudents(data) {
  return request({
    url: '/students/batch',
    method: 'post',
    data
  })
}