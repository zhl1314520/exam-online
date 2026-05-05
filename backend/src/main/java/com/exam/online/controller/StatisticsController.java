package com.exam.online.controller;

import com.exam.online.dto.ResponseDTO;
import com.exam.online.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/overview")
    public ResponseDTO<Map<String, Object>> getOverview() {
        return ResponseDTO.success(statisticsService.getOverview());
    }

    @GetMapping("/users")
    public ResponseDTO<Map<String, Object>> getUserStatistics() {
        return ResponseDTO.success(statisticsService.getUserStatistics());
    }
}