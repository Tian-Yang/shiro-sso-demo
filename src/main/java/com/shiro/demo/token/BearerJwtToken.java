package com.shiro.demo.token;

import com.shiro.demo.bean.MemberInfo;
import com.shiro.demo.entity.MemberInfoEntity;
import com.shiro.demo.jwt.JwtTokenPayload;
import com.shiro.demo.util.JwtUtil;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * 自定义Token
 *
 * @Desc 用于处理Http Bearer协议携带的Jwt Token
 * Author TianYang
 * Date 2023/11/14 16:16
 */
public class BearerJwtToken implements HostAuthenticationToken, RememberMeAuthenticationToken {
    private String host;

    private String clientHost;
    private String token;

    public BearerJwtToken(final String token, final String host, final String clientHost) {
        this.host = host;
        this.token = token;
        this.clientHost = clientHost;
    }


    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public boolean isRememberMe() {
        return false;
    }

    @Override
    public String getPrincipal() {
        //解析Token，从中获取用户唯一标识
        JwtTokenPayload jwtTokenPayload = JwtUtil.decodeJwtToken(token, JwtTokenPayload.class);
        MemberInfo memberInfo = jwtTokenPayload.getMemberInfo();
        Long memberId = memberInfo.getMemberId();
        Long tenantId = memberInfo.getTenantId();
        String bussinessDomainCode = memberInfo.getBusinessDomainCode();
        String key = bussinessDomainCode + "_" + memberId;
        if (null != tenantId) {
            key = key + "_" + token;
        }
        return key;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
