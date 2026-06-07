package com.exam.student.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Exam {
    private Integer examId;
    private String examName;
    private Integer paperId;
    private Integer classId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private Integer allowScreenSwitch;
    private Integer createBy;
    private LocalDateTime createTime;

    // 关联字段
    private String paperName;
    private Integer duration;
    private Double totalScore;
    private Double passScore;
    private String subjectName;
}
