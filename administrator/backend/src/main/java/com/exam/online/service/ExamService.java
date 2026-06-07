package com.exam.online.service;

import com.exam.online.entity.Exam;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.ExamMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<Exam> getByPage(Integer pageNum, Integer pageSize, String keyword, Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        List<Exam> exams = examMapper.selectByCondition(keyword, status);
        return (Page<Exam>) exams;
    }

    @Transactional
    public Exam add(Exam exam) {
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
}
