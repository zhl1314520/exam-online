package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Student;
import com.exam.online.service.StudentService;
import com.exam.online.service.SystemLogService;
import com.github.pagehelper.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SystemLogService systemLogService;

    @GetMapping("/{id}")
    public ResponseDTO<Student> getById(@PathVariable Integer id) {
        Student student = studentService.getById(id);
        if (student == null) {
            return ResponseDTO.error(404, "学生不存在");
        }
        return ResponseDTO.success(student);
    }

    @GetMapping("/list")
    public ResponseDTO<List<Student>> getAll() {
        return ResponseDTO.success(studentService.getAll());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Student>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer classId) {
        PageDTO<Student> page = studentService.getByPage(pageNum, pageSize, keyword, classId);
        return ResponseDTO.success(page);
    }

    @PostMapping("/add")
    public ResponseDTO<Student> add(@RequestBody Student student, HttpServletRequest request) {
        Student created = studentService.add(student);
        systemLogService.log("新增", "新增学生：" + student.getRealName() + "（学号：" + student.getStudentNo() + "）", request);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Student> update(@RequestBody Student student, HttpServletRequest request) {
        Student updated = studentService.update(student);
        systemLogService.log("修改", "修改学生：" + student.getRealName() + "（学号：" + student.getStudentNo() + "）", request);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable Integer id, HttpServletRequest request) {
        Student student = studentService.getById(id);
        studentService.delete(id);
        String detail = student != null ? "删除学生：" + student.getRealName() + "（学号：" + student.getStudentNo() + "）" : "删除学生ID：" + id;
        systemLogService.log("删除", detail, request);
        return ResponseDTO.success("删除成功", null);
    }

    @PostMapping("/batch")
    public ResponseDTO<Void> batchAdd(@RequestBody List<Student> students, HttpServletRequest request) {
        studentService.batchAdd(students);
        systemLogService.log("导入", "批量导入学生，共" + students.size() + "条", request);
        return ResponseDTO.success("批量添加成功", null);
    }
}
