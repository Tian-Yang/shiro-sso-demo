package com.shiro.demo.token;

import com.shiro.demo.cryptography.BcryptCredentialFactory;
import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordBCryptToken extends UsernamePasswordToken {


    public UsernamePasswordBCryptToken(final String username, final String password, final boolean rememberMe, final String host) {
        super(username, password, rememberMe, host);
    }


    public Object getCredentials() {
        return new String(getPassword());
    }
}
