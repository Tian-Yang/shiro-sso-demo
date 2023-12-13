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

    private String detailedException;

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

    public CommonResp(String code, String errorMsg, String detailedException) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.detailedException = detailedException;
    }

    public CommonResp(String code, String errorMsg, T body) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.body = body;
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

    public static CommonResp fail(String code,String errorMsg,Object body){
        return new CommonResp(code,errorMsg,body);
    }

    public static CommonResp fail(String code, String errorMSg, String detailedException) {
        return new CommonResp(code, errorMSg, detailedException);
    }
}
