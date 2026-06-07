package com.exam.online.service;

import com.exam.online.entity.Student;
import com.exam.online.exception.BusinessException;
import com.exam.online.mapper.StudentMapper;
import com.exam.online.mapper.ClassMapper;
import com.exam.online.utils.PasswordUtil;
import com.exam.online.dto.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ClassMapper classMapper;

    public Student getById(Integer studentId) {
        return studentMapper.selectById(studentId);
    }

    public List<Student> getAll() {
        return studentMapper.selectAll();
    }

    public PageDTO<Student> getByPage(Integer pageNum, Integer pageSize, String keyword, Integer classId) {
        List<Student> allStudents = studentMapper.selectAll();

        List<Student> filtered = allStudents.stream()
                .filter(s -> {
                    if (keyword != null && !keyword.isEmpty()) {
                        return (s.getStudentNo() != null && s.getStudentNo().contains(keyword)) ||
                               (s.getRealName() != null && s.getRealName().contains(keyword)) ||
                               (s.getClassName() != null && s.getClassName().contains(keyword));
                    }
                    return true;
                })
                .filter(s -> {
                    if (classId != null) {
                        return s.getClassId() != null && s.getClassId().equals(classId);
                    }
                    return true;
                })
                .collect(Collectors.toList());

        long total = filtered.size();
        int fromIndex = (pageNum - 1) * pageSize;
        if (fromIndex >= filtered.size()) {
            return createEmptyPage(pageNum, pageSize);
        }
        int toIndex = Math.min(fromIndex + pageSize, filtered.size());
        List<Student> pageList = filtered.subList(fromIndex, toIndex);

        PageDTO<Student> dto = new PageDTO<>();
        dto.setTotal(total);
        dto.setPageNum(pageNum);
        dto.setPageSize(pageSize);
        dto.setPages((int) Math.ceil((double) total / pageSize));
        dto.setList(pageList);
        return dto;
    }

    public int countAll() {
        return studentMapper.countAll();
    }

    private PageDTO<Student> createEmptyPage(Integer pageNum, Integer pageSize) {
        PageDTO<Student> dto = new PageDTO<>();
        dto.setTotal(0L);
        dto.setPageNum(pageNum);
        dto.setPageSize(pageSize);
        dto.setPages(0);
        dto.setList(List.of());
        return dto;
    }

    @Transactional
    public Student add(Student student) {
        Student existing = studentMapper.selectByStudentNo(student.getStudentNo());
        if (existing != null) {
            throw new BusinessException(400, "学号已存在");
        }
        String password = student.getPassword();
        if (password == null || password.isEmpty()) {
            password = "123456";
        }
        student.setPassword(PasswordUtil.md5(password));
        student.setStatus((byte) 1);
        studentMapper.insert(student);

        if (student.getClassId() != null) {
            int count = studentMapper.countByClassId(student.getClassId());
            classMapper.updateStudentCount(student.getClassId(), count);
        }
        return student;
    }

    @Transactional
    public Student update(Student student) {
        Student existing = studentMapper.selectById(student.getStudentId());
        if (existing == null) {
            throw new BusinessException(404, "学生不存在");
        }

        Student sameNo = studentMapper.selectByStudentNo(student.getStudentNo());
        if (sameNo != null && !sameNo.getStudentId().equals(student.getStudentId())) {
            throw new BusinessException(400, "学号已存在");
        }

        Integer oldClassId = existing.getClassId();
        Integer newClassId = student.getClassId();

        studentMapper.updateById(student);

        if (oldClassId != null && !oldClassId.equals(newClassId)) {
            int oldCount = studentMapper.countByClassId(oldClassId);
            classMapper.updateStudentCount(oldClassId, oldCount);
        }
        if (newClassId != null) {
            int newCount = studentMapper.countByClassId(newClassId);
            classMapper.updateStudentCount(newClassId, newCount);
        }

        return studentMapper.selectById(student.getStudentId());
    }

    @Transactional
    public void delete(Integer studentId) {
        Student existing = studentMapper.selectById(studentId);
        if (existing == null) {
            throw new BusinessException(404, "学生不存在");
        }

        Integer classId = existing.getClassId();
        studentMapper.deleteById(studentId);

        if (classId != null) {
            int count = studentMapper.countByClassId(classId);
            classMapper.updateStudentCount(classId, count);
        }
    }

    @Transactional
    public void batchAdd(List<Student> students) {
        for (Student student : students) {
            add(student);
        }
    }
}
