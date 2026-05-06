package com.exam.online.mapper;

import com.exam.online.entity.AnswerRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnswerRecordMapper {

    AnswerRecord selectById(@Param("answerId") Integer answerId);

    List<AnswerRecord> selectAll();

    List<AnswerRecord> selectByRecordId(@Param("recordId") Integer recordId);

    List<AnswerRecord> selectByQuestionId(@Param("questionId") Integer questionId);

    AnswerRecord selectByRecordIdAndQuestionId(@Param("recordId") Integer recordId, @Param("questionId") Integer questionId);

    int insert(AnswerRecord answerRecord);

    int updateById(AnswerRecord answerRecord);

    int deleteById(@Param("answerId") Integer answerId);

    int deleteByRecordId(@Param("recordId") Integer recordId);

    int countByRecordId(@Param("recordId") Integer recordId);

    Double selectTotalScoreByRecordId(@Param("recordId") Integer recordId);
}