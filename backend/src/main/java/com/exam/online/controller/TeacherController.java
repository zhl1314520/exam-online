package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Teacher;
import com.exam.online.service.TeacherService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/{id}")
    public ResponseDTO<Teacher> getById(@PathVariable Integer id) {
        Teacher teacher = teacherService.getById(id);
        if (teacher == null) {
            return ResponseDTO.error(404, "教师不存在");
        }
        return ResponseDTO.success(teacher);
    }

    @GetMapping("/list")
    public ResponseDTO<List<Teacher>> getAll() {
        return ResponseDTO.success(teacherService.getAll());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Teacher>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer departmentId) {
        Page<Teacher> page = teacherService.getByPage(pageNum, pageSize, keyword, departmentId);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @PostMapping("/add")
    public ResponseDTO<Teacher> add(@RequestBody Teacher teacher) {
        Teacher created = teacherService.add(teacher);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Teacher> update(@RequestBody Teacher teacher) {
        Teacher updated = teacherService.update(teacher);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable Integer id) {
        teacherService.delete(id);
        return ResponseDTO.success("删除成功", null);
    }
}