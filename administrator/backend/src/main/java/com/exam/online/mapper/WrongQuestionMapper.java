package com.exam.online.mapper;

import com.exam.online.entity.WrongQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WrongQuestionMapper {

    WrongQuestion selectById(@Param("wrongId") Integer wrongId);

    List<WrongQuestion> selectAll();

    List<WrongQuestion> selectByStudentId(@Param("studentId") Integer studentId);

    List<WrongQuestion> selectByQuestionId(@Param("questionId") Integer questionId);

    List<WrongQuestion> selectBySubjectId(@Param("subjectId") Integer subjectId);

    WrongQuestion selectByStudentIdAndQuestionId(@Param("studentId") Integer studentId, @Param("questionId") Integer questionId);

    int insert(WrongQuestion wrongQuestion);

    int updateById(WrongQuestion wrongQuestion);

    int deleteById(@Param("wrongId") Integer wrongId);

    int deleteByStudentIdAndQuestionId(@Param("studentId") Integer studentId, @Param("questionId") Integer questionId);

    int countAll();

    int countByStudentId(@Param("studentId") Integer studentId);

    int countBySubjectId(@Param("subjectId") Integer subjectId);
}