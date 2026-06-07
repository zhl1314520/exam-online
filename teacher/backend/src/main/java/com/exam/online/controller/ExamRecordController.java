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
import com.exam.online.utils.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.io.IOException;
import java.net.URLEncoder;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/records")
public class ExamRecordController {

    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamPaperService examPaperService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/exam/{examId}")
    public ResponseDTO<List<ExamRecord>> getByExamId(@PathVariable Integer examId) {
        return ResponseDTO.success(examRecordService.getByExamId(examId));
    }

    @GetMapping("/exam/{examId}/page")
    public ResponseDTO<PageDTO<ExamRecord>> getByExamIdPage(
            @PathVariable Integer examId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {

        PageHelper.startPage(pageNum, pageSize);
        List<ExamRecord> records = examRecordService.getByExamId(examId);

        // 根据keyword筛选学生姓名或学号
        if (keyword != null && !keyword.isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            records = records.stream()
                    .filter(r -> (r.getStudentName() != null && r.getStudentName().toLowerCase().contains(lowerKeyword))
                            || (r.getStudentNo() != null && r.getStudentNo().toLowerCase().contains(lowerKeyword)))
                    .collect(Collectors.toList());
        }

        PageInfo<ExamRecord> pageInfo = new PageInfo<>(records);
        PageDTO<ExamRecord> pageDTO = new PageDTO<>();
        pageDTO.setTotal(pageInfo.getTotal());
        pageDTO.setPageNum(pageInfo.getPageNum());
        pageDTO.setPageSize(pageInfo.getPageSize());
        pageDTO.setPages(pageInfo.getPages());
        pageDTO.setList(pageInfo.getList());
        return ResponseDTO.success(pageDTO);
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<ExamRecord>> getAllPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {

        PageHelper.startPage(pageNum, pageSize);
        List<ExamRecord> records = examRecordService.getAll();

        // 根据keyword筛选学生姓名或学号
        if (keyword != null && !keyword.isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            records = records.stream()
                    .filter(r -> (r.getStudentName() != null && r.getStudentName().toLowerCase().contains(lowerKeyword))
                            || (r.getStudentNo() != null && r.getStudentNo().toLowerCase().contains(lowerKeyword)))
                    .collect(Collectors.toList());
        }

        PageInfo<ExamRecord> pageInfo = new PageInfo<>(records);
        PageDTO<ExamRecord> pageDTO = new PageDTO<>();
        pageDTO.setTotal(pageInfo.getTotal());
        pageDTO.setPageNum(pageInfo.getPageNum());
        pageDTO.setPageSize(pageInfo.getPageSize());
        pageDTO.setPages(pageInfo.getPages());
        pageDTO.setList(pageInfo.getList());
        return ResponseDTO.success(pageDTO);
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

    @GetMapping("/recent")
    public ResponseDTO<List<Map<String, Object>>> getRecentOperations() {
        List<ExamRecord> records = examRecordService.getAll();

        // Get the latest 10 records
        List<ExamRecord> recentRecords = records.stream()
            .sorted((r1, r2) -> r2.getStartTime().compareTo(r1.getStartTime()))
            .limit(10)
            .collect(Collectors.toList());

        List<Map<String, Object>> result = new ArrayList<>();
        for (ExamRecord record : recentRecords) {
            Map<String, Object> operation = new HashMap<>();
            operation.put("id", record.getRecordId());
            operation.put("examName", record.getExamName());
            operation.put("studentName", record.getStudentName());
            operation.put("status", record.getStatus());
            operation.put("createTime", record.getStartTime().toString());
            result.add(operation);
        }

        return ResponseDTO.success(result);
    }

    @GetMapping("/statistics/dashboard")
    public ResponseDTO<Map<String, Integer>> getDashboardStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("paperCount", examPaperService.countAll());
        stats.put("examCount", examService.countAll());
        stats.put("studentCount", examRecordService.countDistinctStudents());
        return ResponseDTO.success(stats);
    }

    @PostMapping("/{recordId}/scores")
    public ResponseDTO<Void> submitScores(
            @PathVariable Integer recordId,
            @RequestBody List<Map<String, Object>> scores) {
        examRecordService.submitScores(recordId, scores);
        return ResponseDTO.success("评分提交成功", null);
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response,
                           @RequestParam(required = false) Integer examId,
                           @RequestParam(required = false) String keyword) throws IOException {
        // 获取所有考试记录
        List<ExamRecord> records;
        if (examId != null) {
            records = examRecordService.getByExamId(examId);
        } else {
            records = examRecordService.getAll();
        }

        // 根据keyword筛选
        if (keyword != null && !keyword.isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            records = records.stream()
                    .filter(r -> (r.getStudentName() != null && r.getStudentName().toLowerCase().contains(lowerKeyword))
                            || (r.getStudentNo() != null && r.getStudentNo().toLowerCase().contains(lowerKeyword)))
                    .collect(Collectors.toList());
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("成绩导出", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 创建Excel工作簿
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             OutputStream outputStream = response.getOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("成绩数据");

            // 创建表头
            XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {"学生姓名", "学号", "考试名称", "总分", "客观题得分", "主观题得分", "考试时间", "状态"};
            for (int i = 0; i < headers.length; i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据
            for (int i = 0; i < records.size(); i++) {
                ExamRecord record = records.get(i);
                XSSFRow row = sheet.createRow(i + 1);

                row.createCell(0).setCellValue(record.getStudentName() != null ? record.getStudentName() : "");
                row.createCell(1).setCellValue(record.getStudentNo() != null ? record.getStudentNo() : "");
                row.createCell(2).setCellValue(record.getExamName() != null ? record.getExamName() : "");
                row.createCell(3).setCellValue(record.getTotalScore() != null ? record.getTotalScore() : 0);
                row.createCell(4).setCellValue(record.getObjectiveScore() != null ? record.getObjectiveScore() : 0);
                row.createCell(5).setCellValue(record.getSubjectiveScore() != null ? record.getSubjectiveScore() : 0);
                row.createCell(6).setCellValue(record.getSubmitTime() != null ? record.getSubmitTime().toString() : "");

                String statusText = "未知";
                switch (record.getStatus()) {
                    case 0: statusText = "未提交"; break;
                    case 1: statusText = "待评分"; break;
                    case 2: statusText = "已完成"; break;
                }
                row.createCell(7).setCellValue(statusText);
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        }
    }
}