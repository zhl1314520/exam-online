import request from './request.js'

export default {
  // 获取待考考试列表
  getPendingExams() {
    return request.get('/exam/pending')
  },

  // 获取考试详情
  getExamDetail(examId) {
    return request.get(`/exam/${examId}`)
  },

  // 获取所有考试
  getAllExams() {
    return request.get('/exam/all')
  }
}
