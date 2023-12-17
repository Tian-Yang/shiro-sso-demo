package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.entity.RoleMenuInfoEntity;
import com.shiro.demo.enums.ErrorCodeEnum;
import com.shiro.demo.exception.BusinessException;
import com.shiro.demo.mapper.RoleMenuInfoMapper;
import com.shiro.demo.service.MenuInfoService;
import com.shiro.demo.service.RoleMenuInfoService;
import com.shiro.demo.util.ListUtil;
import com.shiro.demo.util.SnowflakeIdGenerator;
import com.shiro.demo.vo.menu.MenuQueryVO;
import com.shiro.demo.vo.menu.MenuVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author TianYang
 * @description 针对表【role_menu_info】的数据库操作Service实现
 * @createDate 2023-12-14 14:11:11
 */
@Service
public class RoleMenuInfoServiceImpl extends ServiceImpl<RoleMenuInfoMapper, RoleMenuInfoEntity>
        implements RoleMenuInfoService {

    @Resource
    private MenuInfoService menuInfoService;

    @Transactional
    @Override
    public void addRoleMenuInfo(Long roleId, List<String> menuIds) {
        //校验menuIds对应的菜单是否存在
        checkMenuList(menuIds);
        //去除重复
        menuIds = menuIds.stream().distinct().collect(Collectors.toList());
        //查询当前已存在角色菜单关系
        List<RoleMenuInfoEntity> existsRoleMenuList = queryByRoleId(roleId);
        List<RoleMenuInfoEntity> updateRoleMenuList = new ArrayList<>();
        for (String menuId : menuIds) {
            RoleMenuInfoEntity roleMenu = new RoleMenuInfoEntity();
            roleMenu.setRoleId(roleId);
            roleMenu.setRoleMenuId(Long.valueOf(menuId));
            roleMenu.setMenuId(Long.parseLong(menuId));
            updateRoleMenuList.add(roleMenu);
        }
        Map<Long, RoleMenuInfoEntity> existsRoleMenuMap = ListUtil.convertListToMap(existsRoleMenuList, RoleMenuInfoEntity::getMenuId);

        List<RoleMenuInfoEntity> addedElements = updateRoleMenuList.stream()
                .filter(roleMenu -> !existsRoleMenuMap.containsKey(roleMenu.getMenuId()))
                .map(roleMenu -> {
                    roleMenu.setRoleMenuId(SnowflakeIdGenerator.nextId());
                    roleMenu.setBusinessDomainCode(AuthContext.getBusinessDomainCode());
                    return roleMenu;
                })
                .collect(Collectors.toList());

        List<RoleMenuInfoEntity> removedElements = existsRoleMenuMap.values().stream()
                .filter(existingRoleMenu -> !updateRoleMenuList.stream()
                        .anyMatch(roleMenu -> roleMenu.getRoleMenuId().equals(existingRoleMenu.getMenuId())))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(addedElements)) {
            batchInsert(addedElements);
        }
        if (!CollectionUtils.isEmpty(removedElements)) {
            batchDelete(removedElements);
        }
    }

    @Override
    public void addList(List<RoleMenuInfoEntity> roleMenuList) {
        this.batchInsert(roleMenuList);
    }

    public void batchInsert(List<RoleMenuInfoEntity> roleMenuInfoList) {
        this.saveBatch(roleMenuInfoList);
    }

    public void batchDelete(List<RoleMenuInfoEntity> roleMenuInfoList) {
        roleMenuInfoList.stream().forEach(roleMenu -> {
            roleMenu.setDelFlag(roleMenu.getRoleMenuId());
        });
        this.updateBatchById(roleMenuInfoList);

    }

    private void checkMenuList(List<String> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return;
        }
        List<MenuVO> allMenus = menuInfoService.queryAllMenus(new MenuQueryVO());
        Map<Long, MenuVO> allMenusMap = ListUtil.convertListToMap(allMenus, MenuVO::getMenuId);
        List<String> notExistList = menuIds.stream().filter(menuId -> !allMenusMap.containsKey(Long.parseLong(menuId)))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(notExistList)) {
            String errorMsgFmt = "以下菜单ID不存在%s";
            String errorMsg = MessageFormat.format(errorMsgFmt, String.join(", ", notExistList));
            ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.CODE_3001;
            errorCodeEnum.setMessage(errorMsg);
            throw new BusinessException(errorCodeEnum, errorMsg);
        }
    }

    private List<RoleMenuInfoEntity> queryByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenuInfoEntity> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(RoleMenuInfoEntity::getRoleId, roleId);
        return this.baseMapper.selectList(queryWrapper);
    }


}




