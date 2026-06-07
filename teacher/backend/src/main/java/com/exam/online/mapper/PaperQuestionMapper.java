package com.exam.online.mapper;

import com.exam.online.entity.PaperQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaperQuestionMapper {

    List<PaperQuestion> selectByPaperId(@Param("paperId") Integer paperId);

    PaperQuestion selectByPaperIdAndQuestionId(@Param("paperId") Integer paperId, @Param("questionId") Integer questionId);

    int insert(PaperQuestion pq);

    int updateById(PaperQuestion pq);

    int deleteByPaperId(@Param("paperId") Integer paperId);

    int deleteById(@Param("id") Integer id);

    int deleteByPaperIdAndQuestionId(@Param("paperId") Integer paperId, @Param("questionId") Integer questionId);

    int countByPaperId(@Param("paperId") Integer paperId);
}