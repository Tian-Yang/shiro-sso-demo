package com.shiro.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jdbc")
@Data
public class JdbcConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private String type;
}
