package com.shiro.demo.relam;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 手机验证码登录
 * Author TianYang
 * Date 2023/11/30 13:46
 */
public class SmsVerificationCodeLoginRealm extends CustomizeAbstractRealm {
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return null;
    }
}
