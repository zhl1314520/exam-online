package com.exam.online.mapper;

import com.exam.online.entity.SystemLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemLogMapper {

    SystemLog selectById(@Param("logId") Integer logId);

    List<SystemLog> selectAll();

    List<SystemLog> selectByUserType(@Param("userType") Integer userType);

    List<SystemLog> selectByUserId(@Param("userType") Integer userType, @Param("userId") Integer userId);

    int insert(SystemLog log);

    int countAll();
}