package com.exam.online.mapper;

import com.exam.online.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    Admin selectById(@Param("adminId") Integer adminId);

    Admin selectByUsername(@Param("username") String username);

    List<Admin> selectAll();

    int insert(Admin admin);

    int updateById(Admin admin);

    int updateLastLoginTime(@Param("adminId") Integer adminId);

    int deleteById(@Param("adminId") Integer adminId);
}