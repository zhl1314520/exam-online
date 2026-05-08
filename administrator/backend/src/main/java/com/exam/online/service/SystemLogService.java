package com.exam.online.service;

import com.exam.online.entity.SystemLog;
import com.exam.online.mapper.SystemLogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemLogService {

    @Autowired
    private SystemLogMapper systemLogMapper;

    public SystemLog getById(Integer logId) {
        return systemLogMapper.selectById(logId);
    }

    public List<SystemLog> getAll() {
        return systemLogMapper.selectAll();
    }

    public Page<SystemLog> getByPage(Integer pageNum, Integer pageSize, Integer userType, String operation) {
        PageHelper.startPage(pageNum, pageSize);
        List<SystemLog> logs;

        if (userType != null) {
            logs = systemLogMapper.selectByUserType(userType);
        } else {
            logs = systemLogMapper.selectAll();
        }

        if (operation != null && !operation.isEmpty()) {
            logs = logs.stream()
                    .filter(l -> l.getOperation().contains(operation))
                    .toList();
        }

        return (Page<SystemLog>) logs;
    }

    public int countAll() {
        return systemLogMapper.countAll();
    }
}