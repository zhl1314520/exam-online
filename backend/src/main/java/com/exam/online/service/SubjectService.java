package com.exam.online.service;

import com.exam.online.entity.Subject;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.SubjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectMapper subjectMapper;

    public Subject getSubjectById(Integer subjectId) {
        return subjectMapper.selectById(subjectId);
    }

    public Subject getSubjectByCode(String subjectCode) {
        return subjectMapper.selectBySubjectCode(subjectCode);
    }

    public List<Subject> getAllSubjects() {
        return subjectMapper.selectAll();
    }

    public Page<Subject> getSubjectsByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Subject>) subjectMapper.selectAll();
    }

    public List<Subject> getSubjectsByDepartmentId(Integer departmentId) {
        return subjectMapper.selectByDepartmentId(departmentId);
    }

    @Transactional
    public Subject addSubject(Subject subject) {
        Subject existing = subjectMapper.selectBySubjectCode(subject.getSubjectCode());
        if (existing != null) {
            throw new BusinessException(400, "课程代码已存在");
        }
        subjectMapper.insert(subject);
        return subject;
    }

    @Transactional
    public Subject updateSubject(Subject subject) {
        Subject existing = subjectMapper.selectById(subject.getSubjectId());
        if (existing == null) {
            throw new BusinessException(404, "课程不存在");
        }
        Subject sameCode = subjectMapper.selectBySubjectCode(subject.getSubjectCode());
        if (sameCode != null && !sameCode.getSubjectId().equals(subject.getSubjectId())) {
            throw new BusinessException(400, "课程代码已存在");
        }
        subjectMapper.updateById(subject);
        return subjectMapper.selectById(subject.getSubjectId());
    }

    @Transactional
    public void deleteSubject(Integer subjectId) {
        Subject existing = subjectMapper.selectById(subjectId);
        if (existing == null) {
            throw new BusinessException(404, "课程不存在");
        }
        subjectMapper.deleteById(subjectId);
    }

    public int getSubjectCount() {
        return subjectMapper.countAll();
    }

    public int getSubjectCountByDepartmentId(Integer departmentId) {
        return subjectMapper.countByDepartmentId(departmentId);
    }
}
