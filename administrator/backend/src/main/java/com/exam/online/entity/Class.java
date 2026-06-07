package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Class {
    private Integer classId;
    private String className;
    private String classCode;
    private Integer departmentId;
    private String grade;
    private Integer studentCount;
    private LocalDateTime createTime;

    // 关联字段
    private String departmentName;
}