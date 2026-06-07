package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Subject;
import com.exam.online.service.SubjectService;
import com.exam.online.service.SystemLogService;
import com.github.pagehelper.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SystemLogService systemLogService;

    @GetMapping("/{id}")
    public ResponseDTO<Subject> getById(@PathVariable Integer id) {
        Subject subject = subjectService.getById(id);
        if (subject == null) {
            return ResponseDTO.error(404, "学科不存在");
        }
        return ResponseDTO.success(subject);
    }

    @GetMapping("/list")
    public ResponseDTO<List<Subject>> getAll() {
        return ResponseDTO.success(subjectService.getAll());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Subject>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer departmentId) {
        Page<Subject> page = subjectService.getByPage(pageNum, pageSize, keyword, departmentId);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseDTO<List<Subject>> getByDepartmentId(@PathVariable Integer departmentId) {
        return ResponseDTO.success(subjectService.getByDepartmentId(departmentId));
    }

    @PostMapping("/add")
    public ResponseDTO<Subject> add(@RequestBody Subject subject, HttpServletRequest request) {
        Subject created = subjectService.add(subject);
        systemLogService.log("新增", "新增学科：" + subject.getSubjectName(), request);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Subject> update(@RequestBody Subject subject, HttpServletRequest request) {
        Subject updated = subjectService.update(subject);
        systemLogService.log("修改", "修改学科：" + subject.getSubjectName(), request);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable Integer id, HttpServletRequest request) {
        Subject subject = subjectService.getById(id);
        subjectService.delete(id);
        String detail = subject != null ? "删除学科：" + subject.getSubjectName() : "删除学科ID：" + id;
        systemLogService.log("删除", detail, request);
        return ResponseDTO.success("删除成功", null);
    }
}
