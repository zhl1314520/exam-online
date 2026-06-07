package com.exam.online.controller;

import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Clazz;
import com.exam.online.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/classes")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @GetMapping("/list")
    public ResponseDTO<List<Clazz>> getAll() {
        return ResponseDTO.success(clazzService.getAll());
    }
}
