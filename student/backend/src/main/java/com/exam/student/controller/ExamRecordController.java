package com.exam.student.controller;

import com.exam.student.dto.PageDTO;
import com.exam.student.dto.PageQueryDTO;
import com.exam.student.dto.ResponseDTO;
import com.exam.student.entity.ExamRecord;
import com.exam.student.service.ExamRecordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exam-record")
public class ExamRecordController {

    @Autowired
    private ExamRecordService examRecordService;

    /**
     * 获取考试记录列表
     */
    @GetMapping("/list")
    public ResponseDTO<PageDTO<ExamRecord>> getExamRecords(
            PageQueryDTO query,
            HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        PageDTO<ExamRecord> records = examRecordService.getExamRecords(studentId, query);
        return ResponseDTO.success(records);
    }

    /**
     * 获取答卷详情
     */
    @GetMapping("/detail/{recordId}")
    public ResponseDTO<ExamRecord> getExamRecordDetail(
            @PathVariable Integer recordId,
            HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        ExamRecord record = examRecordService.getExamRecordDetail(recordId, studentId);
        return ResponseDTO.success(record);
    }

    /**
     * 获取所有考试记录
     */
    @GetMapping("/all")
    public ResponseDTO<List<ExamRecord>> getAllExamRecords(HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        List<ExamRecord> records = examRecordService.getAllExamRecords(studentId);
        return ResponseDTO.success(records);
    }
}
