package com.shiro.demo.enums;

/**
 * 菜单位置枚举
 * Author TianYang
 * Date 2023/12/13 11:17
 */
public enum MenuPositionEnum {
    LEFT("left"),
    TOP("top"),
    RIGHT("right"),
    BUTTON("button");

    private String code;

    MenuPositionEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
