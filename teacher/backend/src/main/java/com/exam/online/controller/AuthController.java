package com.exam.online.controller;

import com.exam.online.dto.LoginDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseDTO<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = authService.login(loginDTO);
        return ResponseDTO.success("登录成功", result);
    }
}