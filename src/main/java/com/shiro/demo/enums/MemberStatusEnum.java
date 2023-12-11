package com.shiro.demo.enums;

public enum MemberStatusEnum {
    ENABLE(1),
    DISABLE(0);
    private int code;

    MemberStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
