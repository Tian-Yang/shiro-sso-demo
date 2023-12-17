package com.shiro.demo.vo.role;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 菜单权限
 */
@Data
public class RoleAddVO {

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]{1,30}$", message = "角色名称只能输入中文、英文字母、英文下划线,数字，且最大长度30字符")
    private String roleName;

    /**
     * 角色编码
     */
    @Pattern(regexp = "^[a-zA-Z/-_]*$", message = "角色编码只能输入英文字母、英文/和英文-、英文_")
    @Size(max = 30, message = "角色编码长度不能超过30个字符")
    private String roleCode;

    /**
     * 角色状态:0禁用,1启用
     */
    @NotBlank(message = "色状态不能为空")
    @Pattern(regexp = "^(0|1)$", message = "未识别的角色状态")
    private String roleStatus;

    /**
     * 菜单权限，对应勾选的目录、菜单、按钮的menuId集合
     */
    private List<String> menuIds;

}
