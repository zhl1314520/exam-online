package com.exam.online.service;

import com.exam.online.entity.Exam;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.ExamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamMapper examMapper;

    public Exam getById(Integer examId) {
        return examMapper.selectById(examId);
    }

    public List<Exam> getAll() {
        return examMapper.selectAll();
    }

    public List<Exam> getByCreateBy(Integer createBy) {
        return examMapper.selectByCreateBy(createBy);
    }

    public List<Exam> getByClassId(Integer classId) {
        return examMapper.selectByClassId(classId);
    }

    public int countAll() {
        return examMapper.countAll();
    }

    public int countByStatus(Integer status) {
        return examMapper.countByStatus(status);
    }

    public List<Exam> getByDateRange(LocalDateTime startTime, LocalDateTime endTime, Integer createBy) {
        return examMapper.selectByDateRange(startTime, endTime, createBy);
    }

    public List<Exam> getRecentExams(Integer limit, Integer createBy) {
        return examMapper.selectRecentExams(limit, createBy);
    }

    @Transactional
    public Exam add(Exam exam) {
        exam.setStatus(0);
        examMapper.insert(exam);
        return exam;
    }

    @Transactional
    public Exam update(Exam exam) {
        Exam existing = examMapper.selectById(exam.getExamId());
        if (existing == null) {
            throw new BusinessException(404, "考试不存在");
        }
        examMapper.updateById(exam);
        return examMapper.selectById(exam.getExamId());
    }

    @Transactional
    public void delete(Integer examId) {
        Exam existing = examMapper.selectById(examId);
        if (existing == null) {
            throw new BusinessException(404, "考试不存在");
        }
        examMapper.deleteById(examId);
    }

    @Transactional
    public void publishExam(Integer examId) {
        Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            throw new BusinessException(404, "考试不存在");
        }
        exam.setStatus(1);
        examMapper.updateById(exam);
    }

    @Transactional
    public void startExam(Integer examId) {
        Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            throw new BusinessException(404, "考试不存在");
        }
        exam.setStatus(2);
        examMapper.updateById(exam);
    }

    @Transactional
    public void endExam(Integer examId) {
        Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            throw new BusinessException(404, "考试不存在");
        }
        exam.setStatus(3);
        examMapper.updateById(exam);
    }
}