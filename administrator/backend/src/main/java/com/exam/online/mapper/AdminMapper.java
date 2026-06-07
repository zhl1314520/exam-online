package com.exam.online.mapper;

import com.exam.online.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 定义 admin 方法签名，供 Service 调用
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