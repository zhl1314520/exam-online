package com.exam.student.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnswerRecord {
    private Integer answerId;
    private Integer recordId;
    private Integer questionId;
    private String studentAnswer;
    private Integer isCorrect;
    private Double score;
    private LocalDateTime answerTime;

    // 关联字段
    private String questionContent;
    private String questionType;
    private String options;
    private String correctAnswer;
}
