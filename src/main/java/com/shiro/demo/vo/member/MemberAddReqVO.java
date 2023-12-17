package com.shiro.demo.vo.member;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class MemberAddReqVO  implements Serializable {


    private static final long serialVersionUID = -6533368630398460905L;
    /**
     * 请求流水号
     */
    private String requestSerialNumber;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5·]{1,30}$", message = "请输入正确的中文名，最大长度30字符")
    private String realName;

    /**
     * 账户名称
     */
    @NotBlank(message = "账户名称不能为空")
    @Pattern(regexp = "^[a-zA-Z@_0-9]{1,30}$", message = "账户名只能输入英文字母、@、_和数字，最大长度30字符")
    private String accountName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z@_0-9]{1,30}$", message = "密码只能输入英文字母、@、_和数字，最大长度30字符")
    private String password;

    /**
     * 角色ID
     */
    @Pattern(regexp = "^[1-9]\\d*$", message = "权限ID只能输入正整数")
    private String roleId;

    /**
     * 用户状态，
     * 0禁用
     * 1启用
     */
    @NotBlank(message = "用户状态不能为空")
    @Pattern(regexp = "^(0|1)$", message = "未识别的用户状态")
    private String status;

}
