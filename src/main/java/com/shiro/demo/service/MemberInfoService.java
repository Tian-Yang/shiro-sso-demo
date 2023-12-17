package com.shiro.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.bean.MemberInfo;
import com.shiro.demo.constants.LocalCacheConstant;
import com.shiro.demo.entity.MemberInfoEntity;
import com.shiro.demo.vo.member.MemberAddReqVO;
import com.shiro.demo.vo.member.MemberAddRespVO;
import com.shiro.demo.vo.member.MemberListQueryReqVO;
import com.shiro.demo.vo.member.MemberListQueryRespVO;
import com.shiro.demo.vo.role.RoleListQueryReqVO;
import com.shiro.demo.vo.role.RoleListQueryRespVO;
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
    void addMembrerInfo(MemberInfoEntity memberInfoEntity, Long tenantId);

    /**
     * 新增用户信息
     *
     * @param memberAddReqVO
     * @return
     */
    MemberAddRespVO addMemberInfo(MemberAddReqVO memberAddReqVO);

    /**
     * 分页查询
     *
     * @param memberListQueryReqVO
     * @return
     */
    IPage<MemberListQueryRespVO> pageQuery(MemberListQueryReqVO memberListQueryReqVO);

    /**
     * 生成账户名
     *
     * @param chineseName
     * @return
     */
    String generateName(String chineseName);

    /**
     * 创建租户超级管理员
     *
     * @param tenantId
     */
    MemberInfoEntity createTenantSuperAdmin(Long tenantId);
}
