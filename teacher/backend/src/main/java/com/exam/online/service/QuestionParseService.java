package com.exam.online.service;

import com.exam.online.entity.Question;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionParseService {

    private static final Map<String, Integer> QUESTION_TYPE_MAP = new HashMap<>();
    static {
        QUESTION_TYPE_MAP.put("单选题", 1);
        QUESTION_TYPE_MAP.put("多选题", 2);
        QUESTION_TYPE_MAP.put("判断题", 3);
        QUESTION_TYPE_MAP.put("填空题", 4);
        QUESTION_TYPE_MAP.put("简答题", 5);
        QUESTION_TYPE_MAP.put("编程题", 6);
    }

    private static final Map<String, Integer> DIFFICULTY_MAP = new HashMap<>();
    static {
        DIFFICULTY_MAP.put("简单", 1);
        DIFFICULTY_MAP.put("中等", 2);
        DIFFICULTY_MAP.put("困难", 3);
    }

    public List<Question> parseExcelFile(MultipartFile file) throws IOException, InvalidFormatException {
        List<Question> questions = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return questions;

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return questions;

            Map<String, Integer> columnIndex = new HashMap<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                String header = getCellStringValue(headerRow.getCell(i));
                columnIndex.put(header, i);
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Question question = new Question();
                question.setSubjectId(getCellIntValue(row, columnIndex.get("subjectId")));
                question.setQuestionType(getCellIntValue(row, columnIndex.get("questionType")));
                question.setQuestionContent(getCellStringValue(row.getCell(columnIndex.get("questionContent"))));
                question.setOptions(getCellStringValue(row.getCell(columnIndex.get("options"))));
                question.setCorrectAnswer(getCellStringValue(row.getCell(columnIndex.get("correctAnswer"))));
                question.setScore(getCellDoubleValue(row, columnIndex.get("score")));
                question.setDifficulty(getCellIntValue(row, columnIndex.get("difficulty")));
                question.setKnowledgePoint(getCellStringValue(row.getCell(columnIndex.get("knowledgePoint"))));

                if (question.getQuestionContent() != null && !question.getQuestionContent().isEmpty()) {
                    if (question.getScore() == null || question.getScore() <= 0) question.setScore(2.0);
                    if (question.getDifficulty() == null) question.setDifficulty(2);
                    questions.add(question);
                }
            }
        }

        return questions;
    }

    public List<Question> parseWordFile(MultipartFile file) throws IOException {
        List<Question> questions = new ArrayList<>();

        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            Question currentQuestion = null;
            StringBuilder optionsBuilder = new StringBuilder();
            char optionChar = 'A';

            for (XWPFParagraph para : paragraphs) {
                String text = para.getText().trim();
                if (text.isEmpty()) {
                    if (currentQuestion != null && optionsBuilder.length() > 0) {
                        currentQuestion.setOptions(optionsBuilder.toString());
                        optionsBuilder.setLength(0);
                        optionChar = 'A';
                    }
                    continue;
                }

                if (text.startsWith("题型：") || text.startsWith("题型:")) {
                    if (currentQuestion != null) {
                        if (optionsBuilder.length() > 0) {
                            currentQuestion.setOptions(optionsBuilder.toString());
                            optionsBuilder.setLength(0);
                            optionChar = 'A';
                        }
                        questions.add(currentQuestion);
                    }
                    currentQuestion = new Question();
                    String typeStr = text.substring(3).trim();
                    currentQuestion.setQuestionType(QUESTION_TYPE_MAP.getOrDefault(typeStr, 1));
                    currentQuestion.setScore(2.0);
                    currentQuestion.setDifficulty(2);
                    optionChar = 'A';
                } else if (text.startsWith("题目：") || text.startsWith("题目:") || text.startsWith("内容：") || text.startsWith("内容:")) {
                    if (currentQuestion != null) {
                        currentQuestion.setQuestionContent(text.substring(text.indexOf("：") > 0 ? text.indexOf("：") + 1 : text.indexOf(":") + 1).trim());
                    }
                } else if (text.startsWith("答案：") || text.startsWith("答案:") || text.startsWith("正确答案：") || text.startsWith("正确答案:")) {
                    if (currentQuestion != null) {
                        currentQuestion.setCorrectAnswer(text.substring(text.indexOf("：") > 0 ? text.indexOf("：") + 1 : text.indexOf(":") + 1).trim());
                    }
                } else if (text.startsWith("分值：") || text.startsWith("分值:") || text.startsWith("分数：") || text.startsWith("分数:")) {
                    if (currentQuestion != null) {
                        try {
                            currentQuestion.setScore(Double.parseDouble(text.substring(text.indexOf("：") > 0 ? text.indexOf("：") + 1 : text.indexOf(":") + 1).trim()));
                        } catch (NumberFormatException e) {
                            currentQuestion.setScore(2.0);
                        }
                    }
                } else if (text.startsWith("难度：") || text.startsWith("难度:")) {
                    if (currentQuestion != null) {
                        String diffStr = text.substring(text.indexOf("：") > 0 ? text.indexOf("：") + 1 : text.indexOf(":") + 1).trim();
                        currentQuestion.setDifficulty(DIFFICULTY_MAP.getOrDefault(diffStr, 2));
                    }
                } else if (text.startsWith("知识点：") || text.startsWith("知识点:")) {
                    if (currentQuestion != null) {
                        currentQuestion.setKnowledgePoint(text.substring(text.indexOf("：") > 0 ? text.indexOf("：") + 1 : text.indexOf(":") + 1).trim());
                    }
                } else if (currentQuestion != null && (text.matches("^[A-D][.、].*") || text.matches("^[A-D][.、].*"))) {
                    if (optionsBuilder.length() > 0) optionsBuilder.append("\n");
                    optionsBuilder.append(text);
                    optionChar++;
                }
            }

            if (currentQuestion != null) {
                if (optionsBuilder.length() > 0) {
                    currentQuestion.setOptions(optionsBuilder.toString());
                }
                questions.add(currentQuestion);
            }
        }

        return questions;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC:
                double val = cell.getNumericCellValue();
                if (val == (long) val) return String.valueOf((long) val);
                return String.valueOf(val);
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return null;
        }
    }

    private Integer getCellIntValue(Row row, Integer colIndex) {
        if (colIndex == null) return null;
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;
        try {
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            String str = getCellStringValue(cell);
            if (str == null) return null;
            try { return Integer.parseInt(str); } catch (NumberFormatException ex) { return null; }
        }
    }

    private Double getCellDoubleValue(Row row, Integer colIndex) {
        if (colIndex == null) return null;
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            String str = getCellStringValue(cell);
            if (str == null) return null;
            try { return Double.parseDouble(str); } catch (NumberFormatException ex) { return null; }
        }
    }
}
