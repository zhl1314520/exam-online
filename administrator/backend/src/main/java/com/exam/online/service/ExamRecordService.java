package com.exam.online.service;

import com.exam.online.entity.ExamRecord;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.ExamRecordMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamRecordService {

    @Autowired
    private ExamRecordMapper examRecordMapper;

    public ExamRecord getExamRecordById(Integer recordId) {
        return examRecordMapper.selectById(recordId);
    }

    public List<ExamRecord> getAllExamRecords() {
        return examRecordMapper.selectAll();
    }

    public Page<ExamRecord> getExamRecordsByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<ExamRecord>) examRecordMapper.selectAll();
    }

    public List<ExamRecord> getExamRecordsByExamId(Integer examId) {
        return examRecordMapper.selectByExamId(examId);
    }

    public List<ExamRecord> getExamRecordsByStudentId(Integer studentId) {
        return examRecordMapper.selectByStudentId(studentId);
    }

    public ExamRecord getExamRecordByExamIdAndStudentId(Integer examId, Integer studentId) {
        return examRecordMapper.selectByExamIdAndStudentId(examId, studentId);
    }

    @Transactional
    public ExamRecord startExam(Integer examId, Integer studentId, String ipAddress) {
        ExamRecord existing = examRecordMapper.selectByExamIdAndStudentId(examId, studentId);
        if (existing != null) {
            throw new BusinessException(400, "您已参加该考试");
        }
        ExamRecord record = new ExamRecord();
        record.setExamId(examId);
        record.setStudentId(studentId);
        record.setStartTime(LocalDateTime.now());
        record.setStatus(0);
        record.setScreenSwitchCount(0);
        record.setIpAddress(ipAddress);
        examRecordMapper.insert(record);
        return record;
    }

    @Transactional
    public ExamRecord submitExam(ExamRecord examRecord) {
        ExamRecord existing = examRecordMapper.selectById(examRecord.getRecordId());
        if (existing == null) {
            throw new BusinessException(404, "考试记录不存在");
        }
        examRecord.setSubmitTime(LocalDateTime.now());
        examRecord.setStatus(1);
        examRecordMapper.updateById(examRecord);
        return examRecordMapper.selectById(examRecord.getRecordId());
    }

    @Transactional
    public void deleteExamRecord(Integer recordId) {
        ExamRecord existing = examRecordMapper.selectById(recordId);
        if (existing == null) {
            throw new BusinessException(404, "考试记录不存在");
        }
        examRecordMapper.deleteById(recordId);
    }

    public int getExamRecordCount() {
        return examRecordMapper.countAll();
    }

    public int getExamRecordCountByExamId(Integer examId) {
        return examRecordMapper.countByExamId(examId);
    }

    public int getExamRecordCountByStudentId(Integer studentId) {
        return examRecordMapper.countByStudentId(studentId);
    }

    public Double getAverageScoreByExamId(Integer examId) {
        return examRecordMapper.selectAverageScoreByExamId(examId);
    }

    public Double getMaxScoreByExamId(Integer examId) {
        return examRecordMapper.selectMaxScoreByExamId(examId);
    }

    public Double getMinScoreByExamId(Integer examId) {
        return examRecordMapper.selectMinScoreByExamId(examId);
    }
}
