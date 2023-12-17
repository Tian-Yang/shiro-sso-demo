package com.shiro.demo.enums;

public enum ErrorCodeEnum {

    CODE_9999(9999, "系统异常"),
    CODE_9001(9001, "参数校验异常"),
    //认证授权相关异常码(1001-1999)
    CODE_1001(1001, "登录验证码长度应为 4 到 8 之间!"),
    CODE_1002(1002, "登录验证码不能为空"),
    CODE_1003(1003, "登录验证码错误或已失效"),
    CODE_1004(1004, "验证码错误"),
    CODE_1005(1005,"账号非当前业务域管理员账号，禁止登录"),
    CODE_1006(1006,"用户名不存在"),
    CODE_2001(2001, "父菜单不存在"),
    CODE_2002(2002, "排序已存在"),
    //菜单维护相关异常码(2002-2999)
    CODE_2003(2003, "菜单路径已存在"),
    CODE_2004(2004, "只有菜单类型才允许设置权限编码"),
    CODE_2005(2005, "按钮权限编码不能为空"),
    CODE_2006(2006, "权限编码已存在"),
    CODE_2007(2007, "菜单和目录的父节点只能为目录"),

    CODE_2008(2008, "按钮的父节点只能为菜单"),
    CODE_2009(2009, "路径不能为空"),
    CODE_2010(2010, "菜单名称已存在"),
    CODE_2011(2011, "菜单或目录的路径与按钮权限编码冲突"),
    //角色维护相关异常码(3001-3999)
    CODE_3001(3001, "以下菜单不存在"),
    //用户相关异常码
    CODE_4001(4001,"账户名已存在"),
    CODE_4002(4002,"账号关联的角色不存在"),
    //租户相关异常
    CODE_5001(5001,"租户名称已存在");

    private int code;
    private String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
