package com.shiro.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.entity.RoleMenuInfoEntity;

import java.util.List;

/**
* @author TianYang
* @description 针对表【role_menu_info】的数据库操作Service
* @createDate 2023-12-14 14:11:11
*/
public interface RoleMenuInfoService extends IService<RoleMenuInfoEntity> {

    void addRoleMenuInfo(Long roleId, List<String> menuIds);

    void addList(List<RoleMenuInfoEntity> roleMenuList);

}
