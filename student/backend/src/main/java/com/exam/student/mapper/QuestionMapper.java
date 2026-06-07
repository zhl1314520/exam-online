package com.exam.student.mapper;

import com.exam.student.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {

    Question selectById(@Param("questionId") Integer questionId);

    /**
     * 根据试卷ID获取题目列表
     */
    List<Question> selectByPaperId(@Param("paperId") Integer paperId);

    /**
     * 根据试卷ID获取题目列表（包含学生的答案）
     */
    List<Question> selectByPaperIdWithAnswers(@Param("paperId") Integer paperId, @Param("recordId") Integer recordId);
}
