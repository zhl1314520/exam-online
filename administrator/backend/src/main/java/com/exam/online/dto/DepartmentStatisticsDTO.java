package com.exam.online.dto;

import lombok.Data;

import java.util.List;

// 部门统计信息-数据模型
@Data
public class DepartmentStatisticsDTO {
    private String departmentName;
    private Integer teacherCount;
    private Integer classCount;
    private Integer studentCount;
    private Integer examCount;
}