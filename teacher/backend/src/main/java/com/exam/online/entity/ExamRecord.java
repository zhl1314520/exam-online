package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExamRecord {
    private Integer recordId;
    private Integer examId;
    private Integer studentId;
    private LocalDateTime startTime;
    private LocalDateTime submitTime;
    private Double totalScore;
    private Double objectiveScore;
    private Double subjectiveScore;
    private Integer status;
    private Integer screenSwitchCount;
    private String ipAddress;

    private String examName;
    private String studentName;
    private String studentNo;
    private String className;
}