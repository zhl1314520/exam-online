package com.exam.student.mapper;

import com.exam.student.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StudentMapper {

    Student selectById(@Param("studentId") Integer studentId);

    Student selectByStudentNo(@Param("studentNo") String studentNo);

    int updateLastLoginTime(@Param("studentId") Integer studentId);

    int updatePassword(@Param("studentId") Integer studentId, @Param("password") String password);
}
