package com.exam.online.mapper;

import com.exam.online.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {

    Student selectById(@Param("studentId") Integer studentId);

    Student selectByStudentNo(@Param("studentNo") String studentNo);

    List<Student> selectAll();

    List<Student> selectByClassId(@Param("classId") Integer classId);

    int insert(Student student);

    int updateById(Student student);

    int updateLastLoginTime(@Param("studentId") Integer studentId);

    int deleteById(@Param("studentId") Integer studentId);

    int countAll();

    int countByClassId(@Param("classId") Integer classId);
}