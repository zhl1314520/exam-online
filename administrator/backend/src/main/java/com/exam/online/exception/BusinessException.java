package com.exam.online.exception;

// 让 Service 层可以 像抛异常一样表达业务错误
public class BusinessException extends RuntimeException {
    private Integer code;   // 业务错误码（非 HTTP 状态码）

    // 默认 500
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    // 自定义错误码
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}