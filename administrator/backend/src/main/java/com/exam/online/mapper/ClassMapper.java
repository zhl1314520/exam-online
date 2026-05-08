package com.exam.online.mapper;

import com.exam.online.entity.Class;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClassMapper {

    Class selectById(@Param("classId") Integer classId);

    List<Class> selectAll();

    List<Class> selectByDepartmentId(@Param("departmentId") Integer departmentId);

    int insert(Class clazz);

    int updateById(Class clazz);

    int updateStudentCount(@Param("classId") Integer classId, @Param("count") Integer count);

    int deleteById(@Param("classId") Integer classId);
}