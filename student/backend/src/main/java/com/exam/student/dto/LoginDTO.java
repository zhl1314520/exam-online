package com.exam.student.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String studentNo;
}
