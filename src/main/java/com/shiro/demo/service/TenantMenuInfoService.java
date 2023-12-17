package com.shiro.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.entity.TenantMenuInfoEntity;

import java.util.List;

/**
* @author TianYang
* @description 针对表【tenant_menu_info】的数据库操作Service
* @createDate 2023-12-17 15:12:45
*/
public interface TenantMenuInfoService extends IService<TenantMenuInfoEntity> {
        void addList(List<TenantMenuInfoEntity> tenantMenuInfoList);
}
