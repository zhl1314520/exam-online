import request from './request.js'

export default {
  // 获取考试记录列表
  getExamRecords(params = {}) {
    return request.get('/exam-record/list', { params })
  },

  // 获取答卷详情
  getRecordDetail(recordId) {
    return request.get(`/exam-record/detail/${recordId}`)
  },

  // 获取所有考试记录
  getAllExamRecords() {
    return request.get('/exam-record/all')
  }
}
