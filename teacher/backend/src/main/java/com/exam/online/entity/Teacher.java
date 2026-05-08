package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Teacher {
    private Integer teacherId;
    private String teacherNo;
    private String password;
    private String realName;
    private Integer gender;
    private Integer departmentId;
    private String email;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;

    private String departmentName;
}