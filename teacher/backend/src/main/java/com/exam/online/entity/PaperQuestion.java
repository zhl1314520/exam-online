package com.exam.online.entity;

import lombok.Data;

@Data
public class PaperQuestion {
    private Integer id;
    private Integer paperId;
    private Integer questionId;
    private Integer questionOrder;
    private Double score;

    private String questionContent;
    private Integer questionType;
    private String options;
    private String correctAnswer;
    private String subjectName;
}