package com.exam.student.mapper;

import com.exam.student.entity.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamMapper {

    /**
     * 根据班级ID获取待考考试列表
     */
    List<Exam> selectPendingExamsByClassId(@Param("classId") Integer classId, @Param("studentId") Integer studentId);

    /**
     * 根据考试ID获取考试详情
     */
    Exam selectById(@Param("examId") Integer examId);

    /**
     * 根据班级ID获取所有考试（包括已结束）
     */
    List<Exam> selectExamsByClassId(@Param("classId") Integer classId);
}
