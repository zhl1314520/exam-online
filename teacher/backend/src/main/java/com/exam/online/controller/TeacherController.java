package com.exam.online.controller;

import com.exam.online.dto.ChangePasswordDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Teacher;
import com.exam.online.service.AuthService;
import com.exam.online.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private AuthService authService;

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

    @GetMapping("/profile")
    public ResponseDTO<Teacher> getProfile(HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        if (teacherId == null) {
            return ResponseDTO.error(401, "未授权");
        }

        Teacher teacher = authService.getTeacherById(teacherId);
        if (teacher == null) {
            return ResponseDTO.error(404, "教师不存在");
        }

        // 清理敏感信息
        teacher.setPassword(null);
        return ResponseDTO.success(teacher);
    }

    @PostMapping("/change-password")
    public ResponseDTO<Void> changePassword(
            @RequestBody ChangePasswordDTO changePasswordDTO,
            HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        if (teacherId == null) {
            return ResponseDTO.error(401, "未授权");
        }

        authService.changePassword(teacherId, changePasswordDTO);
        return ResponseDTO.success("密码修改成功", null);
    }
}
