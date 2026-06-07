import request from './request.js'

export default {
  // 开始考试
  startExam(examId) {
    return request.post(`/exam-taking/start/${examId}`)
  },

  // 获取试卷题目
  getQuestions(recordId) {
    return request.get(`/exam-taking/questions/${recordId}`)
  },

  // 保存答案
  saveAnswer(recordId, questionId, answer) {
    return request.post(`/exam-taking/save/${recordId}`, null, {
      params: { questionId, answer }
    })
  },

  // 提交试卷
  submitExam(recordId) {
    return request.post(`/exam-taking/submit/${recordId}`)
  },

  // 获取临时答案
  getTempAnswers(recordId) {
    return request.get(`/exam-taking/temp-answers/${recordId}`)
  },

  // 切屏次数+1
  screenSwitch(recordId) {
    return request.post(`/exam-taking/screen-switch/${recordId}`)
  },

  // 获取切屏次数
  getScreenSwitchCount(recordId) {
    return request.get(`/exam-taking/screen-switch-count/${recordId}`)
  }
}
