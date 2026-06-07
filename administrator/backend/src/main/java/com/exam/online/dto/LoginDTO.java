package com.exam.online.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

// 满足业务的登录信息（按需定制）
@Data
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}