package com.exam.online.mapper;

import com.exam.online.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {

    Question selectById(@Param("questionId") Integer questionId);

    List<Question> selectAll();

    List<Question> selectBySubjectId(@Param("subjectId") Integer subjectId);

    List<Question> selectByCreateBy(@Param("createBy") Integer createBy);

    int insert(Question question);

    int updateById(Question question);

    int deleteById(@Param("questionId") Integer questionId);

    int countAll();

    int countBySubjectId(@Param("subjectId") Integer subjectId);

    int countByQuestionType(@Param("questionType") Integer questionType);

    java.util.List<java.util.Map<String, Object>> countDailyForLast30Days();

    int countBeforeDate(@Param("date") java.time.LocalDate date);

    List<Question> selectByConditions(
            @Param("keyword") String keyword,
            @Param("subjectId") Integer subjectId,
            @Param("questionType") Integer questionType,
            @Param("difficulty") Integer difficulty);
}