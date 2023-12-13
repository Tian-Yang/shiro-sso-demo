package com.shiro.demo.vo.menu;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class MenuAddVO {

    /**
     * 菜单类型
     *
     * @see com.shiro.demo.enums.MenuTypeEnum
     */

    @NotNull(message = "菜单类型不能为空")
    @Pattern(regexp = "^(catalog|menu|button)$", message = "未识别的菜单类型")
    private String menuType;

    /**
     * 路由地址
     * 路由地址最大长度120字节
     * 菜单路由地址只能输入英文字母、英文/和英文-
     */
    @Length(max = 120, message = "路由地址最大长度120字节")
    @Pattern(regexp = "^[a-zA-Z/-]*$", message = "菜单路由地址只能输入英文字母、英文/和英文-")
    private String path;

    /**
     * 父节点ID
     */
    @Pattern(regexp = "^[1-9]\\d*$", message = "父节点ID类型错误，只能为正整数")
    private Long parentId;


    /**
     * 图标;图标名称
     */
    @NotBlank
    private String icon;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,30}$", message = "菜单名称只能输入中文、英文字母、数字，且最大长度30字符")
    private String menuName;


    /**
     * 排序
     */
    @NotBlank(message = "排序不能为空")
    @Pattern(regexp = "^[1-9]\\d*$", message = "排序只能输入正整数")
    private Integer sort;


    /**
     * 权限编码(按钮类型专用)
     */
    @Pattern(regexp = "^[1-9]\\d*$", message = "排序只能输入正整数")
    private String permission;
}
