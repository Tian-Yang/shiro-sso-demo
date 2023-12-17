package com.shiro.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.entity.MemberRoleEntity;

/**
* @author TianYang
* @description 针对表【member_role】的数据库操作Service
* @createDate 2023-12-14 14:11:24
*/
public interface MemberRoleService extends IService<MemberRoleEntity> {
    void addMenberRole(Long menberId,Long roleId);
}
