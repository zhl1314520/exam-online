package com.exam.student.service;

import com.exam.student.dto.PageDTO;
import com.exam.student.dto.PageQueryDTO;
import com.exam.student.entity.AnswerRecord;
import com.exam.student.entity.ExamRecord;
import com.exam.student.mapper.AnswerRecordMapper;
import com.exam.student.mapper.ExamRecordMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamRecordService {

    @Autowired
    private ExamRecordMapper examRecordMapper;

    @Autowired
    private AnswerRecordMapper answerRecordMapper;

    /**
     * 获取考试记录列表
     */
    public PageDTO<ExamRecord> getExamRecords(Integer studentId, PageQueryDTO query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        Page<ExamRecord> page = (Page<ExamRecord>) examRecordMapper.selectByStudentIdWithExam(studentId);
        return PageDTO.of(page);
    }

    /**
     * 获取答卷详情
     */
    public ExamRecord getExamRecordDetail(Integer recordId, Integer studentId) {
        ExamRecord record = examRecordMapper.selectById(recordId);
        if (record != null && record.getStudentId().equals(studentId)) {
            List<AnswerRecord> answers = answerRecordMapper.selectByRecordId(recordId);
            record.setAnswers(answers);
            return record;
        }
        return null;
    }

    /**
     * 获取所有考试记录（不分页）
     */
    public List<ExamRecord> getAllExamRecords(Integer studentId) {
        return examRecordMapper.selectByStudentIdWithExam(studentId);
    }
}
