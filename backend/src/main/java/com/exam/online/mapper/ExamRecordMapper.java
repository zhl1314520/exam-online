package com.exam.online.mapper;

import com.exam.online.entity.ExamRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamRecordMapper {

    ExamRecord selectById(@Param("recordId") Integer recordId);

    List<ExamRecord> selectAll();

    List<ExamRecord> selectByExamId(@Param("examId") Integer examId);

    List<ExamRecord> selectByStudentId(@Param("studentId") Integer studentId);

    ExamRecord selectByExamIdAndStudentId(@Param("examId") Integer examId, @Param("studentId") Integer studentId);

    int insert(ExamRecord examRecord);

    int updateById(ExamRecord examRecord);

    int deleteById(@Param("recordId") Integer recordId);

    int countAll();

    int countByExamId(@Param("examId") Integer examId);

    int countByStudentId(@Param("studentId") Integer studentId);

    Double selectAverageScoreByExamId(@Param("examId") Integer examId);

    Double selectMaxScoreByExamId(@Param("examId") Integer examId);

    Double selectMinScoreByExamId(@Param("examId") Integer examId);
}