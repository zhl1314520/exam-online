package com.exam.online.service;

import com.exam.online.mapper.AdminMapper;
import com.exam.online.mapper.StudentMapper;
import com.exam.online.mapper.TeacherMapper;
import com.exam.online.mapper.SystemLogMapper;
import com.exam.online.mapper.ClassMapper;
import com.exam.online.mapper.DepartmentMapper;
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

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    public Map<String, Object> getOverview() {
        Map<String, Object> result = new HashMap<>();

        int adminCount = adminMapper.selectAll().size();
        int studentCount = studentMapper.countAll();
        int teacherCount = teacherMapper.countAll();

        result.put("totalUsers", adminCount + studentCount + teacherCount);
        result.put("studentCount", studentCount);
        result.put("teacherCount", teacherCount);
        result.put("examCount", 0);
        result.put("participantCount", 0);
        result.put("departmentCount", departmentMapper.selectAll().size());
        result.put("classCount", classMapper.selectAll().size());

        return result;
    }

    public Map<String, Object> getUserStatistics() {
        Map<String, Object> result = new HashMap<>();
        int adminCount = adminMapper.selectAll().size();
        int studentCount = studentMapper.countAll();
        int teacherCount = teacherMapper.countAll();

        result.put("totalUsers", adminCount + studentCount + teacherCount);
        result.put("adminCount", adminCount);
        result.put("studentCount", studentCount);
        result.put("teacherCount", teacherCount);

        // 添加院系统计数据
        result.put("departmentStats", departmentMapper.selectDepartmentStatistics());

        return result;
    }
}