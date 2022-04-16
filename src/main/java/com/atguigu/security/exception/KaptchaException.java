package com.atguigu.security.exception;


import org.springframework.security.core.AuthenticationException;



//自定义异常
public class KaptchaException extends AuthenticationException {
    public KaptchaException(String message) {
        super(message);
    }
}
