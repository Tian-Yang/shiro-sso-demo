package com.shiro.demo.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class MemberAddVO implements Serializable {
    private static final long serialVersionUID = -584440438658399304L;
    @NotBlank(message = "账户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,}$", message = "Account name must be at least 3 characters long and can only contain letters, numbers, and underscores")
    private String accountName;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, and one digit")
    private String password;
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "请输入正确的手机号码格式")
    private String phone;
    @Pattern(regexp = "^[a-zA-Z\\u4e00-\\u9fa5]+([·•][a-zA-Z\\u4e00-\\u9fa5]+)*$", message = "Invalid name format")
    private String realName;
    private String tenantId;

}
