package com.exam.student.service;

import com.exam.student.entity.*;
import com.exam.student.exception.BusinessException;
import com.exam.student.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ExamTakingService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ExamRecordMapper examRecordMapper;

    @Autowired
    private AnswerRecordMapper answerRecordMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private WrongQuestionMapper wrongQuestionMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_ANSWER_KEY = "exam:answer:";

    /**
     * 开始考试
     */
    @Transactional
    public Map<String, Object> startExam(Integer examId, Integer studentId, HttpServletRequest request) {
        // 检查考试是否有效
        Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            throw new BusinessException("考试不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(exam.getStartTime())) {
            throw new BusinessException("考试尚未开始");
        }
        if (now.isAfter(exam.getEndTime())) {
            throw new BusinessException("考试已结束");
        }

        // 检查是否已参加过
        ExamRecord existingRecord = examRecordMapper.selectByExamAndStudent(examId, studentId);
        if (existingRecord != null && existingRecord.getStatus() == 1) {
            throw new BusinessException("您已完成该考试");
        }

        ExamRecord record;
        if (existingRecord == null) {
            // 创建新考试记录
            record = new ExamRecord();
            record.setExamId(examId);
            record.setStudentId(studentId);
            record.setStartTime(now);
            record.setStatus(0);
            record.setScreenSwitchCount(0);
            record.setIpAddress(request.getRemoteAddr());
            examRecordMapper.insert(record);
        } else {
            record = existingRecord;
        }

        // 获取试卷题目
        List<Question> questions = questionMapper.selectByPaperId(exam.getPaperId());

        Map<String, Object> result = new HashMap<>();
        result.put("recordId", record.getRecordId());
        result.put("exam", exam);
        result.put("questions", questions);

        // 计算剩余时间
        long elapsedMinutes = ChronoUnit.MINUTES.between(record.getStartTime(), now);
        int remainingMinutes = exam.getDuration() - (int) elapsedMinutes;
        if (remainingMinutes < 0) {
            remainingMinutes = 0;
        }
        result.put("remainingTime", remainingMinutes * 60); // 返回秒数

        return result;
    }

    /**
     * 获取试卷题目
     */
    public List<Question> getQuestions(Integer recordId, Integer studentId) {
        ExamRecord record = examRecordMapper.selectById(recordId);
        if (record == null || !record.getStudentId().equals(studentId)) {
            throw new BusinessException("考试记录不存在");
        }

        Exam exam = examMapper.selectById(record.getExamId());
        return questionMapper.selectByPaperIdWithAnswers(exam.getPaperId(), recordId);
    }

    /**
     * 保存答案（保存到数据库和Redis）
     */
    @Transactional
    public void saveAnswer(Integer recordId, Integer questionId, String answer, Integer studentId) {
        ExamRecord record = examRecordMapper.selectById(recordId);
        if (record == null || !record.getStudentId().equals(studentId)) {
            throw new BusinessException("考试记录不存在");
        }

        // 保存到数据库
        AnswerRecord answerRecord = answerRecordMapper.selectByRecordAndQuestion(recordId, questionId);
        if (answerRecord == null) {
            answerRecord = new AnswerRecord();
            answerRecord.setRecordId(recordId);
            answerRecord.setQuestionId(questionId);
            answerRecord.setStudentAnswer(answer);
            answerRecordMapper.insert(answerRecord);
        } else {
            answerRecord.setStudentAnswer(answer);
            answerRecordMapper.updateById(answerRecord);
        }

        // 保存到Redis（用于实时同步）
        try {
            String redisKey = REDIS_ANSWER_KEY + recordId;
            redisTemplate.opsForHash().put(redisKey, String.valueOf(questionId), answer);
        } catch (Exception e) {
            // Redis不可用时忽略，数据库已保存
        }
    }

    /**
     * 从Redis获取临时答案
     */
    public Map<String, String> getTempAnswers(Integer recordId) {
        try {
            String redisKey = REDIS_ANSWER_KEY + recordId;
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(redisKey);
            Map<String, String> answers = new HashMap<>();
            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                answers.put(entry.getKey().toString(), entry.getValue().toString());
            }
            return answers;
        } catch (Exception e) {
            // Redis不可用时从数据库读取
            Map<String, String> answers = new HashMap<>();
            List<AnswerRecord> records = answerRecordMapper.selectByRecordId(recordId);
            for (AnswerRecord ar : records) {
                if (ar.getStudentAnswer() != null) {
                    answers.put(String.valueOf(ar.getQuestionId()), ar.getStudentAnswer());
                }
            }
            return answers;
        }
    }

    /**
     * 提交试卷
     */
    @Transactional
    public Map<String, Object> submitExam(Integer recordId, Integer studentId) {
        ExamRecord record = examRecordMapper.selectById(recordId);
        if (record == null || !record.getStudentId().equals(studentId)) {
            throw new BusinessException("考试记录不存在");
        }

        if (record.getStatus() == 1) {
            throw new BusinessException("考试已提交");
        }

        // 获取试卷所有题目
        Exam exam = examMapper.selectById(record.getExamId());
        List<Question> questions = questionMapper.selectByPaperId(exam.getPaperId());

        // 获取已有答案记录
        List<AnswerRecord> existingAnswers = answerRecordMapper.selectByRecordId(recordId);
        Map<Integer, AnswerRecord> answerMap = new HashMap<>();
        for (AnswerRecord ar : existingAnswers) {
            answerMap.put(ar.getQuestionId(), ar);
        }

        double objectiveScore = 0;
        double subjectiveScore = 0;
        boolean hasSubjective = false;

        for (Question question : questions) {
            AnswerRecord answer = answerMap.get(question.getQuestionId());
            boolean isCorrect = false;
            double score = 0;
            String studentAnswer = answer != null ? answer.getStudentAnswer() : null;

            if (question.getQuestionType() <= 3) {
                // 客观题自动评分
                if (studentAnswer != null && !studentAnswer.trim().isEmpty()) {
                    isCorrect = checkAnswer(question.getQuestionType(), studentAnswer, question.getCorrectAnswer());
                    if (isCorrect) {
                        score = question.getScore() != null ? question.getScore() : 0;
                        objectiveScore += score;
                    } else {
                        saveWrongQuestion(studentId, question, studentAnswer);
                    }
                }
            } else {
                // 主观题待教师批改
                hasSubjective = true;
                score = 0;
            }

            if (answer == null) {
                // 未作答也要创建记录
                answer = new AnswerRecord();
                answer.setRecordId(recordId);
                answer.setQuestionId(question.getQuestionId());
                answer.setStudentAnswer(studentAnswer);
                answer.setIsCorrect(isCorrect ? 1 : 0);
                answer.setScore(score);
                answerRecordMapper.insert(answer);
            } else {
                answer.setIsCorrect(isCorrect ? 1 : 0);
                answer.setScore(score);
                answerRecordMapper.updateById(answer);
            }
        }

        // 更新考试记录
        record.setTotalScore(objectiveScore + subjectiveScore);
        record.setObjectiveScore(objectiveScore);
        record.setSubjectiveScore(subjectiveScore);
        record.setStatus(1);
        record.setSubmitTime(LocalDateTime.now());
        examRecordMapper.updateById(record);

        // 清除Redis缓存
        try {
            redisTemplate.delete(REDIS_ANSWER_KEY + recordId);
        } catch (Exception e) {
            // Redis不可用时忽略
        }

        Map<String, Object> result = new HashMap<>();
        result.put("recordId", recordId);
        result.put("totalScore", objectiveScore + subjectiveScore);
        result.put("objectiveScore", objectiveScore);
        result.put("subjectiveScore", subjectiveScore);
        result.put("hasSubjective", hasSubjective);

        return result;
    }

    /**
     * 检查答案是否正确
     */
    private boolean checkAnswer(Integer questionType, String studentAnswer, String correctAnswer) {
        if (studentAnswer == null || correctAnswer == null) {
            return false;
        }

        if (questionType == 1 || questionType == 3) { // 单选、判断
            return studentAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
        } else if (questionType == 2) { // 多选
            // 排序后比较
            List<String> studentList = Arrays.asList(studentAnswer.split("\\|"));
            List<String> correctList = Arrays.asList(correctAnswer.split("\\|"));
            Collections.sort(studentList);
            Collections.sort(correctList);
            return studentList.equals(correctList);
        }
        return false;
    }

    /**
     * 保存错题
     */
    private void saveWrongQuestion(Integer studentId, Question question, String studentAnswer) {
        WrongQuestion existing = wrongQuestionMapper.selectByStudentAndQuestion(studentId, question.getQuestionId());
        if (existing == null) {
            WrongQuestion wrongQuestion = new WrongQuestion();
            wrongQuestion.setStudentId(studentId);
            wrongQuestion.setQuestionId(question.getQuestionId());
            wrongQuestion.setStudentAnswer(studentAnswer);
            wrongQuestion.setCorrectAnswer(question.getCorrectAnswer());
            wrongQuestion.setSubjectId(question.getSubjectId());
            wrongQuestion.setWrongCount(1);
            wrongQuestion.setLastWrongTime(LocalDateTime.now());
            wrongQuestionMapper.insert(wrongQuestion);
        } else {
            existing.setStudentAnswer(studentAnswer);
            existing.setCorrectAnswer(question.getCorrectAnswer());
            existing.setWrongCount(existing.getWrongCount() + 1);
            existing.setLastWrongTime(LocalDateTime.now());
            wrongQuestionMapper.updateById(existing);
        }
    }

    /**
     * 自动交卷（时间到）
     */
    public void autoSubmit(Integer recordId, Integer studentId) {
        submitExam(recordId, studentId);
    }

    /**
     * 切屏次数+1
     */
    public void incrementScreenSwitch(Integer recordId, Integer studentId) {
        ExamRecord record = examRecordMapper.selectById(recordId);
        if (record != null && record.getStudentId().equals(studentId)) {
            examRecordMapper.incrementScreenSwitchCount(recordId);
        }
    }

    /**
     * 获取切屏次数
     */
    public int getScreenSwitchCount(Integer recordId, Integer studentId) {
        ExamRecord record = examRecordMapper.selectById(recordId);
        if (record != null && record.getStudentId().equals(studentId)) {
            return record.getScreenSwitchCount();
        }
        return 0;
    }
}
