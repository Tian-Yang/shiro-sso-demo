package com.shiro.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.bean.MemberInfo;
import com.shiro.demo.constants.LocalCacheConstant;
import com.shiro.demo.entity.MemberInfoEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.lang.reflect.Member;

/**
 * @author TianYang
 * @description 针对表【member_info】的数据库操作Service
 * @createDate 2023-12-10 16:51:34
 */
public interface MemberInfoService extends IService<MemberInfoEntity> {

    /**
     * 根据账号名查询用户信息
     *
     * @param accountName
     * @return
     */
    MemberInfoEntity queryByAccountName(String accountName);

    /**
     * 新增用户信息
     *
     * @param memberInfoEntity
     */
    @CacheEvict(value =LocalCacheConstant.MEMBER_INFO_CACHE,key = "#accountName+'_'+#tenantId")
    void addMembrerInfo(MemberInfoEntity memberInfoEntity,Long tenantId);
}
