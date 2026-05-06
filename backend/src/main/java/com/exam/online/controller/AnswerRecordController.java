package com.exam.online.controller;

import com.exam.online.dto.ResponseDTO;
import com.exam.online.entity.AnswerRecord;
import com.exam.online.service.AnswerRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answerRecord")
public class AnswerRecordController {

    @Autowired
    private AnswerRecordService answerRecordService;

    @GetMapping("/{id}")
    public ResponseDTO<AnswerRecord> getAnswerRecordById(@PathVariable Integer id) {
        AnswerRecord answerRecord = answerRecordService.getAnswerRecordById(id);
        if (answerRecord == null) {
            return ResponseDTO.error(404, "答题记录不存在");
        }
        return ResponseDTO.success(answerRecord);
    }

    @GetMapping("/list")
    public ResponseDTO<List<AnswerRecord>> getAllAnswerRecords() {
        return ResponseDTO.success(answerRecordService.getAllAnswerRecords());
    }

    @GetMapping("/record/{recordId}")
    public ResponseDTO<List<AnswerRecord>> getAnswerRecordsByRecordId(@PathVariable Integer recordId) {
        return ResponseDTO.success(answerRecordService.getAnswerRecordsByRecordId(recordId));
    }

    @GetMapping("/question/{questionId}")
    public ResponseDTO<List<AnswerRecord>> getAnswerRecordsByQuestionId(@PathVariable Integer questionId) {
        return ResponseDTO.success(answerRecordService.getAnswerRecordsByQuestionId(questionId));
    }

    @GetMapping("/record/{recordId}/question/{questionId}")
    public ResponseDTO<AnswerRecord> getAnswerRecordByRecordIdAndQuestionId(
            @PathVariable Integer recordId,
            @PathVariable Integer questionId) {
        AnswerRecord record = answerRecordService.getAnswerRecordByRecordIdAndQuestionId(recordId, questionId);
        if (record == null) {
            return ResponseDTO.error(404, "答题记录不存在");
        }
        return ResponseDTO.success(record);
    }

    @PostMapping("/add")
    public ResponseDTO<AnswerRecord> addAnswerRecord(@RequestBody AnswerRecord answerRecord) {
        AnswerRecord created = answerRecordService.addAnswerRecord(answerRecord);
        return ResponseDTO.success("添加成功", created);
    }

    @PutMapping("/update")
    public ResponseDTO<AnswerRecord> updateAnswerRecord(@RequestBody AnswerRecord answerRecord) {
        AnswerRecord updated = answerRecordService.updateAnswerRecord(answerRecord);
        return ResponseDTO.success("更新成功", updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Void> deleteAnswerRecord(@PathVariable Integer id) {
        answerRecordService.deleteAnswerRecord(id);
        return ResponseDTO.success("删除成功", null);
    }

    @DeleteMapping("/delete/record/{recordId}")
    public ResponseDTO<Void> deleteAnswerRecordsByRecordId(@PathVariable Integer recordId) {
        answerRecordService.deleteAnswerRecordsByRecordId(recordId);
        return ResponseDTO.success("删除成功", null);
    }

    @GetMapping("/count/record/{recordId}")
    public ResponseDTO<Integer> getAnswerRecordCountByRecordId(@PathVariable Integer recordId) {
        return ResponseDTO.success(answerRecordService.getAnswerRecordCountByRecordId(recordId));
    }

    @GetMapping("/totalScore/record/{recordId}")
    public ResponseDTO<Double> getTotalScoreByRecordId(@PathVariable Integer recordId) {
        return ResponseDTO.success(answerRecordService.getTotalScoreByRecordId(recordId));
    }
}
