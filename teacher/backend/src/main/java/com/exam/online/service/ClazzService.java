package com.exam.online.service;

import com.exam.online.entity.Clazz;
import com.exam.online.mapper.ClazzMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    public Clazz getById(Integer classId) {
        return clazzMapper.selectById(classId);
    }

    public List<Clazz> getAll() {
        return clazzMapper.selectAll();
    }

    public List<Clazz> getByDepartmentId(Integer departmentId) {
        return clazzMapper.selectByDepartmentId(departmentId);
    }

    public int countAll() {
        return clazzMapper.countAll();
    }
}
