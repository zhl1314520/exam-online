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

    public Subject getById(Integer subjectId) {
        return subjectMapper.selectById(subjectId);
    }

    public List<Subject> getAll() {
        return subjectMapper.selectAll();
    }

    public Page<Subject> getByPage(Integer pageNum, Integer pageSize, String keyword, Integer departmentId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Subject> subjects = subjectMapper.selectByCondition(keyword, departmentId);
        return (Page<Subject>) subjects;
    }

    public List<Subject> getByDepartmentId(Integer departmentId) {
        return subjectMapper.selectByDepartmentId(departmentId);
    }

    @Transactional
    public Subject add(Subject subject) {
        Subject existing = subjectMapper.selectByCode(subject.getSubjectCode());
        if (existing != null) {
            throw new BusinessException(400, "学科代码已存在");
        }
        subjectMapper.insert(subject);
        return subject;
    }

    @Transactional
    public Subject update(Subject subject) {
        Subject existing = subjectMapper.selectById(subject.getSubjectId());
        if (existing == null) {
            throw new BusinessException(404, "学科不存在");
        }

        Subject sameCode = subjectMapper.selectByCode(subject.getSubjectCode());
        if (sameCode != null && !sameCode.getSubjectId().equals(subject.getSubjectId())) {
            throw new BusinessException(400, "学科代码已存在");
        }

        subjectMapper.updateById(subject);
        return subjectMapper.selectById(subject.getSubjectId());
    }

    @Transactional
    public void delete(Integer subjectId) {
        Subject existing = subjectMapper.selectById(subjectId);
        if (existing == null) {
            throw new BusinessException(404, "学科不存在");
        }
        subjectMapper.deleteById(subjectId);
    }
}
