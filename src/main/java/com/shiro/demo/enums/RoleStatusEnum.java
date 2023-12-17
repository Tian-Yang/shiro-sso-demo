package com.shiro.demo.enums;

/**
 * 角色状态枚举
 * Author TianYang
 * Date  13:56
 */
public enum RoleStatusEnum {
    NORMAL(1),
    DESGAITU(0);
    private Integer code;

    RoleStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
