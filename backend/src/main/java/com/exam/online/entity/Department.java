package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Department {
    private Integer departmentId;
    private String departmentName;
    private String departmentCode;
    private String description;
    private LocalDateTime createTime;
}