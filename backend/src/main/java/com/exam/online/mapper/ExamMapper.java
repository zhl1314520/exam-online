package com.exam.online.mapper;

import com.exam.online.entity.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamMapper {

    Exam selectById(@Param("examId") Integer examId);

    List<Exam> selectAll();

    List<Exam> selectByClassId(@Param("classId") Integer classId);

    List<Exam> selectByPaperId(@Param("paperId") Integer paperId);

    List<Exam> selectByCreateBy(@Param("createBy") Integer createBy);

    List<Exam> selectByStatus(@Param("status") Integer status);

    int insert(Exam exam);

    int updateById(Exam exam);

    int deleteById(@Param("examId") Integer examId);

    int countAll();

    int countByClassId(@Param("classId") Integer classId);

    int countByStatus(@Param("status") Integer status);
}