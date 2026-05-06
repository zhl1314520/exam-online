package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExamPaper {
    private Integer paperId;
    private String paperName;
    private Integer subjectId;
    private Double totalScore;
    private Double passScore;
    private Integer duration;
    private Integer questionCount;
    private Integer createBy;
    private LocalDateTime createTime;
    private Integer status;
}