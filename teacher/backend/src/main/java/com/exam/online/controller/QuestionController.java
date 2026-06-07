package com.exam.online.controller;

import com.exam.online.annotation.OperationLog;
import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Question;
import com.exam.online.service.QuestionParseService;
import com.exam.online.service.QuestionService;
import com.exam.online.utils.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionParseService questionParseService;

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
    public ResponseDTO<PageDTO<Question>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer subjectId,
            @RequestParam(required = false) Integer questionType,
            @RequestParam(required = false) Integer difficulty) {

        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionService.selectByConditions(keyword, subjectId, questionType, difficulty);
        PageInfo<Question> pageInfo = new PageInfo<>(questions);

        PageDTO<Question> pageDTO = new PageDTO<>();
        pageDTO.setTotal(pageInfo.getTotal());
        pageDTO.setPageNum(pageInfo.getPageNum());
        pageDTO.setPageSize(pageInfo.getPageSize());
        pageDTO.setPages(pageInfo.getPages());
        pageDTO.setList(pageInfo.getList());
        return ResponseDTO.success(pageDTO);
    }

    @GetMapping("/list")
    public ResponseDTO<List<Question>> getAll() {
        return ResponseDTO.success(questionService.getAll());
    }

    @GetMapping("/statistics")
    public ResponseDTO<java.util.Map<String, Integer>> getStatistics() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        stats.put("total", questionService.countAll());
        stats.put("single", questionService.countByQuestionType(1));
        stats.put("multiple", questionService.countByQuestionType(2));
        stats.put("judge", questionService.countByQuestionType(3));
        stats.put("fill", questionService.countByQuestionType(4));
        stats.put("short", questionService.countByQuestionType(5));
        stats.put("code", questionService.countByQuestionType(6));
        return ResponseDTO.success(stats);
    }

    @GetMapping("/trend")
    public ResponseDTO<java.util.List<java.util.Map<String, Object>>> getQuestionTrend() {
        return ResponseDTO.success(questionService.getQuestionTrendLast30Days());
    }

    @GetMapping("/{id:\\d+}")
    public ResponseDTO<Question> getById(@PathVariable Integer id) {
        Question question = questionService.getById(id);
        if (question == null) {
            return ResponseDTO.error(404, "题目不存在");
        }
        return ResponseDTO.success(question);
    }

    @PostMapping("/add")
    @OperationLog(operation = "创建题目")
    public ResponseDTO<Question> add(@RequestBody Question question, HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        question.setCreateBy(teacherId);
        Question created = questionService.add(question);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    @OperationLog(operation = "更新题目")
    public ResponseDTO<Question> update(@RequestBody Question question) {
        Question updated = questionService.update(question);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    @OperationLog(operation = "删除题目")
    public ResponseDTO<Void> delete(@PathVariable Integer id) {
        questionService.delete(id);
        return ResponseDTO.success("删除成功", null);
    }

    @PostMapping("/batch/import")
    @OperationLog(operation = "批量导入题目")
    public ResponseDTO<Integer> batchImport(@RequestBody List<Question> questions, HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        int count = questionService.batchImport(questions, teacherId);
        return ResponseDTO.success("批量导入成功", count);
    }

    @PostMapping("/parse/excel")
    public ResponseDTO<List<Question>> parseExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return ResponseDTO.error(400, "仅支持 Excel 文件 (.xlsx, .xls)");
            }
            List<Question> questions = questionParseService.parseExcelFile(file);
            return ResponseDTO.success(questions);
        } catch (Exception e) {
            return ResponseDTO.error(500, "Excel 解析失败: " + e.getMessage());
        }
    }

    @PostMapping("/parse/word")
    public ResponseDTO<List<Question>> parseWordFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.endsWith(".docx")) {
                return ResponseDTO.error(400, "仅支持 Word 文件 (.docx)");
            }
            List<Question> questions = questionParseService.parseWordFile(file);
            return ResponseDTO.success(questions);
        } catch (Exception e) {
            return ResponseDTO.error(500, "Word 解析失败: " + e.getMessage());
        }
    }

    @GetMapping("/template/excel")
    public void downloadExcelTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=question_import_template.xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("题目导入模板");

            String[] headers = {"subjectId", "questionType", "questionContent", "options", "correctAnswer", "score", "difficulty", "knowledgePoint"};

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            String[] headerDescs = {"科目ID（数字）", "题型（1单选 2多选 3判断 4填空 5简答 6编程）", "题目内容", "选项（换行分隔）", "正确答案", "分值", "难度（1简单 2中等 3困难）", "知识点"};
            Row descRow = sheet.createRow(1);
            CellStyle descStyle = workbook.createCellStyle();
            Font descFont = workbook.createFont();
            descFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
            descFont.setItalic(true);
            descStyle.setFont(descFont);
            for (int i = 0; i < headerDescs.length; i++) {
                Cell cell = descRow.createCell(i);
                cell.setCellValue(headerDescs[i]);
                cell.setCellStyle(descStyle);
            }

            Row sampleRow = sheet.createRow(2);
            sampleRow.createCell(0).setCellValue(1);
            sampleRow.createCell(1).setCellValue(1);
            sampleRow.createCell(2).setCellValue("示例题目内容");
            sampleRow.createCell(3).setCellValue("A.选项A\nB.选项B\nC.选项C\nD.选项D");
            sampleRow.createCell(4).setCellValue("A");
            sampleRow.createCell(5).setCellValue(2);
            sampleRow.createCell(6).setCellValue(2);
            sampleRow.createCell(7).setCellValue("示例知识点");

            Row sampleRow2 = sheet.createRow(3);
            sampleRow2.createCell(0).setCellValue(1);
            sampleRow2.createCell(1).setCellValue(3);
            sampleRow2.createCell(2).setCellValue("这是一个判断题示例");
            sampleRow2.createCell(3).setCellValue("");
            sampleRow2.createCell(4).setCellValue("1");
            sampleRow2.createCell(5).setCellValue(1);
            sampleRow2.createCell(6).setCellValue(1);
            sampleRow2.createCell(7).setCellValue("判断题知识点");

            for (int i = 0; i < headers.length; i++) {
                sheet.setColumnWidth(i, 5000);
            }

            workbook.write(response.getOutputStream());
        }
    }

    @GetMapping("/template/word")
    public void downloadWordTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=question_import_template.docx");

        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph titlePara = document.createParagraph();
            titlePara.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = titlePara.createRun();
            titleRun.setText("题目导入模板");
            titleRun.setBold(true);
            titleRun.setFontSize(18);

            XWPFParagraph notePara = document.createParagraph();
            XWPFRun noteRun = notePara.createRun();
            noteRun.setText("说明：每道题以'题型：'开头，题目之间用空行分隔。题型取值：1单选 2多选 3判断 4填空 5简答 6编程；难度取值：1简单 2中等 3困难；判断题答案填 0（错误）或 1（正确）。");
            noteRun.setFontSize(10);
            noteRun.setColor("888888");

            document.createParagraph().createRun().addCarriageReturn();

            String[] templateLines = {
                    "题型：1",
                    "题目：示例单选题内容",
                    "A. 选项A",
                    "B. 选项B",
                    "C. 选项C",
                    "D. 选项D",
                    "答案：A",
                    "分值：2",
                    "难度：2",
                    "知识点：示例知识点"
            };
            for (String line : templateLines) {
                XWPFParagraph p = document.createParagraph();
                XWPFRun r = p.createRun();
                r.setText(line);
                r.setFontSize(12);
            }

            document.createParagraph().createRun().addCarriageReturn();

            String[] templateLines2 = {
                    "题型：3",
                    "题目：这是一个判断题示例",
                    "答案：1",
                    "分值：1",
                    "难度：1",
                    "知识点：判断题知识点"
            };
            for (String line : templateLines2) {
                XWPFParagraph p = document.createParagraph();
                XWPFRun r = p.createRun();
                r.setText(line);
                r.setFontSize(12);
            }

            document.createParagraph().createRun().addCarriageReturn();

            String[] templateLines3 = {
                    "题型：4",
                    "题目：这是一个填空题示例，空格用___表示",
                    "答案：填空答案",
                    "分值：2",
                    "难度：2",
                    "知识点：填空题知识点"
            };
            for (String line : templateLines3) {
                XWPFParagraph p = document.createParagraph();
                XWPFRun r = p.createRun();
                r.setText(line);
                r.setFontSize(12);
            }

            document.write(response.getOutputStream());
        }
    }
}
