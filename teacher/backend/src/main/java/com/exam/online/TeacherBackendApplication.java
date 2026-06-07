package com.exam.online;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.exam.online.mapper")
public class TeacherBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherBackendApplication.class, args);
    }
}