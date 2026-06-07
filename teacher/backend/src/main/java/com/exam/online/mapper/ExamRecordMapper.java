package com.exam.online.mapper;

import com.exam.online.entity.ExamRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ExamRecordMapper {

    ExamRecord selectById(@Param("recordId") Integer recordId);

    List<ExamRecord> selectByExamId(@Param("examId") Integer examId);

    List<ExamRecord> selectAll();

    List<ExamRecord> selectByStudentId(@Param("studentId") Integer studentId);

    int countByExamId(@Param("examId") Integer examId);

    int countSubmittedByExamId(@Param("examId") Integer examId);

    Double getMaxScoreByExamId(@Param("examId") Integer examId);

    Double getMinScoreByExamId(@Param("examId") Integer examId);

    Double getAvgScoreByExamId(@Param("examId") Integer examId);

    Double getPassRateByExamId(@Param("examId") Integer examId, @Param("passScore") Double passScore);


    int countSubmittedInDateRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    int countDistinctStudents();

    int update(ExamRecord record);
}