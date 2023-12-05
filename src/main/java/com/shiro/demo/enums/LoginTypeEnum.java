package com.shiro.demo.enums;

import lombok.Data;

public enum LoginTypeEnum {

    USER_NAME_PWD(0);
    private int code;
     LoginTypeEnum(int code){
        this.code = code;
    }


    public int getCode() {
        return code;
    }
}
