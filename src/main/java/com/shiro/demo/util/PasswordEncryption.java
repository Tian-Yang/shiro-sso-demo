package com.shiro.demo.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryption {

    public static String bCryptHashpw(String password) {
        String salt = BCrypt.gensalt(10);
        return BCrypt.hashpw(password, salt);
    }

}
