package com.shiro.demo.vo.role;

import lombok.Data;

import java.util.List;

/**
 * 菜单权限
 */
@Data
public class RoleAddVO {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色状态:0禁用,1启用
     */
    private Integer roleStatus;

    /**
     * 菜单权限，对应勾选的目录、菜单、按钮的menuId集合
     */
    private List<String> menuIds;

}
