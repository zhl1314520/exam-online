package com.exam.online.mapper;

import com.exam.online.entity.SystemLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemLogMapper {

    int insert(SystemLog systemLog);

    List<SystemLog> selectRecent(@Param("userId") Integer userId, @Param("limit") Integer limit);
}
