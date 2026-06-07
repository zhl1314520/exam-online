package com.exam.student.controller;

import com.exam.student.dto.ResponseDTO;
import com.exam.student.service.ScoreReportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/score-report")
public class ScoreReportController {

    @Autowired
    private ScoreReportService scoreReportService;

    /**
     * 获取成绩统计
     */
    @GetMapping("/statistics")
    public ResponseDTO<Map<String, Object>> getStatistics(HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        Map<String, Object> stats = scoreReportService.getScoreStatistics(studentId);
        return ResponseDTO.success(stats);
    }

    /**
     * 获取成绩趋势
     */
    @GetMapping("/trend")
    public ResponseDTO<List<Map<String, Object>>> getScoreTrend(HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        List<Map<String, Object>> trend = scoreReportService.getScoreTrend(studentId);
        return ResponseDTO.success(trend);
    }

    /**
     * 获取知识点雷达图数据
     */
    @GetMapping("/radar")
    public ResponseDTO<Map<String, Object>> getKnowledgeRadar(HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        Map<String, Object> radar = scoreReportService.getKnowledgeRadar(studentId);
        return ResponseDTO.success(radar);
    }

    /**
     * 获取科目成绩分布
     */
    @GetMapping("/subject-distribution")
    public ResponseDTO<List<Map<String, Object>>> getSubjectDistribution(HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        List<Map<String, Object>> distribution = scoreReportService.getSubjectScoreDistribution(studentId);
        return ResponseDTO.success(distribution);
    }

    /**
     * 获取最近成绩列表（带排名）
     */
    @GetMapping("/recent-records")
    public ResponseDTO<List<Map<String, Object>>> getRecentRecords(HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        List<Map<String, Object>> records = scoreReportService.getRecentScoresWithRank(studentId);
        return ResponseDTO.success(records);
    }

    /**
     * 获取首页统计数据
     */
    @GetMapping("/home-statistics")
    public ResponseDTO<Map<String, Object>> getHomeStatistics(HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        Map<String, Object> stats = scoreReportService.getHomeStatistics(studentId);
        return ResponseDTO.success(stats);
    }
}
