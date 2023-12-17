package com.shiro.demo.vo.role;

import com.shiro.demo.vo.menu.MenuVO;
import lombok.Data;

import java.util.List;

@Data
public class RoleQueryRespVO {

    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色状态
     */
    private Long roleStatus;

    /**
     * 角色菜单权限列表
     */
    private List<MenuVO> menuList;
}
