package com.shiro.demo.token;

import lombok.Data;
import org.apache.shiro.authc.UsernamePasswordToken;

@Data
public class BaseLoginToken extends UsernamePasswordToken {
    private String appCode;
    private String appSecret;
}
