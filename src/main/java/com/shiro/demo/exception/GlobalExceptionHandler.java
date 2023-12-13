package com.shiro.demo.exception;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.config.DeserializerException;
import com.shiro.demo.constants.RespCode;
import com.shiro.demo.enums.ErrorCodeEnum;
import org.apache.shiro.authz.UnauthorizedException;
import org.checkerframework.checker.units.qual.C;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.ServletException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeserializerException.class)
    @ResponseBody
    public CommonResp handeDeserializerException(DeserializerException deserializerException) {
        return CommonResp.fail("9999", deserializerException.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public CommonResp businessException(BusinessException businessException) {
        Integer code = businessException.getCode();
        String errorCode = null == code ? "9999" : String.valueOf(code);
        return CommonResp.fail(errorCode, businessException.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResp handlerRuntimeException(Exception e) {
        return CommonResp.fail(RespCode.CODE_500, e.getMessage());
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public CommonResp handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<String> msg = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        String detail = StrUtil.EMPTY;
        if (CollectionUtil.isNotEmpty(msg)) {
            detail = CollectionUtil.join(msg, StrUtil.COMMA);
        }
        return CommonResp.fail(String.valueOf(ErrorCodeEnum.CODE_9001.getCode()), "参数校验失败!" + detail, msg);
    }

    @ExceptionHandler({BindException.class})
    @ResponseBody
    public CommonResp<List> handleBodyValidException(BindException bindException) {
        List<FieldError> fieldErrors = bindException.getBindingResult().getFieldErrors();
        List<String> msg = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        String detail = StrUtil.EMPTY;
        if (CollectionUtil.isNotEmpty(msg)) {
            detail = CollectionUtil.join(msg, StrUtil.COMMA);
        }
        return CommonResp.fail(String.valueOf(ErrorCodeEnum.CODE_9001.getCode()), "参数校验失败!" + detail, msg);
    }

    @ExceptionHandler(ServletException.class)
    @ResponseBody
    public CommonResp handlerServletException(ServletException e) {
        return CommonResp.fail(RespCode.CODE_500, e.getMessage());
    }


    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public CommonResp handlerUnauthorizedException(UnauthorizedException unauthorizedException) {
        return CommonResp.fail(RespCode.CODE_403, unauthorizedException.getMessage());
    }
}
