package com.exam.online.controller;

import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.ExamPaper;
import com.exam.online.service.ExamPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/papers")
public class ExamPaperController {

    @Autowired
    private ExamPaperService examPaperService;

    @GetMapping("/{id}")
    public ResponseDTO<ExamPaper> getById(@PathVariable Integer id) {
        ExamPaper paper = examPaperService.getById(id);
        if (paper == null) {
            return ResponseDTO.error(404, "试卷不存在");
        }
        return ResponseDTO.success(paper);
    }

    @GetMapping("/list")
    public ResponseDTO<List<ExamPaper>> getAll() {
        return ResponseDTO.success(examPaperService.getAll());
    }
}
