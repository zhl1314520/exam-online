package com.exam.online.mapper;

import com.exam.online.entity.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamMapper {

    Exam selectById(@Param("examId") Integer examId);

    List<Exam> selectAll();

    List<Exam> selectByCreateBy(@Param("createBy") Integer createBy);

    List<Exam> selectByClassId(@Param("classId") Integer classId);

    int insert(Exam exam);

    int updateById(Exam exam);

    int deleteById(@Param("examId") Integer examId);

    int countAll();

    int countByStatus(@Param("status") Integer status);
}