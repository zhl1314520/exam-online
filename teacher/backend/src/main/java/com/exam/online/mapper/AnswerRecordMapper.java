package com.exam.online.mapper;

import com.exam.online.entity.AnswerRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnswerRecordMapper {

    List<AnswerRecord> selectByRecordId(@Param("recordId") Integer recordId);

    int countCorrectByRecordId(@Param("recordId") Integer recordId);

    Double sumScoreByRecordId(@Param("recordId") Integer recordId);

    int updateScore(@Param("answerId") Integer answerId, @Param("score") Double score);
}