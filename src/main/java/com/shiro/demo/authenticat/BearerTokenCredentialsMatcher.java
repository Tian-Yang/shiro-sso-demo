package com.shiro.demo.authenticat;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * 自定义Http Bearer协议请求认证凭据匹配
 */
@Slf4j
public class BearerTokenCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //Step 1:校验token的getCredentials()得到的Jwt Token和AuthenticationInfo中PrincipalCollection中的Jwt Token是否匹配
        String jwtToken = (String) token.getCredentials();
        String cacheJwtToken = (String) info.getCredentials();
        if(log.isDebugEnabled()){
            log.debug("BearerTokenCredentialsMatcher jwtToken:{},cacheJwtToken:{}",jwtToken,cacheJwtToken);
        }
        return jwtToken.equals(cacheJwtToken);
    }
}
