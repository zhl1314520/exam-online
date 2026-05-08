package com.exam.online.mapper;

import com.exam.online.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    Department selectById(@Param("departmentId") Integer departmentId);

    List<Department> selectAll();

    int insert(Department department);

    int updateById(Department department);

    int deleteById(@Param("departmentId") Integer departmentId);
}