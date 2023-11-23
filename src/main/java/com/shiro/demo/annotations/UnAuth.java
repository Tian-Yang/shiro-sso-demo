package com.shiro.demo.annotations;

import java.lang.annotation.*;

/**
 * 自定义免认证注解
 * Author TianYang
 * Date 2023/11/21 17:46
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UnAuth {
}
