package com.shiro.demo.constans;

public interface JwtTokenConstant {
    /**
     * token生效时间
     */
    String TOKEN_EXPIRED_TIME = "tokenExpiredTime";

    /**
     * Token有效天数
     */
    int TOKEN_EXPIRED_DAYS = 60;

    /**
     * principal唯一标识(认证用户唯一标识，例如用户名)
     */
    String PRIMARY_PRINCIPAL_KEY = "primary_principal_key";

    /**
     * 认证失败错误信息
     */
    String AUTH_ERROR_MESSAGE = "auth_error_message";

    /**
     * JWT Token
     */
    String JWT_TOKEN = "token";
}
