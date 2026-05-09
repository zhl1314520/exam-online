package com.exam.student.controller;

import com.exam.student.dto.LoginDTO;
import com.exam.student.dto.ResponseDTO;
import com.exam.student.dto.UpdatePasswordDTO;
import com.exam.student.entity.Student;
import com.exam.student.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseDTO<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = authService.login(loginDTO);
        return ResponseDTO.success("登录成功", result);
    }

    @GetMapping("/me")
    public ResponseDTO<Student> getCurrentUser(HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        Student student = authService.getCurrentStudent(studentId);
        return ResponseDTO.success(student);
    }

    @PostMapping("/password")
    public ResponseDTO<Void> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto, HttpServletRequest request) {
        Integer studentId = (Integer) request.getAttribute("userId");
        authService.updatePassword(studentId, dto);
        return ResponseDTO.success("密码修改成功", null);
    }
}
