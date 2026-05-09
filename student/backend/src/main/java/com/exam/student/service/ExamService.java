package com.exam.student.service;

import com.exam.student.entity.Exam;
import com.exam.student.entity.Student;
import com.exam.student.mapper.ExamMapper;
import com.exam.student.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 获取待考考试列表
     */
    public List<Exam> getPendingExams(Integer studentId) {
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            return List.of();
        }
        return examMapper.selectPendingExamsByClassId(student.getClassId());
    }

    /**
     * 获取考试详情
     */
    public Exam getExamDetail(Integer examId) {
        return examMapper.selectById(examId);
    }

    /**
     * 获取所有考试（包括已结束）
     */
    public List<Exam> getAllExams(Integer studentId) {
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            return List.of();
        }
        return examMapper.selectExamsByClassId(student.getClassId());
    }
}
