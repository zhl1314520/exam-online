package com.exam.online.mapper;

import com.exam.online.entity.Subject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubjectMapper {

    Subject selectById(@Param("subjectId") Integer subjectId);

    Subject selectBySubjectCode(@Param("subjectCode") String subjectCode);

    List<Subject> selectAll();

    List<Subject> selectByDepartmentId(@Param("departmentId") Integer departmentId);

    int insert(Subject subject);

    int updateById(Subject subject);

    int deleteById(@Param("subjectId") Integer subjectId);

    int countAll();

    int countByDepartmentId(@Param("departmentId") Integer departmentId);
}