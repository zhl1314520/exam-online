package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.AnswerRecord;
import com.exam.online.entity.Exam;
import com.exam.online.entity.ExamPaper;
import com.exam.online.entity.ExamRecord;
import com.exam.online.service.ExamPaperService;
import com.exam.online.service.ExamRecordService;
import com.exam.online.service.ExamService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/records")
public class ExamRecordController {

    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamPaperService examPaperService;

    @GetMapping("/exam/{examId}")
    public ResponseDTO<List<ExamRecord>> getByExamId(@PathVariable Integer examId) {
        return ResponseDTO.success(examRecordService.getByExamId(examId));
    }

    @GetMapping("/exam/{examId}/page")
    public ResponseDTO<PageDTO<ExamRecord>> getByExamIdPage(
            @PathVariable Integer examId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<ExamRecord> records = examRecordService.getByExamId(examId);

        return ResponseDTO.success(PageDTO.of((Page<ExamRecord>) records));
    }

    @GetMapping("/{id}")
    public ResponseDTO<ExamRecord> getById(@PathVariable Integer id) {
        ExamRecord record = examRecordService.getById(id);
        if (record == null) {
            return ResponseDTO.error(404, "考试记录不存在");
        }
        return ResponseDTO.success(record);
    }

    @GetMapping("/{id}/answers")
    public ResponseDTO<List<AnswerRecord>> getAnswers(@PathVariable Integer id) {
        return ResponseDTO.success(examRecordService.getAnswerRecords(id));
    }

    @GetMapping("/{id}/statistics")
    public ResponseDTO<Map<String, Object>> getStatistics(@PathVariable Integer id) {
        Exam exam = examService.getById(id);
        if (exam == null) {
            return ResponseDTO.error(404, "考试不存在");
        }
        ExamPaper paper = examPaperService.getById(exam.getPaperId());
        if (paper == null) {
            return ResponseDTO.error(404, "试卷不存在");
        }
        Map<String, Object> stats = examRecordService.getExamStatistics(id, paper.getPassScore());
        return ResponseDTO.success(stats);
    }
}