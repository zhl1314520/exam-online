package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ExamRecord {
    private Integer recordId;
    private Integer examId;
    private Integer studentId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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