package com.shiro.demo.cryptography;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptCredentialFactory implements CredentialFactory{
    private  final String salt = BCrypt.gensalt(12);

    @Override
    public String createCredential(String s) {
        //加盐
        String credential = BCrypt.hashpw(s, salt);
        return credential;
    }


}
