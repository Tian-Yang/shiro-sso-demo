package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.entity.BusinessDomainHostEntity;
import com.shiro.demo.entity.MenuInfoEntity;
import com.shiro.demo.enums.ErrorCodeEnum;
import com.shiro.demo.enums.MenuTypeEnum;
import com.shiro.demo.exception.BusinessException;
import com.shiro.demo.mapper.MenuInfoMapper;
import com.shiro.demo.service.MenuInfoService;
import com.shiro.demo.vo.menu.MenuAddVO;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author nmtz
 * @description 针对表【menu_info】的数据库操作Service实现
 * @createDate 2023-12-12 19:30:15
 */
@Service
public class MenuInfoServiceImpl extends ServiceImpl<MenuInfoMapper, MenuInfoEntity>
        implements MenuInfoService {

    @Override
    public synchronized void addMenu(MenuAddVO menuAddVO) {
        //菜单类型
        String menuType = menuAddVO.getMenuType();
        //菜单排序
        Integer sort = menuAddVO.getSort();
        //父节点ID
        Long parentId = menuAddVO.getParentId();
        //菜单路径
        String path = menuAddVO.getPath();
        //按钮权限
        String permission = menuAddVO.getPermission();
        Integer level;
        menuAddVO.getMenuName();
        //设置父节点ID
        if (null == parentId) {
            level = 0;
        } else {
            MenuInfoEntity parentMenu = this.getById(parentId);
            //校验父节点
            checkParentMenu(parentMenu, menuType);
            //父节点层级
            Integer parentLevel = parentMenu.getLevel();
            level = parentLevel + 1;
        }

        //校验排序是否已存在
        checkSortIsExists(menuType, level, sort, null);
        //校验路径
        checkPath(menuType, path, null);
        //校验权限编码
        checkPermission(menuType, permission, null);

        MenuInfoEntity menuInfo = new MenuInfoEntity();
        BeanUtils.copyProperties(menuAddVO, menuInfo);
        menuInfo.setLevel(level);
        this.save(menuInfo);
    }

    private void checkParentMenu(MenuInfoEntity parentMenu, String menuType) {
        if (null == parentMenu) {
            throw new BusinessException(ErrorCodeEnum.CODE_2001, "父菜单不存在");
        }
        String parentMenuType = parentMenu.getMenuType();
        //菜单和目录的父节点只能为目录
        if (menuType.equals(MenuTypeEnum.MENU.getCode()) || menuType.equals(MenuTypeEnum.CATALOG.getCode())) {
            if (!parentMenuType.equals(MenuTypeEnum.CATALOG)) {
                throw new BusinessException(ErrorCodeEnum.CODE_2007, "菜单和目录的父节点只能为目录");
            }
        }
        //按钮的父节点只能为菜单
        else if (menuType.equals(MenuTypeEnum.BUTTON.getCode())) {
            throw new BusinessException(ErrorCodeEnum.CODE_2006, "按钮的父节点只能为菜单");
        }
    }


    private void checkSortIsExists(String menuType, Integer level, Integer sort, Long menuId) {
        LambdaQueryWrapper<MenuInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MenuInfoEntity::getMenuType, menuType);
        queryWrapper.eq(MenuInfoEntity::getLevel, level);
        queryWrapper.eq(MenuInfoEntity::getSort, sort);
        MenuInfoEntity menuInfo = this.getBaseMapper().selectOne(queryWrapper);
        if (null != menuInfo) {
            throw new BusinessException(ErrorCodeEnum.CODE_2002, "排序已存在");
        }
    }

    private void checkPath(String menuType, String path, Long menuId) {
        if (menuType.equals(MenuTypeEnum.MENU.getCode())) {
            throw new BusinessException(ErrorCodeEnum.CODE_2009, "路径不能为空");
        }
        LambdaQueryWrapper<MenuInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MenuInfoEntity::getPath, path);
        MenuInfoEntity menuInfo = this.getBaseMapper().selectOne(queryWrapper);
        if (checkIsExists(menuId, menuInfo)) {
            throw new BusinessException(ErrorCodeEnum.CODE_2003, "菜单路径已存在");
        }
    }

    private void checkPermission(String menuType, String permission, Long menuId) {
        if (menuType.equals(MenuTypeEnum.BUTTON.getCode())) {
            if (StringUtils.isNotBlank(permission)) {
                throw new BusinessException(ErrorCodeEnum.CODE_2005, "按钮权限编码不能为空");
            }
            LambdaQueryWrapper<MenuInfoEntity> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(MenuInfoEntity::getPermission, permission);
            MenuInfoEntity menuInfo = this.getBaseMapper().selectOne(queryWrapper);
            if (checkIsExists(menuId, menuInfo)) {
                throw new BusinessException(ErrorCodeEnum.CODE_2005, "权限编码已存在");
            }

        } else {
            if (StringUtils.isNotBlank(permission)) {
                throw new BusinessException(ErrorCodeEnum.CODE_2003, "只有菜单类型才允许设置权限编码");
            }
        }

    }

    private boolean checkIsExists(Long currentMenuId, MenuInfoEntity existsMenu) {
        boolean isExists = false;
        if (null != existsMenu) {
            if (null != currentMenuId) {
                if (!currentMenuId.equals(existsMenu.getMenuId())) {
                    isExists = true;
                }
            } else {
                isExists = true;
            }
        }
        return isExists;
    }


}




