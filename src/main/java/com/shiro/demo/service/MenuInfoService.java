package com.shiro.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.entity.MenuInfoEntity;
import com.shiro.demo.vo.menu.MenuAddVO;

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
}
