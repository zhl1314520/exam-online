package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Exam;
import com.exam.online.service.ExamService;
import com.exam.online.utils.JwtUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

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
    public ResponseDTO<PageDTO<Exam>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {

        Integer teacherId = getCurrentTeacherId(request);
        PageHelper.startPage(pageNum, pageSize);
        List<Exam> exams = examService.getByCreateBy(teacherId);

        return ResponseDTO.success(PageDTO.of((Page<Exam>) exams));
    }

    @GetMapping("/list")
    public ResponseDTO<List<Exam>> getAll(HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        return ResponseDTO.success(examService.getByCreateBy(teacherId));
    }

    @GetMapping("/{id}")
    public ResponseDTO<Exam> getById(@PathVariable Integer id) {
        Exam exam = examService.getById(id);
        if (exam == null) {
            return ResponseDTO.error(404, "考试不存在");
        }
        return ResponseDTO.success(exam);
    }

    @PostMapping("/add")
    public ResponseDTO<Exam> add(@RequestBody Exam exam, HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        exam.setCreateBy(teacherId);
        Exam created = examService.add(exam);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Exam> update(@RequestBody Exam exam) {
        Exam updated = examService.update(exam);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable Integer id) {
        examService.delete(id);
        return ResponseDTO.success("删除成功", null);
    }

    @PostMapping("/{id}/publish")
    public ResponseDTO<Void> publish(@PathVariable Integer id) {
        examService.publishExam(id);
        return ResponseDTO.success("发布成功", null);
    }

    @PostMapping("/{id}/start")
    public ResponseDTO<Void> start(@PathVariable Integer id) {
        examService.startExam(id);
        return ResponseDTO.success("考试开始", null);
    }

    @PostMapping("/{id}/end")
    public ResponseDTO<Void> end(@PathVariable Integer id) {
        examService.endExam(id);
        return ResponseDTO.success("考试结束", null);
    }
}