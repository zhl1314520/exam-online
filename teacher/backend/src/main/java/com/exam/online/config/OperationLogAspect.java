package com.exam.online.config;

import com.exam.online.annotation.OperationLog;
import com.exam.online.entity.Exam;
import com.exam.online.entity.ExamPaper;
import com.exam.online.entity.Question;
import com.exam.online.entity.SystemLog;
import com.exam.online.mapper.SystemLogMapper;
import com.exam.online.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private SystemLogMapper systemLogMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            try {
                String token = request.getHeader("Authorization");
                Integer teacherId = null;
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    Claims claims = jwtUtil.parseToken(token);
                    teacherId = claims.get("teacherId", Integer.class);
                }

                SystemLog log = new SystemLog();
                log.setUserType(2);
                log.setUserId(teacherId);
                log.setOperation(operationLog.operation());
                log.setIpAddress(getIpAddress(request));

                String detail = buildFriendlyDetail(operationLog.operation(), joinPoint.getArgs());
                log.setOperationDetail(detail);

                systemLogMapper.insert(log);
            } catch (Exception e) {
            }
        }

        return joinPoint.proceed();
    }

    private String buildFriendlyDetail(String operation, Object[] args) {
        if (args == null || args.length == 0) {
            return operation;
        }

        for (Object arg : args) {
            if (arg == null) continue;

            if (arg instanceof Exam) {
                Exam exam = (Exam) arg;
                String name = exam.getExamName();
                if (name != null && !name.isEmpty()) {
                    return name;
                }
            } else if (arg instanceof ExamPaper) {
                ExamPaper paper = (ExamPaper) arg;
                String name = paper.getPaperName();
                if (name != null && !name.isEmpty()) {
                    return name;
                }
            } else if (arg instanceof Question) {
                Question question = (Question) arg;
                String content = question.getQuestionContent();
                if (content != null && !content.isEmpty()) {
                    return content.length() > 30 ? content.substring(0, 30) + "..." : content;
                }
            } else if (arg instanceof Integer) {
                return "ID: " + arg;
            } else if (arg instanceof List) {
                List<?> list = (List<?>) arg;
                return list.size() + "条数据";
            } else if (arg instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) arg;
                if (map.containsKey("questionIds")) {
                    Object ids = map.get("questionIds");
                    if (ids instanceof List) {
                        return ((List<?>) ids).size() + "道题目";
                    }
                }
            }
        }

        return operation;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
