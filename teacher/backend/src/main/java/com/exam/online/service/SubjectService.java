package com.exam.online.service;

import com.exam.online.entity.Subject;
import com.exam.online.mapper.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectMapper subjectMapper;

    public Subject getById(Integer subjectId) {
        return subjectMapper.selectById(subjectId);
    }

    public List<Subject> getAll() {
        return subjectMapper.selectAll();
    }
}