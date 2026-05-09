package com.exam.student.mapper;

import com.exam.student.entity.WrongQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WrongQuestionMapper {

    List<WrongQuestion> selectByStudentId(@Param("studentId") Integer studentId);

    List<WrongQuestion> selectByStudentIdAndSubject(@Param("studentId") Integer studentId, @Param("subjectId") Integer subjectId);

    WrongQuestion selectByStudentAndQuestion(@Param("studentId") Integer studentId, @Param("questionId") Integer questionId);

    int insert(WrongQuestion wrongQuestion);

    int updateById(WrongQuestion wrongQuestion);

    int deleteById(@Param("wrongId") Integer wrongId);

    int incrementWrongCount(@Param("wrongId") Integer wrongId);
}
