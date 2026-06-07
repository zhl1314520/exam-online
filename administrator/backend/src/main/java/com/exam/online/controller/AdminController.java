package com.exam.online.controller;

import com.exam.online.dto.LoginDTO;
import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Admin;
import com.exam.online.service.AdminService;
import com.github.pagehelper.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseDTO<Map<String, Object>> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return adminService.login(loginDTO, request);
    }

    @GetMapping("/profile")
    public ResponseDTO<Admin> getProfile(@RequestParam Integer adminId) {
        Admin admin = adminService.getAdminById(adminId);
        if (admin == null) {
            return ResponseDTO.error(404, "管理员不存在");
        }
        return ResponseDTO.success(admin);
    }

    @GetMapping("/list")
    public ResponseDTO<List<Admin>> getAllAdmins() {
        return ResponseDTO.success(adminService.getAllAdmins());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Admin>> getAdminsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Admin> page = adminService.getAdminsByPage(pageNum, pageSize);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @PostMapping("/add")
    public ResponseDTO<Admin> addAdmin(@RequestBody Admin admin) {
        Admin created = adminService.addAdmin(admin);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Admin> updateAdmin(@RequestBody Admin admin) {
        Admin updated = adminService.updateAdmin(admin);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> deleteAdmin(@PathVariable Integer id) {
        adminService.deleteAdmin(id);
        return ResponseDTO.success("删除成功", null);
    }

    @PutMapping("/password")
    public ResponseDTO<Void> updatePassword(
            @RequestParam Integer adminId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        adminService.updatePassword(adminId, oldPassword, newPassword);
        return ResponseDTO.success("密码修改成功", null);
    }
}