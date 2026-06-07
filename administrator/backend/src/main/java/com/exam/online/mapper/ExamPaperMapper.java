package com.exam.online.mapper;

import com.exam.online.entity.ExamPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamPaperMapper {

    ExamPaper selectById(@Param("paperId") Integer paperId);

    List<ExamPaper> selectAll();
}
