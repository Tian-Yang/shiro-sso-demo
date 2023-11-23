package com.shiro.demo.constans;

public interface RespCode {

    /**
     * 成功
     */
    String CODE_0000 = "0000";

    /**
     * 未认证
     */
    String CODE_401 = "401";

    /**
     * 认证成功，但无权限
     */
    String CODE_403 = "403";


    /**
     * 服务端异常
     */
    String CODE_500 = "500";
}
