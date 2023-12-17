package com.shiro.demo.vo.menu;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
public class MenuAddVO implements Serializable {

    private static final long serialVersionUID = 3174488572797888332L;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]{1,30}$", message = "菜单名称只能输入中文、英文字母、英文下划线,数字，且最大长度30字符")
    private String menuName;

    /**
     * 菜单类型
     *
     * @see com.shiro.demo.enums.MenuTypeEnum
     */

    @NotNull(message = "菜单类型不能为空")
    @Pattern(regexp = "^(catalog|menu|button)$", message = "未识别的菜单类型")
    private String menuType;


    /**
     * 菜单位置:left(默认) 左侧菜单, top 顶部菜单，right 右侧,button 底部
     */
    @Pattern(regexp = "^(left|top|right|button)$", message = "未识别的菜单类型")
    private String menuPosition;

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
    @Positive(message = "父节点ID类型错误，只能为正整数")
    private Long parentMenuId;


    /**
     * 图标;图标名称
     */
    @NotBlank
    private String icon;


    /**
     * 排序
     */
    @Positive(message = "排序只能输入正整数")
    private Integer sort;


    /**
     * 权限编码(按钮类型专用)
     */
    @Pattern(regexp = "^[a-zA-Z/-_]*$", message = "权限编码只能输入英文字母、英文/和英文-、英文_")
    @Size(max = 30, message = "权限编码长度不能超过30个字符")
    private String permission;
}
