package com.exam.online.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private Integer code;
    private String message;
    private T data;

    // 4 个静态方法，分别对应不同的返回结果
    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(200, "success", data); // 默认成功格式
    }

    // 自定义成功格式
    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(200, message, data);
    }

    // 自定义错误格式
    public static <T> ResponseDTO<T> error(Integer code, String message) {
        return new ResponseDTO<>(code, message, null);
    }

    // 默认错误格式
    public static <T> ResponseDTO<T> error(String message) {
        return new ResponseDTO<>(500, message, null);
    }
}