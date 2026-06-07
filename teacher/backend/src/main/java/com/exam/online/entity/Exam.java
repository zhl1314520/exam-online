package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class Exam {
    private Integer examId;
    private String examName;
    private Integer paperId;
    private Integer classId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private Integer status;
    private Integer allowScreenSwitch;
    private Integer createBy;
    private LocalDateTime createTime;

    private String paperName;
    private String className;
    private Integer studentCount;
    private Integer submittedCount;
    private Integer duration;
}