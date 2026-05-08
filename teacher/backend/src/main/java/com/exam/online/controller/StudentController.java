package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Student;
import com.exam.online.service.StudentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Student>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer classId) {

        PageHelper.startPage(pageNum, pageSize);
        List<Student> students;
        if (classId != null) {
            students = studentService.getByClassId(classId);
        } else {
            students = studentService.getAll();
        }

        return ResponseDTO.success(PageDTO.of((Page<Student>) students));
    }

    @GetMapping("/list")
    public ResponseDTO<List<Student>> getAll() {
        return ResponseDTO.success(studentService.getAll());
    }

    @GetMapping("/class/{classId}")
    public ResponseDTO<List<Student>> getByClassId(@PathVariable Integer classId) {
        return ResponseDTO.success(studentService.getByClassId(classId));
    }

    @GetMapping("/{id}")
    public ResponseDTO<Student> getById(@PathVariable Integer id) {
        Student student = studentService.getById(id);
        if (student == null) {
            return ResponseDTO.error(404, "学生不存在");
        }
        return ResponseDTO.success(student);
    }
}