package com.shiro.demo.token;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/*
 *手机号码一健登录
 *Author TianYang
 *Date 2023/11/15 15:04
 */
public class SmsOneClickLoginToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

    private String phoneNumber;

    private String code;

    private boolean rememberMe = false;

    private String host;


    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

    @Override
    public Object getPrincipal() {
        return phoneNumber;
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }
}
