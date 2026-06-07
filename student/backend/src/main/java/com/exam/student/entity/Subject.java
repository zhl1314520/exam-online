package com.exam.student.entity;

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

    // 关联字段
    private String departmentName;
}
