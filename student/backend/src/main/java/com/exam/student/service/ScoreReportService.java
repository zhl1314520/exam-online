package com.exam.student.service;

import com.exam.student.entity.ExamRecord;
import com.exam.student.entity.WrongQuestion;
import com.exam.student.mapper.ExamRecordMapper;
import com.exam.student.mapper.WrongQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreReportService {

    @Autowired
    private ExamRecordMapper examRecordMapper;

    @Autowired
    private WrongQuestionMapper wrongQuestionMapper;

    /**
     * 获取成绩统计
     */
    public Map<String, Object> getScoreStatistics(Integer studentId) {
        List<ExamRecord> records = examRecordMapper.selectByStudentIdWithExam(studentId);

        Map<String, Object> stats = new HashMap<>();

        if (records.isEmpty()) {
            stats.put("totalExams", 0);
            stats.put("completedExams", 0);
            stats.put("averageScore", 0.0);
            stats.put("highestScore", 0.0);
            stats.put("lowestScore", 0.0);
            stats.put("passCount", 0);
            stats.put("passRate", 0.0);
            return stats;
        }

        List<ExamRecord> completed = records.stream()
                .filter(r -> r.getStatus() == 1)
                .collect(Collectors.toList());

        double totalScore = completed.stream()
                .mapToDouble(ExamRecord::getTotalScore)
                .sum();
        double maxScore = completed.stream()
                .mapToDouble(ExamRecord::getTotalScore)
                .max()
                .orElse(0.0);
        double minScore = completed.stream()
                .mapToDouble(ExamRecord::getTotalScore)
                .min()
                .orElse(0.0);

        long passCount = completed.stream()
                .filter(r -> r.getTotalScore() >= r.getPassScore())
                .count();

        double passRate = completed.isEmpty() ? 0.0 : ((double) passCount / completed.size() * 100);

        stats.put("totalExams", records.size());
        stats.put("completedExams", completed.size());
        stats.put("averageScore", completed.isEmpty() ? 0.0 : totalScore / completed.size());
        stats.put("highestScore", maxScore);
        stats.put("lowestScore", minScore);
        stats.put("passCount", passCount);
        stats.put("passRate", passRate);

        return stats;
    }

    /**
     * 获取成绩趋势数据
     */
    public List<Map<String, Object>> getScoreTrend(Integer studentId) {
        List<ExamRecord> records = examRecordMapper.selectByStudentIdWithExam(studentId);

        return records.stream()
                .filter(r -> r.getStatus() == 1)
                .sorted(Comparator.comparing(ExamRecord::getStartTime))
                .map(r -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("examName", r.getExamName());
                    data.put("subjectName", r.getPaperName());
                    data.put("score", r.getTotalScore());
                    data.put("totalScore", r.getTotalScoreOfPaper());
                    data.put("date", r.getStartTime());
                    data.put("passScore", r.getPassScore());
                    return data;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取知识点掌握情况（雷达图数据）
     */
    public Map<String, Object> getKnowledgeRadar(Integer studentId) {
        // 获取错题数据
        List<WrongQuestion> wrongQuestions = wrongQuestionMapper.selectByStudentId(studentId);

        // 按知识点统计
        Map<String, Integer> knowledgeWrongCount = new HashMap<>();
        for (WrongQuestion wq : wrongQuestions) {
            String knowledgePoint = wq.getKnowledgePoint();
            if (knowledgePoint != null) {
                knowledgeWrongCount.merge(knowledgePoint, 1, Integer::sum);
            }
        }

        // 计算掌握度（错题越少掌握度越高）
        List<String> knowledgePoints = new ArrayList<>(knowledgeWrongCount.keySet());
        List<Integer> wrongCounts = knowledgePoints.stream()
                .map(knowledgeWrongCount::get)
                .collect(Collectors.toList());

        // 计算掌握度百分比（最多5次错误为0%，0次错误为100%）
        List<Integer> mastery = wrongCounts.stream()
                .map(count -> Math.max(0, 100 - count * 20))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("knowledgePoints", knowledgePoints);
        result.put("wrongCounts", wrongCounts);
        result.put("mastery", mastery);

        return result;
    }

    /**
     * 获取科目成绩分布
     */
    public List<Map<String, Object>> getSubjectScoreDistribution(Integer studentId) {
        List<ExamRecord> records = examRecordMapper.selectByStudentIdWithExam(studentId);

        Map<String, List<ExamRecord>> bySubject = records.stream()
                .filter(r -> r.getStatus() == 1)
                .collect(Collectors.groupingBy(ExamRecord::getPaperName));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<ExamRecord>> entry : bySubject.entrySet()) {
            List<ExamRecord> subjectRecords = entry.getValue();
            double avgScore = subjectRecords.stream()
                    .mapToDouble(ExamRecord::getTotalScore)
                    .average()
                    .orElse(0.0);

            Map<String, Object> data = new HashMap<>();
            data.put("subjectName", entry.getKey());
            data.put("examCount", subjectRecords.size());
            data.put("averageScore", avgScore);
            result.add(data);
        }

        return result;
    }
}
