package com.shiro.demo.relam;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
  *自定义Realm抽象类
  *@Desc 用于处理一些自定义Realm中的公共逻辑，例如角色权限相关信息的处理。
  *Author TianYang
  *Date 2023/11/14 16:23
  */
@Slf4j
public abstract class CustomizeAbstractRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("doGetAuthorizationInfo...");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Set<String> permissions = new HashSet<>();
        permissions.add("account:create");
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }


}
