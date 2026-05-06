package com.exam.online.service;

import com.exam.online.entity.Question;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.QuestionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    public Question getQuestionById(Integer questionId) {
        return questionMapper.selectById(questionId);
    }

    public List<Question> getAllQuestions() {
        return questionMapper.selectAll();
    }

    public Page<Question> getQuestionsByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Question>) questionMapper.selectAll();
    }

    public List<Question> getQuestionsBySubjectId(Integer subjectId) {
        return questionMapper.selectBySubjectId(subjectId);
    }

    public List<Question> getQuestionsByType(Integer questionType) {
        return questionMapper.selectByQuestionType(questionType);
    }

    public List<Question> getQuestionsByDifficulty(Integer difficulty) {
        return questionMapper.selectByDifficulty(difficulty);
    }

    public List<Question> getQuestionsByCreateBy(Integer createBy) {
        return questionMapper.selectByCreateBy(createBy);
    }

    @Transactional
    public Question addQuestion(Question question) {
        question.setCreateTime(LocalDateTime.now());
        question.setUpdateTime(LocalDateTime.now());
        questionMapper.insert(question);
        return question;
    }

    @Transactional
    public Question updateQuestion(Question question) {
        Question existing = questionMapper.selectById(question.getQuestionId());
        if (existing == null) {
            throw new BusinessException(404, "试题不存在");
        }
        question.setUpdateTime(LocalDateTime.now());
        questionMapper.updateById(question);
        return questionMapper.selectById(question.getQuestionId());
    }

    @Transactional
    public void deleteQuestion(Integer questionId) {
        Question existing = questionMapper.selectById(questionId);
        if (existing == null) {
            throw new BusinessException(404, "试题不存在");
        }
        questionMapper.deleteById(questionId);
    }

    public int getQuestionCount() {
        return questionMapper.countAll();
    }

    public int getQuestionCountBySubjectId(Integer subjectId) {
        return questionMapper.countBySubjectId(subjectId);
    }

    public int getQuestionCountByType(Integer questionType) {
        return questionMapper.countByQuestionType(questionType);
    }
}
