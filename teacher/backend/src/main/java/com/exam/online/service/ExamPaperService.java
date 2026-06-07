package com.exam.online.service;

import com.exam.online.entity.ExamPaper;
import com.exam.online.entity.PaperQuestion;
import com.exam.online.entity.Question;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.ExamPaperMapper;
import com.exam.online.mapper.PaperQuestionMapper;
import com.exam.online.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExamPaperService {

    @Autowired
    private ExamPaperMapper examPaperMapper;

    @Autowired
    private PaperQuestionMapper paperQuestionMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public ExamPaper getById(Integer paperId) {
        ExamPaper paper = examPaperMapper.selectById(paperId);
        if (paper != null) {
            List<PaperQuestion> questions = paperQuestionMapper.selectByPaperId(paperId);
            paper.setQuestionCount(questions.size());
        }
        return paper;
    }

    public List<ExamPaper> getAll() {
        return examPaperMapper.selectAll();
    }

    public List<ExamPaper> getByCreateBy(Integer createBy) {
        return examPaperMapper.selectByCreateBy(createBy);
    }

    public List<ExamPaper> getBySubjectId(Integer subjectId) {
        return examPaperMapper.selectBySubjectId(subjectId);
    }

    public int countAll() {
        return examPaperMapper.countAll();
    }

    @Transactional
    public ExamPaper add(ExamPaper paper) {
        paper.setStatus(1);
        examPaperMapper.insert(paper);
        return paper;
    }

    @Transactional
    public ExamPaper update(ExamPaper paper) {
        ExamPaper existing = examPaperMapper.selectById(paper.getPaperId());
        if (existing == null) {
            throw new BusinessException(404, "试卷不存在");
        }
        examPaperMapper.updateById(paper);
        return examPaperMapper.selectById(paper.getPaperId());
    }

    @Transactional
    public void delete(Integer paperId) {
        ExamPaper existing = examPaperMapper.selectById(paperId);
        if (existing == null) {
            throw new BusinessException(404, "试卷不存在");
        }
        paperQuestionMapper.deleteByPaperId(paperId);
        examPaperMapper.deleteById(paperId);
    }

    public List<PaperQuestion> getPaperQuestions(Integer paperId) {
        return paperQuestionMapper.selectByPaperId(paperId);
    }

    @Transactional
    public void addQuestionToPaper(PaperQuestion pq) {
        paperQuestionMapper.insert(pq);
    }

    @Transactional
    public void addQuestionsToPaper(Integer paperId, List<Integer> questionIds) {
        int order = paperQuestionMapper.countByPaperId(paperId);
        for (Integer questionId : questionIds) {
            order++;
            Question question = questionMapper.selectById(questionId);
            PaperQuestion pq = new PaperQuestion();
            pq.setPaperId(paperId);
            pq.setQuestionId(questionId);
            pq.setQuestionOrder(order);
            pq.setScore(question != null ? question.getScore() : 2.0);
            paperQuestionMapper.insert(pq);
        }
    }

    @Transactional
    public void removeQuestionFromPaper(Integer id) {
        paperQuestionMapper.deleteById(id);
    }

    @Transactional
    public void removeQuestionFromPaperByQuestionId(Integer paperId, Integer questionId) {
        paperQuestionMapper.deleteByPaperIdAndQuestionId(paperId, questionId);
    }
}