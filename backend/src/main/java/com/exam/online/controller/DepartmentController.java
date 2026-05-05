package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Department;
import com.exam.online.service.DepartmentService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

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
    public ResponseDTO<Department> add(@RequestBody Department department) {
        Department created = departmentService.add(department);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Department> update(@RequestBody Department department) {
        Department updated = departmentService.update(department);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable Integer id) {
        departmentService.delete(id);
        return ResponseDTO.success("删除成功", null);
    }
}