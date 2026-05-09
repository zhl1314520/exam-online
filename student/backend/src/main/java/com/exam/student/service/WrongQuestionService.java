package com.exam.student.service;

import com.exam.student.entity.WrongQuestion;
import com.exam.student.mapper.WrongQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WrongQuestionService {

    @Autowired
    private WrongQuestionMapper wrongQuestionMapper;

    /**
     * 获取错题列表
     */
    public List<WrongQuestion> getWrongQuestions(Integer studentId) {
        return wrongQuestionMapper.selectByStudentId(studentId);
    }

    /**
     * 按科目筛选错题
     */
    public List<WrongQuestion> getWrongQuestionsBySubject(Integer studentId, Integer subjectId) {
        return wrongQuestionMapper.selectByStudentIdAndSubject(studentId, subjectId);
    }

    /**
     * 删除错题
     */
    public void deleteWrongQuestion(Integer wrongId, Integer studentId) {
        WrongQuestion wrongQuestion = wrongQuestionMapper.selectByStudentAndQuestion(studentId, wrongId);
        if (wrongQuestion != null) {
            wrongQuestionMapper.deleteById(wrongQuestion.getWrongId());
        }
    }
}
