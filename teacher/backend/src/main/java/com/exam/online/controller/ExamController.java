package com.exam.online.controller;

import com.exam.online.annotation.OperationLog;
import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Exam;
import com.exam.online.service.ExamService;
import com.exam.online.utils.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

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
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer paperId,
            @RequestParam(required = false) Integer status,
            HttpServletRequest request) {

        Integer teacherId = getCurrentTeacherId(request);
        PageHelper.startPage(pageNum, pageSize);
        List<Exam> exams = examService.getByCreateBy(teacherId);

        // 根据keyword筛选考试名称
        if (keyword != null && !keyword.isEmpty()) {
            exams = exams.stream()
                    .filter(e -> e.getExamName() != null && e.getExamName().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // 根据paperId筛选试卷
        if (paperId != null) {
            exams = exams.stream()
                    .filter(e -> e.getPaperId() != null && e.getPaperId().equals(paperId))
                    .collect(Collectors.toList());
        }

        // 根据status筛选状态
        if (status != null) {
            exams = exams.stream()
                    .filter(e -> e.getStatus() != null && e.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        PageInfo<Exam> pageInfo = new PageInfo<>(exams);
        PageDTO<Exam> pageDTO = new PageDTO<>();
        pageDTO.setTotal(pageInfo.getTotal());
        pageDTO.setPageNum(pageInfo.getPageNum());
        pageDTO.setPageSize(pageInfo.getPageSize());
        pageDTO.setPages(pageInfo.getPages());
        pageDTO.setList(pageInfo.getList());
        return ResponseDTO.success(pageDTO);
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
    @OperationLog(operation = "创建考试")
    public ResponseDTO<Exam> add(@RequestBody Exam exam, HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        exam.setCreateBy(teacherId);
        Exam created = examService.add(exam);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    @OperationLog(operation = "更新考试")
    public ResponseDTO<Exam> update(@RequestBody Exam exam) {
        Exam updated = examService.update(exam);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    @OperationLog(operation = "删除考试")
    public ResponseDTO<Void> delete(@PathVariable Integer id) {
        examService.delete(id);
        return ResponseDTO.success("删除成功", null);
    }

    @PostMapping("/{id}/publish")
    @OperationLog(operation = "发布考试")
    public ResponseDTO<Void> publish(@PathVariable Integer id) {
        examService.publishExam(id);
        return ResponseDTO.success("发布成功", null);
    }

    @PostMapping("/{id}/start")
    @OperationLog(operation = "开始考试")
    public ResponseDTO<Void> start(@PathVariable Integer id) {
        examService.startExam(id);
        return ResponseDTO.success("考试开始", null);
    }

    @PostMapping("/{id}/end")
    @OperationLog(operation = "结束考试")
    public ResponseDTO<Void> end(@PathVariable Integer id) {
        examService.endExam(id);
        return ResponseDTO.success("考试结束", null);
    }
}