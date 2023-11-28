package com.shiro.demo.dto;

import lombok.Data;

/**
 * 认证请求
 * Author TianYang
 * Date 2023/11/24 10:39
 */
@Data
public class AuthReqDto {
    private String clientId;
    private String clientSecret;
    private String csrfSerialNo;
}
