package com.exam.online.service;

import com.exam.online.entity.AnswerRecord;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.AnswerRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerRecordService {

    @Autowired
    private AnswerRecordMapper answerRecordMapper;

    public AnswerRecord getAnswerRecordById(Integer answerId) {
        return answerRecordMapper.selectById(answerId);
    }

    public List<AnswerRecord> getAllAnswerRecords() {
        return answerRecordMapper.selectAll();
    }

    public List<AnswerRecord> getAnswerRecordsByRecordId(Integer recordId) {
        return answerRecordMapper.selectByRecordId(recordId);
    }

    public List<AnswerRecord> getAnswerRecordsByQuestionId(Integer questionId) {
        return answerRecordMapper.selectByQuestionId(questionId);
    }

    public AnswerRecord getAnswerRecordByRecordIdAndQuestionId(Integer recordId, Integer questionId) {
        return answerRecordMapper.selectByRecordIdAndQuestionId(recordId, questionId);
    }

    @Transactional
    public AnswerRecord addAnswerRecord(AnswerRecord answerRecord) {
        answerRecord.setAnswerTime(LocalDateTime.now());
        answerRecordMapper.insert(answerRecord);
        return answerRecord;
    }

    @Transactional
    public AnswerRecord updateAnswerRecord(AnswerRecord answerRecord) {
        AnswerRecord existing = answerRecordMapper.selectById(answerRecord.getAnswerId());
        if (existing == null) {
            throw new BusinessException(404, "答题记录不存在");
        }
        answerRecordMapper.updateById(answerRecord);
        return answerRecordMapper.selectById(answerRecord.getAnswerId());
    }

    @Transactional
    public void deleteAnswerRecord(Integer answerId) {
        AnswerRecord existing = answerRecordMapper.selectById(answerId);
        if (existing == null) {
            throw new BusinessException(404, "答题记录不存在");
        }
        answerRecordMapper.deleteById(answerId);
    }

    @Transactional
    public void deleteAnswerRecordsByRecordId(Integer recordId) {
        answerRecordMapper.deleteByRecordId(recordId);
    }

    public int getAnswerRecordCountByRecordId(Integer recordId) {
        return answerRecordMapper.countByRecordId(recordId);
    }

    public Double getTotalScoreByRecordId(Integer recordId) {
        return answerRecordMapper.selectTotalScoreByRecordId(recordId);
    }
}
