package com.exam.online.service;

import com.exam.online.entity.Teacher;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.TeacherMapper;
import com.exam.online.utils.PasswordUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    public Teacher getById(Integer teacherId) {
        return teacherMapper.selectById(teacherId);
    }

    public List<Teacher> getAll() {
        return teacherMapper.selectAll();
    }

    public Page<Teacher> getByPage(Integer pageNum, Integer pageSize, String keyword, Integer departmentId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Teacher> teachers = teacherMapper.selectByCondition(keyword, departmentId);
        return (Page<Teacher>) teachers;
    }

    public int countAll() {
        return teacherMapper.countAll();
    }

    @Transactional
    public Teacher add(Teacher teacher) {
        Teacher existing = teacherMapper.selectByTeacherNo(teacher.getTeacherNo());
        if (existing != null) {
            throw new BusinessException(400, "工号已存在");
        }
        String password = teacher.getPassword();
        if (password == null || password.isEmpty()) {
            password = "123456";
        }
        teacher.setPassword(PasswordUtil.md5(password));
        teacher.setStatus(1);
        teacherMapper.insert(teacher);
        return teacher;
    }

    @Transactional
    public void batchAdd(List<Teacher> teachers) {
        for (Teacher teacher : teachers) {
            add(teacher);
        }
    }

    @Transactional
    public Teacher update(Teacher teacher) {
        Teacher existing = teacherMapper.selectById(teacher.getTeacherId());
        if (existing == null) {
            throw new BusinessException(404, "教师不存在");
        }

        Teacher sameNo = teacherMapper.selectByTeacherNo(teacher.getTeacherNo());
        if (sameNo != null && !sameNo.getTeacherId().equals(teacher.getTeacherId())) {
            throw new BusinessException(400, "工号已存在");
        }

        teacherMapper.updateById(teacher);
        return teacherMapper.selectById(teacher.getTeacherId());
    }

    @Transactional
    public void delete(Integer teacherId) {
        Teacher existing = teacherMapper.selectById(teacherId);
        if (existing == null) {
            throw new BusinessException(404, "教师不存在");
        }
        teacherMapper.deleteById(teacherId);
    }
}