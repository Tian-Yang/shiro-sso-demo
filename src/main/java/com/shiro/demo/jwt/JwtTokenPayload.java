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
public class JwtTokenPayload extends AbstractJwtTokenPayload{

    /**
     * 会员信息
     */
    private MemberInfo memberInfo;

}
