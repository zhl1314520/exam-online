package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Question;
import com.exam.online.service.QuestionService;
import com.exam.online.utils.JwtUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private JwtUtil jwtUtil;

    private Integer getCurrentTeacherId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtil.parseToken(token);
            return claims.get("teacherId", Integer.class);
        }
        return null;
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Question>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer subjectId,
            @RequestParam(required = false) Integer questionType,
            @RequestParam(required = false) Integer difficulty) {

        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionService.getAll();

        if (keyword != null && !keyword.isEmpty()) {
            questions = questions.stream()
                    .filter(q -> q.getQuestionContent().contains(keyword) ||
                            (q.getKnowledgePoint() != null && q.getKnowledgePoint().contains(keyword)))
                    .collect(Collectors.toList());
        }
        if (subjectId != null) {
            questions = questions.stream()
                    .filter(q -> q.getSubjectId().equals(subjectId))
                    .collect(Collectors.toList());
        }
        if (questionType != null) {
            questions = questions.stream()
                    .filter(q -> q.getQuestionType().equals(questionType))
                    .collect(Collectors.toList());
        }
        if (difficulty != null) {
            questions = questions.stream()
                    .filter(q -> q.getDifficulty().equals(difficulty))
                    .collect(Collectors.toList());
        }

        return ResponseDTO.success(PageDTO.of((Page<Question>) questions));
    }

    @GetMapping("/list")
    public ResponseDTO<List<Question>> getAll() {
        return ResponseDTO.success(questionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseDTO<Question> getById(@PathVariable Integer id) {
        Question question = questionService.getById(id);
        if (question == null) {
            return ResponseDTO.error(404, "题目不存在");
        }
        return ResponseDTO.success(question);
    }

    @PostMapping("/add")
    public ResponseDTO<Question> add(@RequestBody Question question, HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        question.setCreateBy(teacherId);
        Question created = questionService.add(question);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Question> update(@RequestBody Question question) {
        Question updated = questionService.update(question);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable Integer id) {
        questionService.delete(id);
        return ResponseDTO.success("删除成功", null);
    }
}