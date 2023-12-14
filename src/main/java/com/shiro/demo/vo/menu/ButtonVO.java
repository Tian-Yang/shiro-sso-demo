package com.shiro.demo.vo.menu;

import lombok.Data;

import java.io.Serializable;

/**
 * 按钮
 */
@Data
public class ButtonVO implements Serializable {

    private static final long serialVersionUID = 9070664454064618918L;
    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 权限
     */
    private String permission;

}
