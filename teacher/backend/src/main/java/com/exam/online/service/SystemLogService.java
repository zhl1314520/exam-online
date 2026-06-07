package com.exam.online.service;

import com.exam.online.entity.SystemLog;
import com.exam.online.mapper.SystemLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemLogService {

    @Autowired
    private SystemLogMapper systemLogMapper;

    public List<SystemLog> getRecentLogs(Integer userId, Integer limit) {
        return systemLogMapper.selectRecent(userId, limit);
    }
}
