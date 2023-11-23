package com.shiro.demo.strategy;

import com.shiro.demo.principal.JwtPrincipalMap;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 自定义Alarm策略
 * Author TianYang
 * Date 2023/11/17 17:59
 */
public class CustomizeFirstSuccessfulStrategy extends FirstSuccessfulStrategy {


    /**
     * 重写afterAttempt策略，将认证错误信息封装到AuthenticationInfo对象中。
     *
     * @param realm
     * @param token
     * @param singleRealmInfo
     * @param aggregateInfo
     * @param t
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @Author TianYang
     * @Date 2023/11/17 17:59
     */
    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        AuthenticationInfo info = super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
        if (null != t) {
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo();
            JwtPrincipalMap jwtPrincipalMap = new JwtPrincipalMap();
            jwtPrincipalMap.setErrorMsg(t.getMessage());
            simpleAuthenticationInfo.setPrincipals(jwtPrincipalMap);
            return simpleAuthenticationInfo;
        }
        return info;
    }

    /**
     * 重写 afterAllAttempts策略，处理认证失败错误信息
     *
     * @param token
     * @param aggregate
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @Author TianYang
     * @Date 2023/11/17 18:00
     */
    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {

        if (null != aggregate && aggregate instanceof SimpleAuthenticationInfo) {
            SimpleAuthenticationInfo simpleAuthenticationInfo = (SimpleAuthenticationInfo) aggregate;
            PrincipalCollection principalCollection = simpleAuthenticationInfo.getPrincipals();
            if (null != principalCollection && principalCollection instanceof JwtPrincipalMap) {
                JwtPrincipalMap jwtPrincipalMap = (JwtPrincipalMap) principalCollection;
                String authErrorMsg = jwtPrincipalMap.getErrorMsg();
                if (null != authErrorMsg && !"".equals(authErrorMsg)) {
                    throw new AuthenticationException(authErrorMsg);
                }
            }
        }
        return aggregate;
    }
}
