package com.exam.online.service;

import com.exam.online.entity.Class;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.ClassMapper;
import com.exam.online.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private StudentMapper studentMapper;

    public Class getById(Integer classId) {
        return classMapper.selectById(classId);
    }

    public List<Class> getAll() {
        return classMapper.selectAll();
    }

    public Page<Class> getByPage(Integer pageNum, Integer pageSize, String keyword, Integer departmentId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Class> classes = classMapper.selectByCondition(keyword, departmentId);
        return (Page<Class>) classes;
    }

    @Transactional
    public Class add(Class clazz) {
        Class existing = classMapper.selectByClassCode(clazz.getClassCode());
        if (existing != null) {
            throw new BusinessException(400, "班级代码已存在");
        }
        clazz.setStudentCount(0);
        classMapper.insert(clazz);
        return clazz;
    }

    @Transactional
    public Class update(Class clazz) {
        Class existing = classMapper.selectById(clazz.getClassId());
        if (existing == null) {
            throw new BusinessException(404, "班级不存在");
        }

        Class sameCode = classMapper.selectByClassCode(clazz.getClassCode());
        if (sameCode != null && !sameCode.getClassId().equals(clazz.getClassId())) {
            throw new BusinessException(400, "班级代码已存在");
        }

        classMapper.updateById(clazz);
        return classMapper.selectById(clazz.getClassId());
    }

    @Transactional
    public void delete(Integer classId) {
        Class existing = classMapper.selectById(classId);
        if (existing == null) {
            throw new BusinessException(404, "班级不存在");
        }

        int studentCount = studentMapper.countByClassId(classId);
        if (studentCount > 0) {
            throw new BusinessException(400, "班级下存在学生，无法删除");
        }

        classMapper.deleteById(classId);
    }

    public List<Class> getByDepartmentId(Integer departmentId) {
        return classMapper.selectByDepartmentId(departmentId);
    }
}