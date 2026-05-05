package com.exam.online.service;

import com.exam.online.mapper.AdminMapper;
import com.exam.online.mapper.StudentMapper;
import com.exam.online.mapper.TeacherMapper;
import com.exam.online.mapper.SystemLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private SystemLogMapper systemLogMapper;

    public Map<String, Object> getOverview() {
        Map<String, Object> result = new HashMap<>();
        result.put("adminCount", adminMapper.selectAll().size());
        result.put("studentCount", studentMapper.countAll());
        result.put("teacherCount", teacherMapper.countAll());
        result.put("logCount", systemLogMapper.countAll());
        return result;
    }

    public Map<String, Object> getUserStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalUsers", adminMapper.selectAll().size() + studentMapper.countAll() + teacherMapper.countAll());
        result.put("adminCount", adminMapper.selectAll().size());
        result.put("studentCount", studentMapper.countAll());
        result.put("teacherCount", teacherMapper.countAll());
        return result;
    }
}