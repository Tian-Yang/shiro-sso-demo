package com.shiro.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.entity.TenantInfoEntity;
import com.shiro.demo.vo.tenant.TenantAddReqVO;
import com.shiro.demo.vo.tenant.TenantAddRespVO;

/**
* @author TianYang
* @description 针对表【tenant_info】的数据库操作Service
* @createDate 2023-12-17 15:12:26
*/
public interface TenantInfoService extends IService<TenantInfoEntity> {

    /**
     * 新增租户
     * @param tenantAddReqVO
     * @return
     */
    TenantAddRespVO add(TenantAddReqVO tenantAddReqVO);


}
