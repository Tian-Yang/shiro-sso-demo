package com.shiro.demo.bean;

import com.shiro.demo.constants.RespCode;
import com.shiro.demo.enums.ErrorCodeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommonResp<T> implements Serializable {

    private static final long serialVersionUID = -8587276359818398428L;


    /**
     * 无权限
     */
    public static final String CODE_401 = "401";

    private T data;

    private int code;

    private String errorMsg;

    private String detailedException;

    public CommonResp(int code) {
        this.code = code;
    }

    public CommonResp(int code, T body) {
        this.code = code;
        this.data = body;
    }


    public CommonResp(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public CommonResp(ErrorCodeEnum code, String errorMsg, String detailedException) {
        this.code = code.getCode();
        this.errorMsg = errorMsg;
        this.detailedException = detailedException;
    }

    public CommonResp(int code, String errorMsg, T body) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.data = body;
    }

    public static CommonResp success() {
        return new CommonResp(RespCode.CODE_0000);
    }

    public static CommonResp success(Object body) {
        return new CommonResp(RespCode.CODE_0000, body);
    }

    public static CommonResp fail(int code, String errorMSg) {
        return new CommonResp(code, errorMSg);
    }

    public static CommonResp fail(int code,String errorMsg,Object body){
        return new CommonResp(code,errorMsg,body);
    }

    public static CommonResp fail(ErrorCodeEnum code, String errorMSg, String detailedException) {
        return new CommonResp(code, errorMSg, detailedException);
    }
}
