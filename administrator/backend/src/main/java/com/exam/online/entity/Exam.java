package com.exam.online.entity;

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

    private String paperName;
    private String className;
    private String createName;
    private Integer submitCount;
    private Double passRate;
}
