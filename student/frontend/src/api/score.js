import request from './request.js'

export default {
  // 获取成绩统计
  getStatistics() {
    return request.get('/score-report/statistics')
  },

  // 获取成绩趋势
  getTrend() {
    return request.get('/score-report/trend')
  },

  // 获取知识点雷达图
  getRadar() {
    return request.get('/score-report/radar')
  },

  // 获取科目成绩分布
  getSubjectDistribution() {
    return request.get('/score-report/subject-distribution')
  },

  // 获取最近成绩记录（带排名）
  getRecentRecords() {
    return request.get('/score-report/recent-records')
  },

  // 获取首页统计数据
  getHomeStatistics() {
    return request.get('/score-report/home-statistics')
  }
}
