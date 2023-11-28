package com.shiro.demo.bean;

import com.shiro.demo.constants.RespCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommonResp<T> implements Serializable {

    private static final long serialVersionUID = -8587276359818398428L;


    /**
     * 无权限
     */
    public static final String CODE_401 = "401";

    private T body;

    private String code;
    private String errorMsg;

    public CommonResp(String code) {
        this.code = code;
    }

    public CommonResp(String code, T body) {
        this.code = code;
        this.body = body;
    }


    public CommonResp(String code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public static CommonResp success() {
        return new CommonResp(RespCode.CODE_0000);
    }

    public static CommonResp success(Object body) {
        return new CommonResp(RespCode.CODE_0000, body);
    }

    public static CommonResp fail(String code, String errorMSg) {
        return new CommonResp(code, errorMSg);
    }
}
