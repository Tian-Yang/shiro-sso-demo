package com.shiro.demo.jwt;

import lombok.Data;

/**
  *JwtToken
  *@Desc JwkToken
  *Author TianYang
  *Date 2023/11/15 13:31
  */
@Data
public class JwtToken {
    /**
     * 头
     */
    private JwtTokenHeader header;
    /**
     * 载荷
     */
    private JwtTokenPayload payload;

}
