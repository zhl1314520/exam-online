package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Question;
import com.exam.online.service.QuestionService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/{id}")
    public ResponseDTO<Question> getQuestionById(@PathVariable Integer id) {
        Question question = questionService.getQuestionById(id);
        if (question == null) {
            return ResponseDTO.error(404, "试题不存在");
        }
        return ResponseDTO.success(question);
    }

    @GetMapping("/list")
    public ResponseDTO<List<Question>> getAllQuestions() {
        return ResponseDTO.success(questionService.getAllQuestions());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Question>> getQuestionsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Question> page = questionService.getQuestionsByPage(pageNum, pageSize);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseDTO<List<Question>> getQuestionsBySubjectId(@PathVariable Integer subjectId) {
        return ResponseDTO.success(questionService.getQuestionsBySubjectId(subjectId));
    }

    @GetMapping("/type/{questionType}")
    public ResponseDTO<List<Question>> getQuestionsByType(@PathVariable Integer questionType) {
        return ResponseDTO.success(questionService.getQuestionsByType(questionType));
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseDTO<List<Question>> getQuestionsByDifficulty(@PathVariable Integer difficulty) {
        return ResponseDTO.success(questionService.getQuestionsByDifficulty(difficulty));
    }

    @GetMapping("/createBy/{createBy}")
    public ResponseDTO<List<Question>> getQuestionsByCreateBy(@PathVariable Integer createBy) {
        return ResponseDTO.success(questionService.getQuestionsByCreateBy(createBy));
    }

    @PostMapping("/add")
    public ResponseDTO<Question> addQuestion(@RequestBody Question question) {
        Question created = questionService.addQuestion(question);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Question> updateQuestion(@RequestBody Question question) {
        Question updated = questionService.updateQuestion(question);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> deleteQuestion(@PathVariable Integer id) {
        questionService.deleteQuestion(id);
        return ResponseDTO.success("删除成功", null);
    }

    @GetMapping("/count")
    public ResponseDTO<Integer> getQuestionCount() {
        return ResponseDTO.success(questionService.getQuestionCount());
    }

    @GetMapping("/count/subject/{subjectId}")
    public ResponseDTO<Integer> getQuestionCountBySubjectId(@PathVariable Integer subjectId) {
        return ResponseDTO.success(questionService.getQuestionCountBySubjectId(subjectId));
    }

    @GetMapping("/count/type/{questionType}")
    public ResponseDTO<Integer> getQuestionCountByType(@PathVariable Integer questionType) {
        return ResponseDTO.success(questionService.getQuestionCountByType(questionType));
    }
}
