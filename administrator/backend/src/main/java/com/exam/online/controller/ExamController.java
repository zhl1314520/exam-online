package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Exam;
import com.exam.online.service.ExamService;
import com.exam.online.service.SystemLogService;
import com.github.pagehelper.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private SystemLogService systemLogService;

    @GetMapping("/{id}")
    public ResponseDTO<Exam> getById(@PathVariable Integer id) {
        Exam exam = examService.getById(id);
        if (exam == null) {
            return ResponseDTO.error(404, "考试不存在");
        }
        return ResponseDTO.success(exam);
    }

    @GetMapping("/list")
    public ResponseDTO<List<Exam>> getAll() {
        return ResponseDTO.success(examService.getAll());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Exam>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        Page<Exam> page = examService.getByPage(pageNum, pageSize, keyword, status);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @PostMapping("/add")
    public ResponseDTO<Exam> add(@RequestBody Exam exam, HttpServletRequest request) {
        Exam created = examService.add(exam);
        systemLogService.log("新增", "新增考试：" + exam.getExamName(), request);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Exam> update(@RequestBody Exam exam, HttpServletRequest request) {
        Exam updated = examService.update(exam);
        systemLogService.log("修改", "修改考试：" + exam.getExamName(), request);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable Integer id, HttpServletRequest request) {
        Exam exam = examService.getById(id);
        examService.delete(id);
        String detail = exam != null ? "删除考试：" + exam.getExamName() : "删除考试ID：" + id;
        systemLogService.log("删除", detail, request);
        return ResponseDTO.success("删除成功", null);
    }
}
