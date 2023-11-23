package com.shiro.demo.config;

import java.io.IOException;

/**
  *ValidateException
  *@Desc 校验异常
  *Author TianYang
  *Date 2023/9/14 13:40
  */
public class DeserializerException extends IOException {

    public DeserializerException(String message) {
        super(message);
    }
}
