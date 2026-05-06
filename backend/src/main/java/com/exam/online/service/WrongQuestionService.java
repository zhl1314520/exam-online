package com.exam.online.service;

import com.exam.online.entity.WrongQuestion;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.WrongQuestionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WrongQuestionService {

    @Autowired
    private WrongQuestionMapper wrongQuestionMapper;

    public WrongQuestion getWrongQuestionById(Integer wrongId) {
        return wrongQuestionMapper.selectById(wrongId);
    }

    public List<WrongQuestion> getAllWrongQuestions() {
        return wrongQuestionMapper.selectAll();
    }

    public Page<WrongQuestion> getWrongQuestionsByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<WrongQuestion>) wrongQuestionMapper.selectAll();
    }

    public List<WrongQuestion> getWrongQuestionsByStudentId(Integer studentId) {
        return wrongQuestionMapper.selectByStudentId(studentId);
    }

    public List<WrongQuestion> getWrongQuestionsByQuestionId(Integer questionId) {
        return wrongQuestionMapper.selectByQuestionId(questionId);
    }

    public List<WrongQuestion> getWrongQuestionsBySubjectId(Integer subjectId) {
        return wrongQuestionMapper.selectBySubjectId(subjectId);
    }

    public WrongQuestion getWrongQuestionByStudentIdAndQuestionId(Integer studentId, Integer questionId) {
        return wrongQuestionMapper.selectByStudentIdAndQuestionId(studentId, questionId);
    }

    @Transactional
    public WrongQuestion addWrongQuestion(WrongQuestion wrongQuestion) {
        WrongQuestion existing = wrongQuestionMapper.selectByStudentIdAndQuestionId(
                wrongQuestion.getStudentId(), wrongQuestion.getQuestionId());
        if (existing != null) {
            existing.setWrongCount(existing.getWrongCount() + 1);
            existing.setStudentAnswer(wrongQuestion.getStudentAnswer());
            existing.setLastWrongTime(LocalDateTime.now());
            wrongQuestionMapper.updateById(existing);
            return existing;
        }
        wrongQuestion.setWrongCount(1);
        wrongQuestion.setLastWrongTime(LocalDateTime.now());
        wrongQuestionMapper.insert(wrongQuestion);
        return wrongQuestion;
    }

    @Transactional
    public WrongQuestion updateWrongQuestion(WrongQuestion wrongQuestion) {
        WrongQuestion existing = wrongQuestionMapper.selectById(wrongQuestion.getWrongId());
        if (existing == null) {
            throw new BusinessException(404, "错题记录不存在");
        }
        wrongQuestionMapper.updateById(wrongQuestion);
        return wrongQuestionMapper.selectById(wrongQuestion.getWrongId());
    }

    @Transactional
    public void deleteWrongQuestion(Integer wrongId) {
        WrongQuestion existing = wrongQuestionMapper.selectById(wrongId);
        if (existing == null) {
            throw new BusinessException(404, "错题记录不存在");
        }
        wrongQuestionMapper.deleteById(wrongId);
    }

    @Transactional
    public void deleteWrongQuestionByStudentIdAndQuestionId(Integer studentId, Integer questionId) {
        wrongQuestionMapper.deleteByStudentIdAndQuestionId(studentId, questionId);
    }

    public int getWrongQuestionCount() {
        return wrongQuestionMapper.countAll();
    }

    public int getWrongQuestionCountByStudentId(Integer studentId) {
        return wrongQuestionMapper.countByStudentId(studentId);
    }

    public int getWrongQuestionCountBySubjectId(Integer subjectId) {
        return wrongQuestionMapper.countBySubjectId(subjectId);
    }
}
