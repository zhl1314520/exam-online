package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Exam;
import com.exam.online.service.ExamService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("/{id}")
    public ResponseDTO<Exam> getExamById(@PathVariable Integer id) {
        Exam exam = examService.getExamById(id);
        if (exam == null) {
            return ResponseDTO.error(404, "考试不存在");
        }
        return ResponseDTO.success(exam);
    }

    @GetMapping("/list")
    public ResponseDTO<List<Exam>> getAllExams() {
        return ResponseDTO.success(examService.getAllExams());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Exam>> getExamsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Exam> page = examService.getExamsByPage(pageNum, pageSize);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @GetMapping("/class/{classId}")
    public ResponseDTO<List<Exam>> getExamsByClassId(@PathVariable Integer classId) {
        return ResponseDTO.success(examService.getExamsByClassId(classId));
    }

    @GetMapping("/paper/{paperId}")
    public ResponseDTO<List<Exam>> getExamsByPaperId(@PathVariable Integer paperId) {
        return ResponseDTO.success(examService.getExamsByPaperId(paperId));
    }

    @GetMapping("/createBy/{createBy}")
    public ResponseDTO<List<Exam>> getExamsByCreateBy(@PathVariable Integer createBy) {
        return ResponseDTO.success(examService.getExamsByCreateBy(createBy));
    }

    @GetMapping("/status/{status}")
    public ResponseDTO<List<Exam>> getExamsByStatus(@PathVariable Integer status) {
        return ResponseDTO.success(examService.getExamsByStatus(status));
    }

    @PostMapping("/add")
    public ResponseDTO<Exam> addExam(@RequestBody Exam exam) {
        Exam created = examService.addExam(exam);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Exam> updateExam(@RequestBody Exam exam) {
        Exam updated = examService.updateExam(exam);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> deleteExam(@PathVariable Integer id) {
        examService.deleteExam(id);
        return ResponseDTO.success("删除成功", null);
    }

    @GetMapping("/count")
    public ResponseDTO<Integer> getExamCount() {
        return ResponseDTO.success(examService.getExamCount());
    }

    @GetMapping("/count/class/{classId}")
    public ResponseDTO<Integer> getExamCountByClassId(@PathVariable Integer classId) {
        return ResponseDTO.success(examService.getExamCountByClassId(classId));
    }

    @GetMapping("/count/status/{status}")
    public ResponseDTO<Integer> getExamCountByStatus(@PathVariable Integer status) {
        return ResponseDTO.success(examService.getExamCountByStatus(status));
    }
}
