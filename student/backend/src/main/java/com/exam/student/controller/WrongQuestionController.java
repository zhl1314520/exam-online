package com.exam.student.controller;

import com.exam.student.dto.ResponseDTO;
import com.exam.student.entity.WrongQuestion;
import com.exam.student.service.WrongQuestionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wrong-question")
public class WrongQuestionController {

    @Autowired
    private WrongQuestionService wrongQuestionService;

    /**
     * 获取错题列表
     */
    @GetMapping("/list")
    public ResponseDTO<List<WrongQuestion>> getWrongQuestions(HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        List<WrongQuestion> wrongQuestions = wrongQuestionService.getWrongQuestions(studentId);
        return ResponseDTO.success(wrongQuestions);
    }

    /**
     * 按科目筛选错题
     */
    @GetMapping("/list/{subjectId}")
    public ResponseDTO<List<WrongQuestion>> getWrongQuestionsBySubject(
            @PathVariable Integer subjectId,
            HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        List<WrongQuestion> wrongQuestions = wrongQuestionService.getWrongQuestionsBySubject(studentId, subjectId);
        return ResponseDTO.success(wrongQuestions);
    }

    /**
     * 删除错题
     */
    @DeleteMapping("/{wrongId}")
    public ResponseDTO<Void> deleteWrongQuestion(
            @PathVariable Integer wrongId,
            HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        wrongQuestionService.deleteWrongQuestion(wrongId, studentId);
        return ResponseDTO.success("删除成功", null);
    }
}
