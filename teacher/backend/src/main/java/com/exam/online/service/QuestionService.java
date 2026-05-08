package com.exam.online.service;

import com.exam.online.entity.Question;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    public Question getById(Integer questionId) {
        return questionMapper.selectById(questionId);
    }

    public List<Question> getAll() {
        return questionMapper.selectAll();
    }

    public List<Question> getBySubjectId(Integer subjectId) {
        return questionMapper.selectBySubjectId(subjectId);
    }

    public List<Question> getByCreateBy(Integer createBy) {
        return questionMapper.selectByCreateBy(createBy);
    }

    public int countAll() {
        return questionMapper.countAll();
    }

    public int countBySubjectId(Integer subjectId) {
        return questionMapper.countBySubjectId(subjectId);
    }

    @Transactional
    public Question add(Question question) {
        questionMapper.insert(question);
        return question;
    }

    @Transactional
    public Question update(Question question) {
        Question existing = questionMapper.selectById(question.getQuestionId());
        if (existing == null) {
            throw new BusinessException(404, "题目不存在");
        }
        questionMapper.updateById(question);
        return questionMapper.selectById(question.getQuestionId());
    }

    @Transactional
    public void delete(Integer questionId) {
        Question existing = questionMapper.selectById(questionId);
        if (existing == null) {
            throw new BusinessException(404, "题目不存在");
        }
        questionMapper.deleteById(questionId);
    }
}