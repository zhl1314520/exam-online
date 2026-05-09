package com.exam.student.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamRecord {

    private List<AnswerRecord> answers;
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

    // 关联字段
    private String examName;
    private String paperName;
    private Integer duration;
    private Integer totalScoreOfPaper;
    private Integer passScore;
    private LocalDateTime startTimeOfExam;
    private LocalDateTime endTimeOfExam;
}
