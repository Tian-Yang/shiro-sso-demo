package com.shiro.demo.jwt;


import lombok.Data;

/**
 * CSRF防护Token Payload
 * Author TianYang
 * Date 2023/11/24 15:40
 */
@Data
public class CsrfJwtTokenPayload extends AbstractJwtTokenPayload {

    /**
     * CSRF防护请求序列号(UUID)
     */
    private String csrfSerialNo;

}
