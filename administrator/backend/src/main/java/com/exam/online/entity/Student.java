package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Student {
    private Integer studentId;
    private String studentNo;
    private String password;
    private String realName;
    private Byte gender;
    private Integer classId;
    private String email;
    private String phone;
    private Byte status;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;

    // 关联字段
    private String className;
    private Integer departmentId;
    private String departmentName;
}