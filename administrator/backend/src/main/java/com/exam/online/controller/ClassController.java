package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Class;
import com.exam.online.service.ClassService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping("/{id}")
    public ResponseDTO<Class> getById(@PathVariable Integer id) {
        Class clazz = classService.getById(id);
        if (clazz == null) {
            return ResponseDTO.error(404, "班级不存在");
        }
        return ResponseDTO.success(clazz);
    }

    @GetMapping("/list")
    public ResponseDTO<List<Class>> getAll() {
        return ResponseDTO.success(classService.getAll());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Class>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer departmentId) {
        Page<Class> page = classService.getByPage(pageNum, pageSize, keyword, departmentId);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseDTO<List<Class>> getByDepartmentId(@PathVariable Integer departmentId) {
        return ResponseDTO.success(classService.getByDepartmentId(departmentId));
    }

    @PostMapping("/add")
    public ResponseDTO<Class> add(@RequestBody Class clazz) {
        Class created = classService.add(clazz);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Class> update(@RequestBody Class clazz) {
        Class updated = classService.update(clazz);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable Integer id) {
        classService.delete(id);
        return ResponseDTO.success("删除成功", null);
    }
}