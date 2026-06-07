package com.exam.online.service;

import com.exam.online.entity.SystemLog;
import com.exam.online.mapper.SystemLogMapper;
import com.github.pagehelper.Page;
import jakarta.servlet.http.HttpServletRequest;
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

    public Page<SystemLog> getByPage(Integer pageNum, Integer pageSize, Integer userType, String operation, String startDate, String endDate) {
        com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);
        return (Page<SystemLog>) systemLogMapper.selectByPage(userType, operation, startDate, endDate);
    }

    public int countAll() {
        return systemLogMapper.countAll();
    }

    /**
     * 记录操作日志
     */
    public void log(String operation, String operationDetail, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        String userTypeStr = (String) request.getAttribute("userType");
        int userType = 1;
        if ("teacher".equals(userTypeStr)) {
            userType = 2;
        } else if ("student".equals(userTypeStr)) {
            userType = 3;
        }

        SystemLog log = new SystemLog();
        log.setUserType(userType);
        log.setUserId(userId);
        log.setOperation(operation);
        log.setOperationDetail(operationDetail);
        log.setIpAddress(getIpAddress(request));
        systemLogMapper.insert(log);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}