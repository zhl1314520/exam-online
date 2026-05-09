package com.exam.student.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Student {
    private Integer studentId;
    private String studentNo;
    private String password;
    private String realName;
    private Integer gender;
    private Integer classId;
    private String email;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;

    // 关联字段
    private String className;
    private Integer departmentId;
    private String departmentName;
}
