package com.shiro.demo.constants;

public interface RedisKeyConstant {

    /**
     * 认证缓存Key
     */
    String AUTHENTICATION_CACHE_KEY = "authenticate";

    /**
     * 登录图形验证码
     */
    String REDIS_GRAPH_CAPTCHA_KEY = "graph_captcha_";

}
