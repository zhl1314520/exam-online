package com.exam.online.service;

import com.exam.online.entity.Department;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.DepartmentMapper;
import com.exam.online.mapper.ClassMapper;
import com.exam.online.mapper.TeacherMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    public Department getById(Integer departmentId) {
        return departmentMapper.selectById(departmentId);
    }

    public List<Department> getAll() {
        return departmentMapper.selectAll();
    }

    public Page<Department> getByPage(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Department>) departmentMapper.selectByKeyword(keyword);
    }

    @Transactional
    public Department add(Department department) {
        Department existing = departmentMapper.selectByCode(department.getDepartmentCode());
        if (existing != null) {
            throw new BusinessException(400, "院系代码已存在");
        }
        departmentMapper.insert(department);
        return department;
    }

    @Transactional
    public Department update(Department department) {
        Department existing = departmentMapper.selectById(department.getDepartmentId());
        if (existing == null) {
            throw new BusinessException(404, "院系不存在");
        }

        Department sameCode = departmentMapper.selectByCode(department.getDepartmentCode());
        if (sameCode != null && !sameCode.getDepartmentId().equals(department.getDepartmentId())) {
            throw new BusinessException(400, "院系代码已存在");
        }

        departmentMapper.updateById(department);
        return departmentMapper.selectById(department.getDepartmentId());
    }

    @Transactional
    public void delete(Integer departmentId) {
        Department existing = departmentMapper.selectById(departmentId);
        if (existing == null) {
            throw new BusinessException(404, "院系不存在");
        }

        List<?> classes = classMapper.selectByDepartmentId(departmentId);
        if (!classes.isEmpty()) {
            throw new BusinessException(400, "院系下存在班级，无法删除");
        }

        int teacherCount = teacherMapper.countByDepartmentId(departmentId);
        if (teacherCount > 0) {
            throw new BusinessException(400, "院系下存在教师，无法删除");
        }

        departmentMapper.deleteById(departmentId);
    }
}