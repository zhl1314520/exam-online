package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Question {
    private Integer questionId;
    private Integer subjectId;
    private Integer questionType;
    private String questionContent;
    private String options;
    private String correctAnswer;
    private Double score;
    private Integer difficulty;
    private String knowledgePoint;
    private Integer createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}