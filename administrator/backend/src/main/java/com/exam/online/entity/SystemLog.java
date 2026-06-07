package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SystemLog {
    private Integer logId;
    private Integer userType;
    private Integer userId;
    private String operation;
    private String operationDetail;
    private String ipAddress;
    private LocalDateTime createTime;

    // 关联字段
    private String userName;
}