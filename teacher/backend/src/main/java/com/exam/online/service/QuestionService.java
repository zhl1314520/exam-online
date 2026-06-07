package com.exam.online.service;

import com.exam.online.entity.Question;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public int countByQuestionType(Integer questionType) {
        return questionMapper.countByQuestionType(questionType);
    }

    public List<Map<String, Object>> getQuestionTrendLast30Days() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(29);

        int baseCount = questionMapper.countBeforeDate(startDate);

        List<Map<String, Object>> dailyCounts = questionMapper.countDailyForLast30Days();
        Map<LocalDate, Integer> dailyMap = new LinkedHashMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Map<String, Object> row : dailyCounts) {
            LocalDate date = LocalDate.parse(row.get("date").toString(), fmt);
            int count = ((Number) row.get("count")).intValue();
            dailyMap.put(date, count);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        int cumulative = baseCount;
        for (int i = 0; i < 30; i++) {
            LocalDate date = startDate.plusDays(i);
            cumulative += dailyMap.getOrDefault(date, 0);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("date", date.format(fmt));
            item.put("totalCount", cumulative);
            result.add(item);
        }
        return result;
    }

    public List<Question> selectByConditions(String keyword, Integer subjectId, Integer questionType, Integer difficulty) {
        return questionMapper.selectByConditions(keyword, subjectId, questionType, difficulty);
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

    @Transactional
    public int batchImport(List<Question> questions, Integer teacherId) {
        if (questions == null || questions.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Question question : questions) {
            question.setCreateBy(teacherId);
            questionMapper.insert(question);
            count++;
        }
        return count;
    }
}
