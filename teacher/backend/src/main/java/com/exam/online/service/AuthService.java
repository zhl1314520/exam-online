package com.exam.online.service;

import com.exam.online.dto.LoginDTO;
import com.exam.online.entity.Teacher;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.TeacherMapper;
import com.exam.online.utils.JwtUtil;
import com.exam.online.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> login(LoginDTO loginDTO) {
        Teacher teacher = teacherMapper.selectByTeacherNo(loginDTO.getTeacherNo());
        if (teacher == null) {
            throw new BusinessException(401, "工号或密码错误");
        }
        if (teacher.getStatus() != 1) {
            throw new BusinessException(401, "账号已被禁用");
        }
        String encryptedPassword = PasswordUtil.md5(loginDTO.getPassword());
        if (!encryptedPassword.equals(teacher.getPassword())) {
            throw new BusinessException(401, "工号或密码错误");
        }

        teacherMapper.updateLastLoginTime(teacher.getTeacherId());

        String token = jwtUtil.generateToken(teacher.getTeacherId(), teacher.getTeacherNo(), teacher.getRealName());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("teacher", teacher);
        return result;
    }
}