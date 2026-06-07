package com.exam.student.controller;

import com.exam.student.dto.ResponseDTO;
import com.exam.student.entity.Question;
import com.exam.student.exception.BusinessException;
import com.exam.student.service.ExamTakingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exam-taking")
public class ExamTakingController {

    @Autowired
    private ExamTakingService examTakingService;

    /**
     * 开始考试
     */
    @PostMapping("/start/{examId}")
    public ResponseDTO<Map<String, Object>> startExam(
            @PathVariable Integer examId,
            HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        Map<String, Object> result = examTakingService.startExam(examId, studentId, request);
        return ResponseDTO.success("考试已开始", result);
    }

    /**
     * 获取试卷题目
     */
    @GetMapping("/questions/{recordId}")
    public ResponseDTO<List<Question>> getQuestions(
            @PathVariable Integer recordId,
            HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        List<Question> questions = examTakingService.getQuestions(recordId, studentId);
        return ResponseDTO.success(questions);
    }

    /**
     * 保存答案
     */
    @PostMapping("/save/{recordId}")
    public ResponseDTO<Void> saveAnswer(
            @PathVariable Integer recordId,
            @RequestParam Integer questionId,
            @RequestParam String answer,
            HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        examTakingService.saveAnswer(recordId, questionId, answer, studentId);
        return ResponseDTO.success("答案已保存", null);
    }

    /**
     * 提交试卷
     */
    @PostMapping("/submit/{recordId}")
    public ResponseDTO<Map<String, Object>> submitExam(
            @PathVariable Integer recordId,
            HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        Map<String, Object> result = examTakingService.submitExam(recordId, studentId);
        return ResponseDTO.success("考试已提交", result);
    }

    /**
     * 获取临时答案
     */
    @GetMapping("/temp-answers/{recordId}")
    public ResponseDTO<Map<String, String>> getTempAnswers(
            @PathVariable Integer recordId,
            HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        Map<String, String> answers = examTakingService.getTempAnswers(recordId);
        return ResponseDTO.success(answers);
    }

    /**
     * 切屏次数+1
     */
    @PostMapping("/screen-switch/{recordId}")
    public ResponseDTO<Void> screenSwitch(
            @PathVariable Integer recordId,
            HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        examTakingService.incrementScreenSwitch(recordId, studentId);
        return ResponseDTO.success(null);
    }

    /**
     * 获取切屏次数
     */
    @GetMapping("/screen-switch-count/{recordId}")
    public ResponseDTO<Integer> getScreenSwitchCount(
            @PathVariable Integer recordId,
            HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        int count = examTakingService.getScreenSwitchCount(recordId, studentId);
        return ResponseDTO.success(count);
    }
}
