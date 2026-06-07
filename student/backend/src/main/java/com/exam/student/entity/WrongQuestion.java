package com.exam.student.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WrongQuestion {
    private Integer wrongId;
    private Integer studentId;
    private Integer questionId;
    private String studentAnswer;
    private String correctAnswer;
    private Integer wrongCount;
    private LocalDateTime lastWrongTime;
    private Integer subjectId;

    // 关联字段
    private String questionContent;
    private String questionType;
    private String options;
    private String knowledgePoint;
    private String subjectName;
}
