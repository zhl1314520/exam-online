import request from './request.js'

export default {
  // 获取错题列表
  getWrongQuestions() {
    return request.get('/wrong-question/list')
  },

  // 按科目筛选错题
  getWrongQuestionsBySubject(subjectId) {
    return request.get(`/wrong-question/list/${subjectId}`)
  },

  // 删除错题
  deleteWrongQuestion(wrongId) {
    return request.delete(`/wrong-question/${wrongId}`)
  }
}
