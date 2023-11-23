package com.shiro.demo.jwt;

import lombok.Data;

@Data
public class JwtTokenHeader {
    /**
     * 签名算法
     */
    private String alg;
    /**
     * 令牌类型
     */
    private String typ;
}
