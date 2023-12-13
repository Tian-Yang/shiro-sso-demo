package com.shiro.demo.enums;

/***
 * 菜单类型枚举
 *Author TianYang
 *Date 2023/12/12 18:14
 */
public enum MenuTypeEnum {
    /**
     * 目录
     */
    CATALOG("catalog"),
    /**
     * 菜单
     */
    MENU("menu"),
    /**
     * 按钮
     */
    BUTTON("button");
    private String code;

    MenuTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
