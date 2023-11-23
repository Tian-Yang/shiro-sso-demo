package com.shiro.demo.filter;

import com.shiro.demo.token.UsernamePasswordBCryptToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

public class BasicUsernamePasswordLoginFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected AuthenticationToken createToken(String username, String password,
                                              boolean rememberMe, String host) {
        return new UsernamePasswordBCryptToken(username, password, rememberMe, host);
    }
}
