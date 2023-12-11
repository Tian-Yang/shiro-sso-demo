package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.cache.LocalCache;
import com.shiro.demo.constants.LocalCacheConstant;
import com.shiro.demo.entity.BusinessDomainEntity;
import com.shiro.demo.entity.BusinessDomainHostEntity;
import com.shiro.demo.mapper.BusinessDomainHostMapper;
import com.shiro.demo.service.BusinessDomainHostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author TianYang
 * @description 针对表【business_domain_host】的数据库操作Service实现
 * @createDate 2023-12-10 10:43:11
 */
@Service
public class BusinessDomainHostServiceImpl extends ServiceImpl<BusinessDomainHostMapper, BusinessDomainHostEntity>
        implements BusinessDomainHostService {
    @Resource
    private LocalCache localCache;

    @Override
    public BusinessDomainHostEntity queryByHost(String host) {
        String key = LocalCacheConstant.BUSINESS_DOMAIN_HOST_CACHE + "_" + host;
        BusinessDomainHostEntity cachedBusinessDomainHostEntity = (BusinessDomainHostEntity) localCache.get(key);
        if (null != cachedBusinessDomainHostEntity) {
            return cachedBusinessDomainHostEntity;
        }
        LambdaQueryWrapper<BusinessDomainHostEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(BusinessDomainHostEntity::getHostName, host);
        BusinessDomainHostEntity businessDomainHostEntity = this.getBaseMapper().selectOne(queryWrapper);
        localCache.put(key, businessDomainHostEntity);
        return businessDomainHostEntity;
    }
}




