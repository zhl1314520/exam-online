package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Subject;
import com.exam.online.service.SubjectService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/{id}")
    public ResponseDTO<Subject> getSubjectById(@PathVariable Integer id) {
        Subject subject = subjectService.getSubjectById(id);
        if (subject == null) {
            return ResponseDTO.error(404, "课程不存在");
        }
        return ResponseDTO.success(subject);
    }

    @GetMapping("/code/{subjectCode}")
    public ResponseDTO<Subject> getSubjectByCode(@PathVariable String subjectCode) {
        Subject subject = subjectService.getSubjectByCode(subjectCode);
        if (subject == null) {
            return ResponseDTO.error(404, "课程不存在");
        }
        return ResponseDTO.success(subject);
    }

    @GetMapping("/list")
    public ResponseDTO<List<Subject>> getAllSubjects() {
        return ResponseDTO.success(subjectService.getAllSubjects());
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<Subject>> getSubjectsByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Subject> page = subjectService.getSubjectsByPage(pageNum, pageSize);
        return ResponseDTO.success(PageDTO.of(page));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseDTO<List<Subject>> getSubjectsByDepartmentId(@PathVariable Integer departmentId) {
        return ResponseDTO.success(subjectService.getSubjectsByDepartmentId(departmentId));
    }

    @PostMapping("/add")
    public ResponseDTO<Subject> addSubject(@RequestBody Subject subject) {
        Subject created = subjectService.addSubject(subject);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<Subject> updateSubject(@RequestBody Subject subject) {
        Subject updated = subjectService.updateSubject(subject);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> deleteSubject(@PathVariable Integer id) {
        subjectService.deleteSubject(id);
        return ResponseDTO.success("删除成功", null);
    }

    @GetMapping("/count")
    public ResponseDTO<Integer> getSubjectCount() {
        return ResponseDTO.success(subjectService.getSubjectCount());
    }

    @GetMapping("/count/department/{departmentId}")
    public ResponseDTO<Integer> getSubjectCountByDepartmentId(@PathVariable Integer departmentId) {
        return ResponseDTO.success(subjectService.getSubjectCountByDepartmentId(departmentId));
    }
}
