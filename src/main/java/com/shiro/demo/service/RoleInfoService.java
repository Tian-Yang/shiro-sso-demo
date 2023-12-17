package com.shiro.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.entity.RoleInfoEntity;
import com.shiro.demo.vo.role.*;

/**
 * @author TianYang
 * @description 针对表【role_info】的数据库操作Service
 * @createDate 2023-12-13 14:49:16
 */
public interface RoleInfoService extends IService<RoleInfoEntity> {
    void addRole(RoleAddVO roleAddVO);

    IPage<RoleListQueryRespVO> pageQuery(RoleListQueryReqVO roleListQueryReqVO);

    RoleQueryRespVO  query(RoleQueryReqVO req);

    RoleInfoEntity queryById(Long roleId);

}
