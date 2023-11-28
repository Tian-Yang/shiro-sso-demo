package com.shiro.demo.relam;

import com.shiro.demo.constants.JwtTokenConstant;
import com.shiro.demo.principal.JwtPrincipalMap;
import com.shiro.demo.token.BearerJwtToken;
import com.shiro.demo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Collection;

/**
 * Http Bearer协议请求处理Realm
 *
 * @Desc 用于处理通过Http Bearer协议携带Jwt Token的Http请求。
 * Author TianYang
 * Date 2023/11/14 16:18
 */
@Slf4j
public class BearerJwtTokenRealm extends CustomizeAbstractRealm {

    public BearerJwtTokenRealm() {
        //限定支持的Token类型
        this.setAuthenticationTokenClass(BearerJwtToken.class);
    }


    /**
     * JwtToken的 Realm 不应该创建认证信息。
     *
     * @param token
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @Author TianYang
     * @Date 2023/11/15 16:03
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        throw new IncorrectCredentialsException("token is unrecognized");
    }


    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {

        Cache<Object, AuthenticationInfo> cache = this.getAuthenticationCache();
        String jwtToken = (String) token.getCredentials();

        if (null != info) {
            SimpleAuthenticationInfo simpleAuthenticationInfo = (SimpleAuthenticationInfo) info;
            JwtPrincipalMap simplePrincipalMap = (JwtPrincipalMap) simpleAuthenticationInfo.getPrincipals();
            String cachedJwtToken = simplePrincipalMap.getJwtToken();
            if (!jwtToken.equals(cachedJwtToken)) {
                throw new IncorrectCredentialsException("the token unrecognized");
            }
            long oldExpiredTime = simplePrincipalMap.getTokenExpiredTime();
            //判断token有效期是否失效
            if (JwtUtil.isExpired(oldExpiredTime)) {
                //删除认证缓存
                cache.remove(token.getPrincipal());
                throw new ExpiredCredentialsException("the token is expired");
            }
            //未失效则延长有效期
            else {
                //延长JWT Token失效时间
                simplePrincipalMap.put(JwtTokenConstant.TOKEN_EXPIRED_TIME, JwtUtil.getExpirationTimeMillis(60));
                simpleAuthenticationInfo.setPrincipals(simplePrincipalMap);
                cache.put(token.getPrincipal(), simpleAuthenticationInfo);
            }

        } else {
            throw new ExpiredCredentialsException("the token is expired");
        }


    }


    @Override
    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        if (null != principals && !principals.isEmpty()) {
            Cache<Object, AuthenticationInfo> cache = this.getAuthenticationCache();
            //cache instance will be non-null if caching is enabled:
            if (cache != null) {
                if (principals instanceof JwtPrincipalMap) {
                    JwtPrincipalMap jwtPrincipalMap = (JwtPrincipalMap) principals;
                    Object key = jwtPrincipalMap.getPrimaryPrincipal();
                    cache.remove(key);
                } else {
                    throw new AuthenticationException("principals is mismatch");
                }


            }
        }
    }


    /**
     * 重写AuthorizingRealm clearCachedAuthorizationInfo方法，自定义清除授权缓存
     *
     * @param principals
     * @return java.lang.Object
     * @Author TianYang
     * @Date 2023/11/17 13:59
     */
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


    /**
     * 重写授权缓存key获取方法，实现自电影一缓存key
     *
     * @param principals
     * @return java.lang.Object
     * @Author TianYang
     * @Date 2023/11/21 09:57
     */
    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        JwtPrincipalMap jwtPrincipalMap = (JwtPrincipalMap) principals;
        String primaryPrincipal = (String) jwtPrincipalMap.getPrimaryPrincipal();
        String key = primaryPrincipal + "_authorization";
        return key;
    }

    /**
     * 重写AuthorizingRealm的getAuthorizationInfo方法，实现自定义授权信息获取
     *
     * @param principals
     * @return org.apache.shiro.authz.AuthorizationInfo
     * @Author TianYang
     * @Date 2023/11/21 10:52
     */
    @Override
    protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {

        if (principals == null) {
            return null;
        }

        AuthorizationInfo info = null;

        if (log.isTraceEnabled()) {
            log.trace("Retrieving AuthorizationInfo for principals [" + principals + "]");
        }

        Cache<Object, AuthorizationInfo> cache = super.getAuthorizationCache();
        if (cache != null) {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to retrieve the AuthorizationInfo from cache.");
            }
            Object key = getAuthorizationCacheKey(principals);
            info = cache.get(key);
            if (log.isTraceEnabled()) {
                if (info == null) {
                    log.trace("No AuthorizationInfo found in cache for principals [" + principals + "]");
                } else {
                    log.trace("AuthorizationInfo found in cache for principals [" + principals + "]");
                }
            }
        }


        if (info == null) {
            // Call template method if the info was not found in a cache
            info = doGetAuthorizationInfo(principals);
            // If the info is not null and the cache has been created, then cache the authorization info.
            if (info != null && cache != null) {
                if (log.isTraceEnabled()) {
                    log.trace("Caching authorization info for principals: [" + principals + "].");
                }
                Object key = getAuthorizationCacheKey(principals);
                cache.put(key, info);
            }
        }

        return info;
    }

    @Override
    public void checkPermissions(PrincipalCollection principal, Collection<Permission> permissions) throws AuthorizationException {
        AuthorizationInfo info = getAuthorizationInfo(principal);
        checkPermissions(permissions, info);
    }


}
