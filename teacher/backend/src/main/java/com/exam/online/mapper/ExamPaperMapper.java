package com.exam.online.mapper;

import com.exam.online.entity.ExamPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamPaperMapper {

    ExamPaper selectById(@Param("paperId") Integer paperId);

    List<ExamPaper> selectAll();

    List<ExamPaper> selectByCreateBy(@Param("createBy") Integer createBy);

    List<ExamPaper> selectBySubjectId(@Param("subjectId") Integer subjectId);

    int insert(ExamPaper paper);

    int updateById(ExamPaper paper);

    int deleteById(@Param("paperId") Integer paperId);

    int countAll();
}