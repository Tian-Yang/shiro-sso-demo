package com.shiro.demo.token;

import lombok.Data;
import org.apache.shiro.authc.UsernamePasswordToken;

@Data
public class BasicToken extends UsernamePasswordToken {
    private String token;
    private String loginType;


    public BasicToken(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
