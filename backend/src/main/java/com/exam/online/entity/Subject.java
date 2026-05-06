package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Subject {
    private Integer subjectId;
    private String subjectName;
    private String subjectCode;
    private Integer departmentId;
    private Double credit;
    private String description;
    private LocalDateTime createTime;
}