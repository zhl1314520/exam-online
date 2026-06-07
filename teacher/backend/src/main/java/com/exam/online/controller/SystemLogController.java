package com.exam.online.controller;

import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.SystemLog;
import com.exam.online.service.SystemLogService;
import com.exam.online.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/system-logs")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    @Autowired
    private JwtUtil jwtUtil;

    private Integer getCurrentTeacherId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtil.parseToken(token);
            return claims.get("teacherId", Integer.class);
        }
        return null;
    }

    @GetMapping("/recent")
    public ResponseDTO<List<SystemLog>> getRecentLogs(
            @RequestParam(defaultValue = "10") Integer limit,
            HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        List<SystemLog> logs = systemLogService.getRecentLogs(teacherId, limit);
        return ResponseDTO.success(logs);
    }
}
