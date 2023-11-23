package com.shiro.demo.authenticat;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.mindrot.jbcrypt.BCrypt;

/**
 * 密码BCrypt Hash匹配
 */
public class PasswordBCryptCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        return BCrypt.checkpw((String) token.getCredentials(), (String) info.getCredentials());
    }
}
