package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.ExamRecord;
import com.exam.online.service.ExamRecordService;
import com.github.pagehelper.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 考试记录
@RestController
@RequestMapping("/examRecord")
public class ExamRecordController {

    @Autowired
    private ExamRecordService examRecordService;

    @GetMapping("/{id}")
    public ResponseDTO<ExamRecord> getExamRecordById(@PathVariable Integer id) {
        ExamRecord examRecord = examRecordService.getExamRecordById(id);
        if (examRecord == null) {
            return ResponseDTO.error(404, "考试记录不存在");
        }
        return ResponseDTO.success(examRecord);
    }

    @GetMapping("/list")
    public ResponseDTO<List<ExamRecord>> getAllExamRecords() {
        return ResponseDTO.success(examRecordService.getAllExamRecords());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<ExamRecord>> getExamRecordsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<ExamRecord> page = examRecordService.getExamRecordsByPage(pageNum, pageSize);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @GetMapping("/exam/{examId}")
    public ResponseDTO<List<ExamRecord>> getExamRecordsByExamId(@PathVariable Integer examId) {
        return ResponseDTO.success(examRecordService.getExamRecordsByExamId(examId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseDTO<List<ExamRecord>> getExamRecordsByStudentId(@PathVariable Integer studentId) {
        return ResponseDTO.success(examRecordService.getExamRecordsByStudentId(studentId));
    }

    @GetMapping("/exam/{examId}/student/{studentId}")
    public ResponseDTO<ExamRecord> getExamRecordByExamIdAndStudentId(
            @PathVariable Integer examId,
            @PathVariable Integer studentId) {
        ExamRecord record = examRecordService.getExamRecordByExamIdAndStudentId(examId, studentId);
        if (record == null) {
            return ResponseDTO.error(404, "考试记录不存在");
        }
        return ResponseDTO.success(record);
    }

    @PostMapping("/start")
    public ResponseDTO<ExamRecord> startExam(
            @RequestParam Integer examId,
            @RequestParam Integer studentId,
            HttpServletRequest request) {
        String ipAddress = getIpAddress(request);
        ExamRecord record = examRecordService.startExam(examId, studentId, ipAddress);
        return ResponseDTO.success("考试开始", record);
    }

    @PutMapping("/submit")
    public ResponseDTO<ExamRecord> submitExam(@RequestBody ExamRecord examRecord) {
        ExamRecord submitted = examRecordService.submitExam(examRecord);
        return ResponseDTO.success("提交成功", submitted);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> deleteExamRecord(@PathVariable Integer id) {
        examRecordService.deleteExamRecord(id);
        return ResponseDTO.success("删除成功", null);
    }

    @GetMapping("/count")
    public ResponseDTO<Integer> getExamRecordCount() {
        return ResponseDTO.success(examRecordService.getExamRecordCount());
    }

    @GetMapping("/count/exam/{examId}")
    public ResponseDTO<Integer> getExamRecordCountByExamId(@PathVariable Integer examId) {
        return ResponseDTO.success(examRecordService.getExamRecordCountByExamId(examId));
    }

    @GetMapping("/count/student/{studentId}")
    public ResponseDTO<Integer> getExamRecordCountByStudentId(@PathVariable Integer studentId) {
        return ResponseDTO.success(examRecordService.getExamRecordCountByStudentId(studentId));
    }

    @GetMapping("/stats/exam/{examId}")
    public ResponseDTO<Map<String, Double>> getExamStats(@PathVariable Integer examId) {
        Map<String, Double> stats = new HashMap<>();
        stats.put("average", examRecordService.getAverageScoreByExamId(examId));
        stats.put("max", examRecordService.getMaxScoreByExamId(examId));
        stats.put("min", examRecordService.getMinScoreByExamId(examId));
        return ResponseDTO.success(stats);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
