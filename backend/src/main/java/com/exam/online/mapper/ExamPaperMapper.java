package com.exam.online.mapper;

import com.exam.online.entity.ExamPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamPaperMapper {

    ExamPaper selectById(@Param("paperId") Integer paperId);

    List<ExamPaper> selectAll();

    List<ExamPaper> selectBySubjectId(@Param("subjectId") Integer subjectId);

    List<ExamPaper> selectByCreateBy(@Param("createBy") Integer createBy);

    List<ExamPaper> selectByStatus(@Param("status") Integer status);

    int insert(ExamPaper examPaper);

    int updateById(ExamPaper examPaper);

    int deleteById(@Param("paperId") Integer paperId);

    int countAll();

    int countBySubjectId(@Param("subjectId") Integer subjectId);
}