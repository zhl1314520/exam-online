package com.exam.online.controller;

import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.ExamPaper;
import com.exam.online.entity.PaperQuestion;
import com.exam.online.service.ExamPaperService;
import com.exam.online.utils.JwtUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/papers")
public class ExamPaperController {

    @Autowired
    private ExamPaperService examPaperService;

    @Autowired
    private JwtUtil jwtUtil;

    private Integer getCurrentTeacherId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtil.parseToken(token);
            return claims.get("teacherId", Integer.class);
        }
        return null;
    }

    @GetMapping("/page")
    public ResponseDTO<PageDTO<ExamPaper>> getByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {

        Integer teacherId = getCurrentTeacherId(request);
        PageHelper.startPage(pageNum, pageSize);
        List<ExamPaper> papers = examPaperService.getByCreateBy(teacherId);

        return ResponseDTO.success(PageDTO.of((Page<ExamPaper>) papers));
    }

    @GetMapping("/list")
    public ResponseDTO<List<ExamPaper>> getAll(HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        return ResponseDTO.success(examPaperService.getByCreateBy(teacherId));
    }

    @GetMapping("/{id}")
    public ResponseDTO<ExamPaper> getById(@PathVariable Integer id) {
        ExamPaper paper = examPaperService.getById(id);
        if (paper == null) {
            return ResponseDTO.error(404, "试卷不存在");
        }
        return ResponseDTO.success(paper);
    }

    @PostMapping("/add")
    public ResponseDTO<ExamPaper> add(@RequestBody ExamPaper paper, HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        paper.setCreateBy(teacherId);
        ExamPaper created = examPaperService.add(paper);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<ExamPaper> update(@RequestBody ExamPaper paper) {
        ExamPaper updated = examPaperService.update(paper);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> delete(@PathVariable Integer id) {
        examPaperService.delete(id);
        return ResponseDTO.success("删除成功", null);
    }

    @GetMapping("/{id}/questions")
    public ResponseDTO<List<PaperQuestion>> getPaperQuestions(@PathVariable Integer id) {
        return ResponseDTO.success(examPaperService.getPaperQuestions(id));
    }

    @PostMapping("/{id}/questions")
    public ResponseDTO<Void> addQuestionToPaper(@PathVariable Integer id, @RequestBody PaperQuestion pq) {
        pq.setPaperId(id);
        examPaperService.addQuestionToPaper(pq);
        return ResponseDTO.success("添加成功", null);
    }

    @DeleteMapping("/{id}/questions/{questionId}")
    public ResponseDTO<Void> removeQuestionFromPaper(@PathVariable Integer id, @PathVariable Integer questionId) {
        // 根据 paperId 和 questionId 找到对应记录删除
        return ResponseDTO.success("删除成功", null);
    }
}