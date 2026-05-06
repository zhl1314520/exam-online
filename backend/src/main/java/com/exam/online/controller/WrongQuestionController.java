package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.WrongQuestion;
import com.exam.online.service.WrongQuestionService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wrongQuestion")
public class WrongQuestionController {

    @Autowired
    private WrongQuestionService wrongQuestionService;

    @GetMapping("/{id}")
    public ResponseDTO<WrongQuestion> getWrongQuestionById(@PathVariable Integer id) {
        WrongQuestion wrongQuestion = wrongQuestionService.getWrongQuestionById(id);
        if (wrongQuestion == null) {
            return ResponseDTO.error(404, "错题记录不存在");
        }
        return ResponseDTO.success(wrongQuestion);
    }

    @GetMapping("/list")
    public ResponseDTO<List<WrongQuestion>> getAllWrongQuestions() {
        return ResponseDTO.success(wrongQuestionService.getAllWrongQuestions());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<WrongQuestion>> getWrongQuestionsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<WrongQuestion> page = wrongQuestionService.getWrongQuestionsByPage(pageNum, pageSize);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @GetMapping("/student/{studentId}")
    public ResponseDTO<List<WrongQuestion>> getWrongQuestionsByStudentId(@PathVariable Integer studentId) {
        return ResponseDTO.success(wrongQuestionService.getWrongQuestionsByStudentId(studentId));
    }

    @GetMapping("/question/{questionId}")
    public ResponseDTO<List<WrongQuestion>> getWrongQuestionsByQuestionId(@PathVariable Integer questionId) {
        return ResponseDTO.success(wrongQuestionService.getWrongQuestionsByQuestionId(questionId));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseDTO<List<WrongQuestion>> getWrongQuestionsBySubjectId(@PathVariable Integer subjectId) {
        return ResponseDTO.success(wrongQuestionService.getWrongQuestionsBySubjectId(subjectId));
    }

    @GetMapping("/student/{studentId}/question/{questionId}")
    public ResponseDTO<WrongQuestion> getWrongQuestionByStudentIdAndQuestionId(
            @PathVariable Integer studentId,
            @PathVariable Integer questionId) {
        WrongQuestion record = wrongQuestionService.getWrongQuestionByStudentIdAndQuestionId(studentId, questionId);
        if (record == null) {
            return ResponseDTO.error(404, "错题记录不存在");
        }
        return ResponseDTO.success(record);
    }

    @PostMapping("/add")
    public ResponseDTO<WrongQuestion> addWrongQuestion(@RequestBody WrongQuestion wrongQuestion) {
        WrongQuestion created = wrongQuestionService.addWrongQuestion(wrongQuestion);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<WrongQuestion> updateWrongQuestion(@RequestBody WrongQuestion wrongQuestion) {
        WrongQuestion updated = wrongQuestionService.updateWrongQuestion(wrongQuestion);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> deleteWrongQuestion(@PathVariable Integer id) {
        wrongQuestionService.deleteWrongQuestion(id);
        return ResponseDTO.success("删除成功", null);
    }

    @DeleteMapping("/delete/student/{studentId}/question/{questionId}")
    public ResponseDTO<Void> deleteWrongQuestionByStudentIdAndQuestionId(
            @PathVariable Integer studentId,
            @PathVariable Integer questionId) {
        wrongQuestionService.deleteWrongQuestionByStudentIdAndQuestionId(studentId, questionId);
        return ResponseDTO.success("删除成功", null);
    }

    @GetMapping("/count")
    public ResponseDTO<Integer> getWrongQuestionCount() {
        return ResponseDTO.success(wrongQuestionService.getWrongQuestionCount());
    }

    @GetMapping("/count/student/{studentId}")
    public ResponseDTO<Integer> getWrongQuestionCountByStudentId(@PathVariable Integer studentId) {
        return ResponseDTO.success(wrongQuestionService.getWrongQuestionCountByStudentId(studentId));
    }

    @GetMapping("/count/subject/{subjectId}")
    public ResponseDTO<Integer> getWrongQuestionCountBySubjectId(@PathVariable Integer subjectId) {
        return ResponseDTO.success(wrongQuestionService.getWrongQuestionCountBySubjectId(subjectId));
    }
}
