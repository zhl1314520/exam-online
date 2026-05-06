package com.exam.online.service;

import com.exam.online.entity.Exam;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.ExamMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamMapper examMapper;

    public Exam getExamById(Integer examId) {
        return examMapper.selectById(examId);
    }

    public List<Exam> getAllExams() {
        return examMapper.selectAll();
    }

    public Page<Exam> getExamsByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Exam>) examMapper.selectAll();
    }

    public List<Exam> getExamsByClassId(Integer classId) {
        return examMapper.selectByClassId(classId);
    }

    public List<Exam> getExamsByPaperId(Integer paperId) {
        return examMapper.selectByPaperId(paperId);
    }

    public List<Exam> getExamsByCreateBy(Integer createBy) {
        return examMapper.selectByCreateBy(createBy);
    }

    public List<Exam> getExamsByStatus(Integer status) {
        return examMapper.selectByStatus(status);
    }

    @Transactional
    public Exam addExam(Exam exam) {
        exam.setCreateTime(LocalDateTime.now());
        exam.setStatus(0);
        examMapper.insert(exam);
        return exam;
    }

    @Transactional
    public Exam updateExam(Exam exam) {
        Exam existing = examMapper.selectById(exam.getExamId());
        if (existing == null) {
            throw new BusinessException(404, "考试不存在");
        }
        examMapper.updateById(exam);
        return examMapper.selectById(exam.getExamId());
    }

    @Transactional
    public void deleteExam(Integer examId) {
        Exam existing = examMapper.selectById(examId);
        if (existing == null) {
            throw new BusinessException(404, "考试不存在");
        }
        examMapper.deleteById(examId);
    }

    public int getExamCount() {
        return examMapper.countAll();
    }

    public int getExamCountByClassId(Integer classId) {
        return examMapper.countByClassId(classId);
    }

    public int getExamCountByStatus(Integer status) {
        return examMapper.countByStatus(status);
    }
}
