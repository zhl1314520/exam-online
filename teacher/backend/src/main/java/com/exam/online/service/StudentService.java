package com.exam.online.service;

import com.exam.online.entity.Student;
import com.exam.online.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    public Student getById(Integer studentId) {
        return studentMapper.selectById(studentId);
    }

    public List<Student> getAll() {
        return studentMapper.selectAll();
    }

    public List<Student> getByClassId(Integer classId) {
        return studentMapper.selectByClassId(classId);
    }

    public int countAll() {
        return studentMapper.countAll();
    }

    public int countByClassId(Integer classId) {
        return studentMapper.countByClassId(classId);
    }
}