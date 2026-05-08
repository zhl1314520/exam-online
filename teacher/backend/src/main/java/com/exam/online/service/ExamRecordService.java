package com.exam.online.service;

import com.exam.online.entity.AnswerRecord;
import com.exam.online.entity.ExamRecord;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.AnswerRecordMapper;
import com.exam.online.mapper.ExamRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExamRecordService {

    @Autowired
    private ExamRecordMapper examRecordMapper;

    @Autowired
    private AnswerRecordMapper answerRecordMapper;

    public ExamRecord getById(Integer recordId) {
        return examRecordMapper.selectById(recordId);
    }

    public List<ExamRecord> getByExamId(Integer examId) {
        return examRecordMapper.selectByExamId(examId);
    }

    public List<ExamRecord> getByStudentId(Integer studentId) {
        return examRecordMapper.selectByStudentId(studentId);
    }

    public int countByExamId(Integer examId) {
        return examRecordMapper.countByExamId(examId);
    }

    public int countSubmittedByExamId(Integer examId) {
        return examRecordMapper.countSubmittedByExamId(examId);
    }

    public List<AnswerRecord> getAnswerRecords(Integer recordId) {
        return answerRecordMapper.selectByRecordId(recordId);
    }

    public Map<String, Object> getExamStatistics(Integer examId, Double passScore) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", examRecordMapper.countByExamId(examId));
        stats.put("submittedCount", examRecordMapper.countSubmittedByExamId(examId));
        stats.put("maxScore", examRecordMapper.getMaxScoreByExamId(examId));
        stats.put("minScore", examRecordMapper.getMinScoreByExamId(examId));
        stats.put("avgScore", examRecordMapper.getAvgScoreByExamId(examId));
        stats.put("passRate", examRecordMapper.getPassRateByExamId(examId, passScore));
        return stats;
    }
}