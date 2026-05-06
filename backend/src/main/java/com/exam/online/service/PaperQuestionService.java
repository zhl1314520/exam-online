package com.exam.online.service;

import com.exam.online.entity.PaperQuestion;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.PaperQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaperQuestionService {

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    public PaperQuestion getPaperQuestionById(Integer id) {
        return paperQuestionMapper.selectById(id);
    }

    public List<PaperQuestion> getPaperQuestionsByPaperId(Integer paperId) {
        return paperQuestionMapper.selectByPaperId(paperId);
    }

    public List<PaperQuestion> getPaperQuestionsByQuestionId(Integer questionId) {
        return paperQuestionMapper.selectByQuestionId(questionId);
    }

    @Transactional
    public PaperQuestion addPaperQuestion(PaperQuestion paperQuestion) {
        paperQuestionMapper.insert(paperQuestion);
        return paperQuestion;
    }

    @Transactional
    public PaperQuestion updatePaperQuestion(PaperQuestion paperQuestion) {
        PaperQuestion existing = paperQuestionMapper.selectById(paperQuestion.getId());
        if (existing == null) {
            throw new BusinessException(404, "试卷题目关联不存在");
        }
        paperQuestionMapper.updateById(paperQuestion);
        return paperQuestionMapper.selectById(paperQuestion.getId());
    }

    @Transactional
    public void deletePaperQuestion(Integer id) {
        PaperQuestion existing = paperQuestionMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "试卷题目关联不存在");
        }
        paperQuestionMapper.deleteById(id);
    }

    @Transactional
    public void deletePaperQuestionsByPaperId(Integer paperId) {
        paperQuestionMapper.deleteByPaperId(paperId);
    }

    public int getPaperQuestionCountByPaperId(Integer paperId) {
        return paperQuestionMapper.countByPaperId(paperId);
    }
}
