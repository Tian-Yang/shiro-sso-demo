package com.shiro.demo.jwt;

import com.shiro.demo.bean.MemberInfo;
import lombok.Data;

/**
 * JwtTokenPayload
 *
 * @Desc JWT Token Payload
 * Author TianYang
 * Date 2023/11/15 13:57
 */
@Data
public class JwtTokenPayload {

    /**
     * 用户唯一标识
     */
    private String sub;

    /**
     * 到期时间
     */
    private String exp;

    /**
     * JWTId,Jwt Token的唯一标识，使用UUID生成，防止每次生成的JwtToken都一样。
     */
    private String jti;

    /**
     * 会员信息
     */
    private MemberInfo memberInfo;

}
