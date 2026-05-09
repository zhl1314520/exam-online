package com.exam.student.controller;

import com.exam.student.dto.ResponseDTO;
import com.exam.student.entity.Exam;
import com.exam.student.service.ExamService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    /**
     * 获取待考考试列表
     */
    @GetMapping("/pending")
    public ResponseDTO<List<Exam>> getPendingExams(HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        List<Exam> exams = examService.getPendingExams(studentId);
        return ResponseDTO.success(exams);
    }

    /**
     * 获取考试详情
     */
    @GetMapping("/{examId}")
    public ResponseDTO<Exam> getExamDetail(@PathVariable Integer examId) {
        Exam exam = examService.getExamDetail(examId);
        return ResponseDTO.success(exam);
    }

    /**
     * 获取所有考试（包括已结束）
     */
    @GetMapping("/all")
    public ResponseDTO<List<Exam>> getAllExams(HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        List<Exam> exams = examService.getAllExams(studentId);
        return ResponseDTO.success(exams);
    }
}
