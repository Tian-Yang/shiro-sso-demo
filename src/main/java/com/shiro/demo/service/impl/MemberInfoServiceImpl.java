package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.bean.MemberInfo;
import com.shiro.demo.cache.LocalCache;
import com.shiro.demo.constants.LocalCacheConstant;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.entity.BusinessDomainHostEntity;
import com.shiro.demo.entity.MemberInfoEntity;
import com.shiro.demo.mapper.MemberInfoEntityMapper;
import com.shiro.demo.service.MemberInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author TianYang
 * @description 针对表【member_info】的数据库操作Service实现
 * @createDate 2023-12-10 16:51:34
 */
@Service
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoEntityMapper, MemberInfoEntity>
        implements MemberInfoService {

    @Resource
    private LocalCache localCache;

    @Override
    public MemberInfoEntity queryByAccountName(String accountName) {
        LambdaQueryWrapper<MemberInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MemberInfoEntity::getAccountName, accountName);
        Long tenantId = AuthContext.getTenantId();
        if (null != tenantId) {
            queryWrapper.eq(MemberInfoEntity::getTenantId, tenantId);
        }
        String key = LocalCacheConstant.MEMBER_INFO_CACHE + "_" + accountName + "_" + tenantId;
        MemberInfoEntity cachedMemberInfo = (MemberInfoEntity) localCache.get(key);
        if (null != cachedMemberInfo) {
            return cachedMemberInfo;
        }
        MemberInfoEntity memberInfoEntity = this.getBaseMapper().selectOne(queryWrapper);
        localCache.put(key, memberInfoEntity);
        return memberInfoEntity;
    }

    public void addMembrerInfo(MemberInfoEntity memberInfoEntity, Long tenantId) {
        this.save(memberInfoEntity);
    }

    /**
     * 账号名唯一性校验
     *
     * @param accountName
     */
    private boolean accountNameUniquenessValidate(String accountName) {
        LambdaQueryWrapper<MemberInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MemberInfoEntity::getAccountName, accountName);
        return this.getBaseMapper().exists(queryWrapper);
    }


}




