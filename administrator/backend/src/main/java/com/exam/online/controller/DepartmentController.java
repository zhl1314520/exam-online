package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Department;
import com.exam.online.service.DepartmentService;
import com.exam.online.service.SystemLogService;
import com.github.pagehelper.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SystemLogService systemLogService;

    @GetMapping("/{id}")
    public ResponseDTO<Department> getById(@PathVariable Integer id) {
        Department department = departmentService.getById(id);
        if (department == null) {
            return ResponseDTO.error(404, "院系不存在");
        }
        return ResponseDTO.success(department);
    }

    @GetMapping("/list")
    public ResponseDTO<List<Department>> getAll() {
        return ResponseDTO.success(departmentService.getAll());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Department>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<Department> page = departmentService.getByPage(pageNum, pageSize, keyword);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @PostMapping("/add")
    public ResponseDTO<Department> add(@RequestBody Department department, HttpServletRequest request) {
        Department created = departmentService.add(department);
        systemLogService.log("新增", "新增院系：" + department.getDepartmentName(), request);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Department> update(@RequestBody Department department, HttpServletRequest request) {
        Department updated = departmentService.update(department);
        systemLogService.log("修改", "修改院系：" + department.getDepartmentName(), request);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable Integer id, HttpServletRequest request) {
        Department department = departmentService.getById(id);
        departmentService.delete(id);
        String detail = department != null ? "删除院系：" + department.getDepartmentName() : "删除院系ID：" + id;
        systemLogService.log("删除", detail, request);
        return ResponseDTO.success("删除成功", null);
    }
}