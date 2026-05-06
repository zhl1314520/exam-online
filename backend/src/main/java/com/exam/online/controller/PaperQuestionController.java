package com.exam.online.controller;

import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.PaperQuestion;
import com.exam.online.service.PaperQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paperQuestion")
public class PaperQuestionController {

    @Autowired
    private PaperQuestionService paperQuestionService;

    @GetMapping("/{id}")
    public ResponseDTO<PaperQuestion> getPaperQuestionById(@PathVariable Integer id) {
        PaperQuestion paperQuestion = paperQuestionService.getPaperQuestionById(id);
        if (paperQuestion == null) {
            return ResponseDTO.error(404, "试卷题目关联不存在");
        }
        return ResponseDTO.success(paperQuestion);
    }

    @GetMapping("/paper/{paperId}")
    public ResponseDTO<List<PaperQuestion>> getPaperQuestionsByPaperId(@PathVariable Integer paperId) {
        return ResponseDTO.success(paperQuestionService.getPaperQuestionsByPaperId(paperId));
    }

    @GetMapping("/question/{questionId}")
    public ResponseDTO<List<PaperQuestion>> getPaperQuestionsByQuestionId(@PathVariable Integer questionId) {
        return ResponseDTO.success(paperQuestionService.getPaperQuestionsByQuestionId(questionId));
    }

    @PostMapping("/add")
    public ResponseDTO<PaperQuestion> addPaperQuestion(@RequestBody PaperQuestion paperQuestion) {
        PaperQuestion created = paperQuestionService.addPaperQuestion(paperQuestion);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<PaperQuestion> updatePaperQuestion(@RequestBody PaperQuestion paperQuestion) {
        PaperQuestion updated = paperQuestionService.updatePaperQuestion(paperQuestion);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> deletePaperQuestion(@PathVariable Integer id) {
        paperQuestionService.deletePaperQuestion(id);
        return ResponseDTO.success("删除成功", null);
    }

    @DeleteMapping("/delete/paper/{paperId}")
    public ResponseDTO<Void> deletePaperQuestionsByPaperId(@PathVariable Integer paperId) {
        paperQuestionService.deletePaperQuestionsByPaperId(paperId);
        return ResponseDTO.success("删除成功", null);
    }

    @GetMapping("/count/paper/{paperId}")
    public ResponseDTO<Integer> getPaperQuestionCountByPaperId(@PathVariable Integer paperId) {
        return ResponseDTO.success(paperQuestionService.getPaperQuestionCountByPaperId(paperId));
    }
}
