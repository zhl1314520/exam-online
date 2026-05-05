package com.exam.online.service;

import com.exam.online.dto.LoginDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Admin;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.AdminMapper;
import com.exam.online.mapper.SystemLogMapper;
import com.exam.online.entity.SystemLog;
import com.exam.online.utils.JwtUtil;
import com.exam.online.utils.PasswordUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private SystemLogMapper systemLogMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseDTO<Map<String, Object>> login(LoginDTO loginDTO, HttpServletRequest request) {
        Admin admin = adminMapper.selectByUsername(loginDTO.getUsername());
        if (admin == null) {
            throw new BusinessException(401, "用户不存在");
        }
        if (admin.getStatus() != 1) {
            throw new BusinessException(403, "账号已被禁用");
        }
        if (!PasswordUtil.verify(loginDTO.getPassword(), admin.getPassword())) {
            throw new BusinessException(401, "密码错误");
        }

        adminMapper.updateLastLoginTime(admin.getAdminId());

        String token = jwtUtil.generateToken(admin.getAdminId(), admin.getUsername(), "admin");

        SystemLog log = new SystemLog();
        log.setUserType(1);
        log.setUserId(admin.getAdminId());
        log.setOperation("登录系统");
        log.setOperationDetail("管理员" + admin.getUsername() + "登录成功");
        log.setIpAddress(getIpAddress(request));
        systemLogMapper.insert(log);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("admin", admin);
        return ResponseDTO.success("登录成功", result);
    }

    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectById(adminId);
    }

    public List<Admin> getAllAdmins() {
        return adminMapper.selectAll();
    }

    public Page<Admin> getAdminsByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Admin>) adminMapper.selectAll();
    }

    @Transactional
    public Admin addAdmin(Admin admin) {
        Admin existing = adminMapper.selectByUsername(admin.getUsername());
        if (existing != null) {
            throw new BusinessException(400, "用户名已存在");
        }
        admin.setPassword(PasswordUtil.md5(admin.getPassword()));
        admin.setStatus(1);
        adminMapper.insert(admin);
        return admin;
    }

    @Transactional
    public Admin updateAdmin(Admin admin) {
        Admin existing = adminMapper.selectById(admin.getAdminId());
        if (existing == null) {
            throw new BusinessException(404, "管理员不存在");
        }
        Admin sameName = adminMapper.selectByUsername(admin.getUsername());
        if (sameName != null && !sameName.getAdminId().equals(admin.getAdminId())) {
            throw new BusinessException(400, "用户名已存在");
        }
        adminMapper.updateById(admin);
        return adminMapper.selectById(admin.getAdminId());
    }

    @Transactional
    public void deleteAdmin(Integer adminId) {
        Admin existing = adminMapper.selectById(adminId);
        if (existing == null) {
            throw new BusinessException(404, "管理员不存在");
        }
        adminMapper.deleteById(adminId);
    }

    @Transactional
    public void updatePassword(Integer adminId, String oldPassword, String newPassword) {
        Admin admin = adminMapper.selectById(adminId);
        if (admin == null) {
            throw new BusinessException(404, "管理员不存在");
        }
        if (!PasswordUtil.verify(oldPassword, admin.getPassword())) {
            throw new BusinessException(400, "原密码错误");
        }
        admin.setPassword(PasswordUtil.md5(newPassword));
        adminMapper.updateById(admin);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}