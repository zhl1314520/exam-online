package com.exam.online.service;

import com.exam.online.entity.ExamPaper;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.ExamPaperMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamPaperService {

    @Autowired
    private ExamPaperMapper examPaperMapper;

    public ExamPaper getExamPaperById(Integer paperId) {
        return examPaperMapper.selectById(paperId);
    }

    public List<ExamPaper> getAllExamPapers() {
        return examPaperMapper.selectAll();
    }

    public Page<ExamPaper> getExamPapersByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<ExamPaper>) examPaperMapper.selectAll();
    }

    public List<ExamPaper> getExamPapersBySubjectId(Integer subjectId) {
        return examPaperMapper.selectBySubjectId(subjectId);
    }

    public List<ExamPaper> getExamPapersByCreateBy(Integer createBy) {
        return examPaperMapper.selectByCreateBy(createBy);
    }

    public List<ExamPaper> getExamPapersByStatus(Integer status) {
        return examPaperMapper.selectByStatus(status);
    }

    @Transactional
    public ExamPaper addExamPaper(ExamPaper examPaper) {
        examPaper.setCreateTime(LocalDateTime.now());
        examPaper.setStatus(1);
        examPaperMapper.insert(examPaper);
        return examPaper;
    }

    @Transactional
    public ExamPaper updateExamPaper(ExamPaper examPaper) {
        ExamPaper existing = examPaperMapper.selectById(examPaper.getPaperId());
        if (existing == null) {
            throw new BusinessException(404, "试卷不存在");
        }
        examPaperMapper.updateById(examPaper);
        return examPaperMapper.selectById(examPaper.getPaperId());
    }

    @Transactional
    public void deleteExamPaper(Integer paperId) {
        ExamPaper existing = examPaperMapper.selectById(paperId);
        if (existing == null) {
            throw new BusinessException(404, "试卷不存在");
        }
        examPaperMapper.deleteById(paperId);
    }

    public int getExamPaperCount() {
        return examPaperMapper.countAll();
    }

    public int getExamPaperCountBySubjectId(Integer subjectId) {
        return examPaperMapper.countBySubjectId(subjectId);
    }
}
