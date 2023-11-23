package com.shiro.demo.listener;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;

public class AuthListener implements AuthenticationListener {

    @Override
    public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {

    }

    @Override
    public void onFailure(AuthenticationToken token, AuthenticationException ae) {

    }

    @Override
    public void onLogout(PrincipalCollection principals) {

    }
}
