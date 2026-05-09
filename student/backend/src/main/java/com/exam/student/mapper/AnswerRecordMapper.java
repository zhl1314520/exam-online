package com.exam.student.mapper;

import com.exam.student.entity.AnswerRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnswerRecordMapper {

    AnswerRecord selectById(@Param("answerId") Integer answerId);

    List<AnswerRecord> selectByRecordId(@Param("recordId") Integer recordId);

    AnswerRecord selectByRecordAndQuestion(@Param("recordId") Integer recordId, @Param("questionId") Integer questionId);

    int insert(AnswerRecord answerRecord);

    int updateById(AnswerRecord answerRecord);

    int upsert(AnswerRecord answerRecord);
}
