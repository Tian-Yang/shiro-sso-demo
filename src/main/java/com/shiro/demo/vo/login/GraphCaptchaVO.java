package com.shiro.demo.vo.login;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 图形验证码
 */
@Getter
@Setter
@Builder
public class GraphCaptchaVO {
    private String captcha;
    private String uid;
}
