package com.exam.online.entity;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class SystemLog {
    private Integer logId;
    private Integer userType;
    private Integer userId;
    private String operation;
    private String operationDetail;
    private String ipAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String userName;
}
