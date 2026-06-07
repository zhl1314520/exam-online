package com.exam.online.mapper;

import com.exam.online.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeacherMapper {

    Teacher selectById(@Param("teacherId") Integer teacherId);

    Teacher selectByTeacherNo(@Param("teacherNo") String teacherNo);

    List<Teacher> selectAll();

    List<Teacher> selectByDepartmentId(@Param("departmentId") Integer departmentId);

    List<Teacher> selectByCondition(@Param("keyword") String keyword, @Param("departmentId") Integer departmentId);

    int insert(Teacher teacher);

    int updateById(Teacher teacher);

    int updateLastLoginTime(@Param("teacherId") Integer teacherId);

    int deleteById(@Param("teacherId") Integer teacherId);

    int countAll();

    int countByDepartmentId(@Param("departmentId") Integer departmentId);

    int countByCondition(@Param("keyword") String keyword, @Param("departmentId") Integer departmentId);
}