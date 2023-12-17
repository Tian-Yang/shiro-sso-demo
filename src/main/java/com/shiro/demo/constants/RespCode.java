package com.shiro.demo.constants;

public interface RespCode {

    /**
     * 成功
     */
    Integer CODE_0000 = 0;

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

    /**
     * 1000:业务域未识别
     */
    String CODE_1000 = "1000";

    /**
     * 1001：用户不存在
     */
    String CODE_1001 = "1001";

    /**
     * 1002: 用户被禁用
     */
    String CODE_1002 = "1002";

    /**
     *
     */
    String CODE_1003 = "1003";

}
