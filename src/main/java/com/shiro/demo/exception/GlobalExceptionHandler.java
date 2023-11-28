package com.shiro.demo.exception;

import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.config.DeserializerException;
import com.shiro.demo.constants.RespCode;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeserializerException.class)
    @ResponseBody
    public CommonResp handeDeserializerException(DeserializerException deserializerException) {
        return CommonResp.fail("999", deserializerException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResp handlerRuntimeException(Exception e) {
        return CommonResp.fail(RespCode.CODE_500, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public CommonResp handlerUnauthorizedException(UnauthorizedException unauthorizedException) {
        return CommonResp.fail(RespCode.CODE_403, unauthorizedException.getMessage());
    }
}
