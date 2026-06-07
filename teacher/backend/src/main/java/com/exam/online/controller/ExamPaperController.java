package com.exam.online.controller;

import com.exam.online.annotation.OperationLog;
import com.exam.online.dto.PageDTO;
import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.ExamPaper;
import com.exam.online.entity.PaperQuestion;
import com.exam.online.service.ExamPaperService;
import com.exam.online.utils.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer subjectId,
            HttpServletRequest request) {

        Integer teacherId = getCurrentTeacherId(request);
        PageHelper.startPage(pageNum, pageSize);
        List<ExamPaper> papers = examPaperService.getByCreateBy(teacherId);

        // 根据keyword筛选试卷名称
        if (keyword != null && !keyword.isEmpty()) {
            papers = papers.stream()
                    .filter(p -> p.getPaperName() != null && p.getPaperName().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // 根据subjectId筛选科目
        if (subjectId != null) {
            papers = papers.stream()
                    .filter(p -> p.getSubjectId() != null && p.getSubjectId().equals(subjectId))
                    .collect(Collectors.toList());
        }

        PageInfo<ExamPaper> pageInfo = new PageInfo<>(papers);
        PageDTO<ExamPaper> pageDTO = new PageDTO<>();
        pageDTO.setTotal(pageInfo.getTotal());
        pageDTO.setPageNum(pageInfo.getPageNum());
        pageDTO.setPageSize(pageInfo.getPageSize());
        pageDTO.setPages(pageInfo.getPages());
        pageDTO.setList(pageInfo.getList());
        return ResponseDTO.success(pageDTO);
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
    @OperationLog(operation = "创建试卷")
    public ResponseDTO<ExamPaper> add(@RequestBody ExamPaper paper, HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        paper.setCreateBy(teacherId);
        ExamPaper created = examPaperService.add(paper);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    @OperationLog(operation = "更新试卷")
    public ResponseDTO<ExamPaper> update(@RequestBody ExamPaper paper) {
        ExamPaper updated = examPaperService.update(paper);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    @OperationLog(operation = "删除试卷")
    public ResponseDTO<Void> delete(@PathVariable Integer id) {
        examPaperService.delete(id);
        return ResponseDTO.success("删除成功", null);
    }

    @GetMapping("/{id}/questions")
    public ResponseDTO<List<PaperQuestion>> getPaperQuestions(@PathVariable Integer id) {
        return ResponseDTO.success(examPaperService.getPaperQuestions(id));
    }

    @PostMapping("/{id}/questions")
    @OperationLog(operation = "添加试题到试卷")
    public ResponseDTO<Void> addQuestionToPaper(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Integer> questionIds = (List<Integer>) body.get("questionIds");
        if (questionIds != null && !questionIds.isEmpty()) {
            examPaperService.addQuestionsToPaper(id, questionIds);
        }
        return ResponseDTO.success("添加成功", null);
    }

    @DeleteMapping("/{id}/questions/{questionId}")
    @OperationLog(operation = "从试卷移除试题")
    public ResponseDTO<Void> removeQuestionFromPaper(@PathVariable Integer id, @PathVariable Integer questionId) {
        examPaperService.removeQuestionFromPaperByQuestionId(id, questionId);
        return ResponseDTO.success("删除成功", null);
    }
}