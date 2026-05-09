package com.exam.student.mapper;

import com.exam.student.entity.ExamRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamRecordMapper {

    ExamRecord selectById(@Param("recordId") Integer recordId);

    ExamRecord selectByExamAndStudent(@Param("examId") Integer examId, @Param("studentId") Integer studentId);

    List<ExamRecord> selectByStudentId(@Param("studentId") Integer studentId);

    List<ExamRecord> selectByStudentIdWithExam(@Param("studentId") Integer studentId);

    int insert(ExamRecord examRecord);

    int updateById(ExamRecord examRecord);

    int updateSubmit(@Param("recordId") Integer recordId);

    int incrementScreenSwitchCount(@Param("recordId") Integer recordId);
}
