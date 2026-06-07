package com.exam.online.mapper;

import com.exam.online.entity.Clazz;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClazzMapper {

    Clazz selectById(@Param("classId") Integer classId);

    List<Clazz> selectAll();

    List<Clazz> selectByDepartmentId(@Param("departmentId") Integer departmentId);

    int insert(Clazz clazz);

    int updateById(Clazz clazz);

    int deleteById(@Param("classId") Integer classId);

    int countAll();
}
