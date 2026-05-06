package com.exam.online.mapper;

import com.exam.online.entity.PaperQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaperQuestionMapper {

    PaperQuestion selectById(@Param("id") Integer id);

    List<PaperQuestion> selectByPaperId(@Param("paperId") Integer paperId);

    List<PaperQuestion> selectByQuestionId(@Param("questionId") Integer questionId);

    int insert(PaperQuestion paperQuestion);

    int updateById(PaperQuestion paperQuestion);

    int deleteById(@Param("id") Integer id);

    int deleteByPaperId(@Param("paperId") Integer paperId);

    int countByPaperId(@Param("paperId") Integer paperId);
}