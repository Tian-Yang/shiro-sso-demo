package com.shiro.demo.jwt;

import lombok.Data;

/**
 * JWT Token Payload 抽象类
 * Author TianYang
 * Date 2023/11/24 15:40
 */
@Data
public class AbstractJwtTokenPayload {
    /**
     * 用户唯一标识
     */
    private String sub;

    /**
     * 到期时间
     */
    private Object exp;

    /**
     * JWTId,Jwt Token的唯一标识，使用UUID生成，防止每次生成的JwtToken都一样。
     */
    private String jti;
}
