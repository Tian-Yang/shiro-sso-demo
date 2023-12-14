package com.shiro.demo.vo.menu;

import lombok.Data;

import java.io.Serializable;

@Data
public class MenuVO implements Serializable {
    private static final long serialVersionUID = -171151559082606758L;
    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 父菜单ID
     */
    private Long parentMenuId;

    /**
     * 菜单名称
     */
    private String menuName;


    /**
     * 层级
     */
    private Integer level;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 菜单类型:catalog|menu|button
     */
    private String menuType;

    /**
     * 路径
     */
    private String path;

    /**
     * 图标
     */
    private String icon;


}
