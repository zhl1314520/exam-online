package com.exam.online.controller;

import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.Subject;
import com.exam.online.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/list")
    public ResponseDTO<List<Subject>> getAll() {
        return ResponseDTO.success(subjectService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseDTO<Subject> getById(@PathVariable Integer id) {
        Subject subject = subjectService.getById(id);
        if (subject == null) {
            return ResponseDTO.error(404, "科目不存在");
        }
        return ResponseDTO.success(subject);
    }
}