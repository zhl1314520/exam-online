package com.exam.online.mapper;

import com.exam.online.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeacherMapper {

    Teacher selectById(@Param("teacherId") Integer teacherId);

    Teacher selectByTeacherNo(@Param("teacherNo") String teacherNo);

    int updateLastLoginTime(@Param("teacherId") Integer teacherId);

    Teacher selectByPrimaryKey(@Param("teacherId") Integer teacherId);

    int updatePassword(@Param("teacherId") Integer teacherId, @Param("password") String password);
}