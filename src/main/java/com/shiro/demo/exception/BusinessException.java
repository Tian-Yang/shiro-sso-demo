package com.shiro.demo.exception;

import com.shiro.demo.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.Assert;

/**
 * 自定义业务异常类
 * Author TianYang
 * Date 2023/12/12 11:13
 */
@Slf4j
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 6191815347338869549L;
    private Integer code;

    public BusinessException(ErrorCodeEnum errorCode, String desc, Throwable cause) {
        super(errorCode.getMessage(), cause);
        Assert.notNull(errorCode, "errorCode cant be null");
        this.code = errorCode.getCode();
        log.error("BusinessException message:{}", desc);
    }


    public BusinessException(ErrorCodeEnum errorCode, String desc) {
        super(errorCode.getMessage());
        Assert.notNull(errorCode, "errorCode cant be null");
        this.code = errorCode.getCode();
        log.error("BusinessException errorCode:{}, message:{}", code, desc);
        this.code = errorCode.getCode();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }
}
