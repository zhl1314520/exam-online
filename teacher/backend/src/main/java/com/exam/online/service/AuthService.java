package com.exam.online.service;

import com.exam.online.dto.ChangePasswordDTO;
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

    public Teacher getTeacherById(Integer teacherId) {
        return teacherMapper.selectByPrimaryKey(teacherId);
    }

    public void changePassword(Integer teacherId, ChangePasswordDTO changePasswordDTO) {
        Teacher teacher = teacherMapper.selectByPrimaryKey(teacherId);
        if (teacher == null) {
            throw new BusinessException(404, "教师不存在");
        }

        // 验证旧密码
        String oldEncryptedPassword = PasswordUtil.md5(changePasswordDTO.getOldPassword());
        if (!oldEncryptedPassword.equals(teacher.getPassword())) {
            throw new BusinessException(400, "旧密码错误");
        }

        // 更新新密码
        String newEncryptedPassword = PasswordUtil.md5(changePasswordDTO.getNewPassword());
        teacherMapper.updatePassword(teacherId, newEncryptedPassword);
    }
}