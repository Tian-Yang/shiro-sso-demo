package com.shiro.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.constants.LocalCacheConstant;
import com.shiro.demo.entity.BusinessDomainEntity;
import com.shiro.demo.entity.BusinessDomainHostEntity;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author TianYang
 * @description 针对表【business_domain_host】的数据库操作Service
 * @createDate 2023-12-10 10:43:11
 */
public interface BusinessDomainHostService extends IService<BusinessDomainHostEntity> {

    /**
     * 根据 Host查询业务域Host信息
     *
     * @param host
     * @return com.shiro.demo.entity.BusinessDomainHostEntity
     * @Author TianYang
     * @Date 2023/12/10 15:12
     */
    BusinessDomainHostEntity queryByHost(String host);
}
