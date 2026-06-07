package com.exam.student.service;

import com.exam.student.dto.LoginDTO;
import com.exam.student.dto.UpdatePasswordDTO;
import com.exam.student.entity.Student;
import com.exam.student.exception.BusinessException;
import com.exam.student.mapper.StudentMapper;
import com.exam.student.utils.JwtUtil;
import com.exam.student.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> login(LoginDTO loginDTO) {
        // 支持从统一登录传入studentNo，也支持直接传入username
        String studentNo = loginDTO.getStudentNo();
        if (studentNo == null || studentNo.isEmpty()) {
            studentNo = loginDTO.getUsername();
        }
        Student student = studentMapper.selectByStudentNo(studentNo);
        if (student == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (student.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }

        if (!PasswordUtil.verify(loginDTO.getPassword(), student.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 更新最后登录时间
        studentMapper.updateLastLoginTime(student.getStudentId());

        // 生成JWT
        String token = jwtUtil.generateToken(student.getStudentId(), student.getStudentNo(), "student");

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("student", student);
        return result;
    }

    public Student getCurrentStudent(Integer studentId) {
        return studentMapper.selectById(studentId);
    }

    public void updatePassword(Integer studentId, UpdatePasswordDTO dto) {
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            throw new BusinessException("用户不存在");
        }

        if (!PasswordUtil.verify(dto.getOldPassword(), student.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        String newPassword = PasswordUtil.md5(dto.getNewPassword());
        studentMapper.updatePassword(studentId, newPassword);
    }
}
