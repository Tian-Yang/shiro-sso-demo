package com.shiro.demo.filter;

import com.shiro.demo.token.UsernamePasswordBCryptToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

/**
 * 用户名密码认证过滤器
 * Author TianYang
 * Date 2023/11/30 14:42
 */
public class BasicUsernamePasswordLoginFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected AuthenticationToken createToken(String username, String password,
                                              boolean rememberMe, String host) {
        return new UsernamePasswordBCryptToken(username, password, rememberMe, host);
    }
}
