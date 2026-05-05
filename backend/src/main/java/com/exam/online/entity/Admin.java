package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Admin {
    private Integer adminId;
    private String username;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
}