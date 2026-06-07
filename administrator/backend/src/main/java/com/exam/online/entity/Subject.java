package com.exam.online.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Subject {
    private Integer subjectId;
    private String subjectName;
    private String subjectCode;
    private Integer departmentId;
    private BigDecimal credit;
    private String description;
    private LocalDateTime createTime;

    private String departmentName;
}
