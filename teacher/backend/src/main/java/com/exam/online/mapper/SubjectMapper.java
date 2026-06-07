package com.exam.online.mapper;

import com.exam.online.entity.Subject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubjectMapper {

    Subject selectById(@Param("subjectId") Integer subjectId);

    List<Subject> selectAll();

    List<Subject> selectByTeacherId(@Param("teacherId") Integer teacherId);
}