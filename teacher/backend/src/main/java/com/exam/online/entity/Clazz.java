package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Clazz {
    private Integer classId;
    private String className;
    private String classCode;
    private Integer departmentId;
    private Integer grade;
    private Integer studentCount;
    private LocalDateTime createTime;

    private String departmentName;
}
