package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.ExamPaper;
import com.exam.online.service.ExamPaperService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/examPaper")
public class ExamPaperController {

    @Autowired
    private ExamPaperService examPaperService;

    @GetMapping("/{id}")
    public ResponseDTO<ExamPaper> getExamPaperById(@PathVariable Integer id) {
        ExamPaper examPaper = examPaperService.getExamPaperById(id);
        if (examPaper == null) {
            return ResponseDTO.error(404, "试卷不存在");
        }
        return ResponseDTO.success(examPaper);
    }

    @GetMapping("/list")
    public ResponseDTO<List<ExamPaper>> getAllExamPapers() {
        return ResponseDTO.success(examPaperService.getAllExamPapers());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<ExamPaper>> getExamPapersByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<ExamPaper> page = examPaperService.getExamPapersByPage(pageNum, pageSize);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseDTO<List<ExamPaper>> getExamPapersBySubjectId(@PathVariable Integer subjectId) {
        return ResponseDTO.success(examPaperService.getExamPapersBySubjectId(subjectId));
    }

    @GetMapping("/createBy/{createBy}")
    public ResponseDTO<List<ExamPaper>> getExamPapersByCreateBy(@PathVariable Integer createBy) {
        return ResponseDTO.success(examPaperService.getExamPapersByCreateBy(createBy));
    }

    @GetMapping("/status/{status}")
    public ResponseDTO<List<ExamPaper>> getExamPapersByStatus(@PathVariable Integer status) {
        return ResponseDTO.success(examPaperService.getExamPapersByStatus(status));
    }

    @PostMapping("/add")
    public ResponseDTO<ExamPaper> addExamPaper(@RequestBody ExamPaper examPaper) {
        ExamPaper created = examPaperService.addExamPaper(examPaper);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<ExamPaper> updateExamPaper(@RequestBody ExamPaper examPaper) {
        ExamPaper updated = examPaperService.updateExamPaper(examPaper);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> deleteExamPaper(@PathVariable Integer id) {
        examPaperService.deleteExamPaper(id);
        return ResponseDTO.success("删除成功", null);
    }

    @GetMapping("/count")
    public ResponseDTO<Integer> getExamPaperCount() {
        return ResponseDTO.success(examPaperService.getExamPaperCount());
    }

    @GetMapping("/count/subject/{subjectId}")
    public ResponseDTO<Integer> getExamPaperCountBySubjectId(@PathVariable Integer subjectId) {
        return ResponseDTO.success(examPaperService.getExamPaperCountBySubjectId(subjectId));
    }
}
