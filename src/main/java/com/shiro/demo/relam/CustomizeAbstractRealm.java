package com.shiro.demo.relam;

import com.shiro.demo.bean.MemberInfo;
import com.shiro.demo.constants.JwtTokenConstant;
import com.shiro.demo.principal.JwtPrincipalMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
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

    @Override
    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        if (null != principals && !principals.isEmpty()) {
            Cache<Object, AuthenticationInfo> cache = this.getAuthenticationCache();
            //cache instance will be non-null if caching is enabled:
            if (cache != null) {
                if (principals instanceof JwtPrincipalMap) {
                    JwtPrincipalMap jwtPrincipalMap = (JwtPrincipalMap) principals;

                    MemberInfo memberInfo = jwtPrincipalMap.oneByType(MemberInfo.class);
                    String key = memberInfo.getMemberAuthCacheKey();
                    cache.remove(key);
                } else {
                    throw new AuthenticationException("principals is mismatch");
                }


            }
        }
    }

    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            return;
        }

        Cache<Object, AuthenticationInfo> cache = this.getAuthenticationCache();
        //cache instance will be non-null if caching is enabled:
        if (cache != null) {
            Object key = getAuthorizationCacheKey(principals);
            cache.remove(key);
        }
    }


    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        JwtPrincipalMap jwtPrincipalMap = (JwtPrincipalMap) principals;
        MemberInfo memberInfo= jwtPrincipalMap.oneByType(MemberInfo.class);
        String key = memberInfo.getMemberAuthCacheKey() + "_authorization";
        return key;
    }

}
