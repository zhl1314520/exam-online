package com.exam.online.service;

import com.exam.online.entity.ExamPaper;
import com.exam.online.mapper.ExamPaperMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamPaperService {

    @Autowired
    private ExamPaperMapper examPaperMapper;

    public ExamPaper getById(Integer paperId) {
        return examPaperMapper.selectById(paperId);
    }

    public List<ExamPaper> getAll() {
        return examPaperMapper.selectAll();
    }
}
