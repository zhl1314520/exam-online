package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.SystemLog;
import com.exam.online.service.SystemLogService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    @GetMapping("/{id}")
    public ResponseDTO<SystemLog> getById(@PathVariable Integer id) {
        SystemLog log = systemLogService.getById(id);
        if (log == null) {
            return ResponseDTO.error(404, "日志不存在");
        }
        return ResponseDTO.success(log);
    }

    @GetMapping("/list")
    public ResponseDTO<List<SystemLog>> getAll() {
        return ResponseDTO.success(systemLogService.getAll());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<SystemLog>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer userType,
            @RequestParam(required = false) String operation) {
        Page<SystemLog> page = systemLogService.getByPage(pageNum, pageSize, userType, operation);
        return ResponseDTO.success(PageDTO.of(page));
    }
}