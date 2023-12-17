package com.shiro.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.entity.MenuInfoEntity;
import com.shiro.demo.vo.menu.MenuAddVO;
import com.shiro.demo.vo.menu.MenuQueryVO;
import com.shiro.demo.vo.menu.MenuVO;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * @author nmtz
 * @description 针对表【menu_info】的数据库操作Service
 * @createDate 2023-12-12 19:30:15
 */
public interface MenuInfoService extends IService<MenuInfoEntity> {
    /**
     * 新增菜单
     *
     * @param menuAddVO
     */
    void addMenu(MenuAddVO menuAddVO);

    /**
     * 查询可用菜单
     *
     * @param menuQueryVO
     * @return
     */
    List<MenuVO> queryAccessibleMenus(MenuQueryVO menuQueryVO);


    /**
     * 查询所有菜单
     * Author TianYang
     * Date 2023/12/14 10:19
     */
    List<MenuVO> queryAllMenus(MenuQueryVO menuQueryVO);

    List<MenuInfoEntity> queryAllMenus();


    List<MenuVO> queryAllMenusByRoleId(Long roleId);

    /**
     * 初始化租户菜单
     * @param tenantId
     */
    void initTenantMenu(Long tenantId);

}
