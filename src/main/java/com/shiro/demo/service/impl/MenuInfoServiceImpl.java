package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.entity.BusinessDomainHostEntity;
import com.shiro.demo.entity.MenuInfoEntity;
import com.shiro.demo.enums.ErrorCodeEnum;
import com.shiro.demo.enums.MenuPositionEnum;
import com.shiro.demo.enums.MenuTypeEnum;
import com.shiro.demo.exception.BusinessException;
import com.shiro.demo.mapper.MenuInfoMapper;
import com.shiro.demo.service.MenuInfoService;
import com.shiro.demo.util.SnowflakeIdGenerator;
import com.shiro.demo.vo.menu.MenuAddVO;
import com.shiro.demo.vo.menu.MenuQueryVO;
import com.shiro.demo.vo.menu.MenuVO;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AUTH;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Long parentMenuId = menuAddVO.getParentMenuId();
        //菜单名称
        String menuName = menuAddVO.getMenuName();
        //菜单路径
        String path = menuAddVO.getPath();
        //按钮权限
        String permission = menuAddVO.getPermission();
        //菜单、目录不要求填写权限编码，使用菜单路径自动填充
        permission = StringUtils.isBlank(permission) ? path : permission;
        String menuPosition = menuAddVO.getMenuPosition();
        menuPosition = StringUtils.isBlank(menuPosition) ? MenuPositionEnum.LEFT.getCode() : menuPosition;

        Integer level;
        menuAddVO.getMenuName();
        //设置父节点ID
        if (null == parentMenuId) {
            level = 1;
        } else {
            MenuInfoEntity parentMenu = this.getById(parentMenuId);
            //校验父节点
            checkParentMenu(parentMenu, menuType);
            //父节点层级
            Integer parentLevel = parentMenu.getLevel();
            level = parentLevel + 1;
        }
        //校验菜单名称是否存在
        checkMenuNameIsExists(menuName, null);
        //校验排序是否已存在
        checkSortIsExists(menuType, level, sort, parentMenuId, menuPosition, null);
        //校验路径
        checkPath(menuType, path, null);
        //校验权限编码
        checkPermission(menuType, permission, null);
        MenuInfoEntity menuInfo = new MenuInfoEntity();
        BeanUtils.copyProperties(menuAddVO, menuInfo);
        menuInfo.setMenuId(SnowflakeIdGenerator.nextId());
        menuInfo.setLevel(level);
        menuInfo.setBusinessDomainCode(AuthContext.getBusinessDomainCode());
        menuInfo.setMenuPosition(menuPosition);
        //如果permission为空，则用path填充，为了保证权限字段使用permission
        menuInfo.setPermission(permission);
        this.save(menuInfo);
    }

    @Override
    public List<MenuVO> queryAccessibleMenus(MenuQueryVO menuQueryVO) {
        return this.baseMapper.queryAccessibleMenus(AuthContext.getMemberId(), getQueryMenuPosition(menuQueryVO), AuthContext.getBusinessDomainCode(), AuthContext.getTenantId());
    }

    @Override
    public List<MenuVO> queryAllMenus(MenuQueryVO menuQueryVO) {
        Long tenantId = AuthContext.getTenantId();
        return this.baseMapper.queryAllMenus(getQueryMenuPosition(menuQueryVO), AuthContext.getBusinessDomainCode(), AuthContext.getTenantId());
    }

    @Override
    public List<MenuInfoEntity> queryAllMenus() {
        LambdaQueryWrapper<MenuInfoEntity> menuInfoEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuInfoEntityLambdaQueryWrapper.eq(MenuInfoEntity::getBusinessDomainCode, AuthContext.getBusinessDomainCode());
        return this.baseMapper.selectList(menuInfoEntityLambdaQueryWrapper);
    }

    @Override
    public List<MenuVO> queryAllMenusByRoleId(Long roleId) {
        return this.baseMapper.queryAllMenusByRoleId(roleId, AuthContext.getBusinessDomainCode(), AuthContext.getTenantId());
    }

    @Override
    public void initTenantMenu(Long tenantId) {

    }

    private void checkParentMenu(MenuInfoEntity parentMenu, String menuType) {
        if (null == parentMenu) {
            throw new BusinessException(ErrorCodeEnum.CODE_2001, "父菜单不存在");
        }
        String parentMenuType = parentMenu.getMenuType();
        //菜单和目录的父节点只能为目录
        if (menuType.equals(MenuTypeEnum.MENU.getCode()) || menuType.equals(MenuTypeEnum.CATALOG.getCode())) {
            if (!parentMenuType.equals(MenuTypeEnum.CATALOG.getCode())) {
                throw new BusinessException(ErrorCodeEnum.CODE_2007, "菜单和目录的父节点只能为目录");
            }
        }
        //按钮的父节点只能为菜单
        else if (menuType.equals(MenuTypeEnum.BUTTON.getCode())) {
            if (!parentMenuType.equals(MenuTypeEnum.MENU.getCode())) {
                throw new BusinessException(ErrorCodeEnum.CODE_2008, "按钮的父节点只能为菜单");
            }
        }
    }


    private void checkMenuNameIsExists(String menuName, Long menuId) {
        LambdaQueryWrapper<MenuInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MenuInfoEntity::getMenuName, menuName);
        queryWrapper.eq(MenuInfoEntity::getBusinessDomainCode, AuthContext.getBusinessDomainCode());

        MenuInfoEntity menuInfo = this.getBaseMapper().selectOne(queryWrapper);
        if (checkIsExists(menuId, menuInfo)) {
            throw new BusinessException(ErrorCodeEnum.CODE_2010, "菜单名称已存在");
        }
    }

    private void checkSortIsExists(String menuType, Integer level, Integer sort, Long parentMenuId, String menuPosition, Long menuId) {
        LambdaQueryWrapper<MenuInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MenuInfoEntity::getMenuType, menuType);
        queryWrapper.eq(MenuInfoEntity::getLevel, level);
        queryWrapper.eq(MenuInfoEntity::getSort, sort);
        queryWrapper.eq(MenuInfoEntity::getMenuPosition, menuPosition);
        if (null != parentMenuId) {
            queryWrapper.eq(MenuInfoEntity::getParentMenuId, parentMenuId);
        }
        queryWrapper.eq(MenuInfoEntity::getBusinessDomainCode, AuthContext.getBusinessDomainCode());

        MenuInfoEntity menuInfo = this.getBaseMapper().selectOne(queryWrapper);
        if (checkIsExists(menuId, menuInfo)) {
            throw new BusinessException(ErrorCodeEnum.CODE_2002, "排序已存在");
        }
    }

    private void checkPath(String menuType, String path, Long menuId) {
        if (menuType.equals(MenuTypeEnum.MENU.getCode()) || menuType.equals(MenuTypeEnum.CATALOG.getCode())) {
            if (StringUtils.isBlank(menuType)) {
                throw new BusinessException(ErrorCodeEnum.CODE_2009, "路径不能为空");
            }
        }
        LambdaQueryWrapper<MenuInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MenuInfoEntity::getPath, path);
        queryWrapper.eq(MenuInfoEntity::getBusinessDomainCode, AuthContext.getBusinessDomainCode());
        MenuInfoEntity menuInfo = this.getBaseMapper().selectOne(queryWrapper);
        if (checkIsExists(menuId, menuInfo)) {
            throw new BusinessException(ErrorCodeEnum.CODE_2003, "菜单路径已存在");
        }
    }

    private void checkPermission(String menuType, String permission, Long menuId) {
        if (StringUtils.isBlank(permission)) {
            throw new BusinessException(ErrorCodeEnum.CODE_2005, "按钮权限编码不能为空");
        }
        LambdaQueryWrapper<MenuInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MenuInfoEntity::getPermission, permission);
        queryWrapper.eq(MenuInfoEntity::getBusinessDomainCode, AuthContext.getBusinessDomainCode());
        MenuInfoEntity menuInfo = this.getBaseMapper().selectOne(queryWrapper);
        if (checkIsExists(menuId, menuInfo)) {
            if (menuType.equals(MenuTypeEnum.BUTTON.getCode())) {
                throw new BusinessException(ErrorCodeEnum.CODE_2006, "按钮权限编码已存在");
            } else {
                throw new BusinessException(ErrorCodeEnum.CODE_2011, "菜单或目录的路径与按钮权限编码冲突");
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


    private String getQueryMenuPosition(MenuQueryVO menuQueryVO) {
        String menuPosition = menuQueryVO.getMenuPosition();
        return StringUtils.isBlank(menuPosition) ? MenuPositionEnum.LEFT.getCode() : menuPosition;
    }


}




