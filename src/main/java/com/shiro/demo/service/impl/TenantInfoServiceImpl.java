package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.entity.MemberInfoEntity;
import com.shiro.demo.entity.TenantInfoEntity;
import com.shiro.demo.enums.ErrorCodeEnum;
import com.shiro.demo.exception.BusinessException;
import com.shiro.demo.mapper.TenantInfoMapper;
import com.shiro.demo.service.MemberInfoService;
import com.shiro.demo.service.TenantInfoService;
import com.shiro.demo.vo.tenant.TenantAddReqVO;
import com.shiro.demo.vo.tenant.TenantAddRespVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author TianYang
 * @description 针对表【tenant_info】的数据库操作Service实现
 * @createDate 2023-12-17 15:12:26
 */
@Service
public class TenantInfoServiceImpl extends ServiceImpl<TenantInfoMapper, TenantInfoEntity>
        implements TenantInfoService {

    @Resource
    private MemberInfoService memberInfoService;

    @Transactional
    @Override
    public TenantAddRespVO add(TenantAddReqVO tenantAddReqVO) {
        String tenantName = tenantAddReqVO.getTenantName();
        //校验租户名称是否已存在
        checkTenantNameExists(tenantName, null);
        TenantInfoEntity tenantInfoEntity = new TenantInfoEntity();
        tenantInfoEntity.setTenantName(tenantName);
        tenantInfoEntity.setBusinessDomainCode(AuthContext.getBusinessDomainCode());
        //新增租户信息
        this.save(tenantInfoEntity);
        Long tenantId = tenantInfoEntity.getTenantId();
        TenantAddRespVO resp = new TenantAddRespVO();
        resp.setTenantId(tenantId);
        //新增租户超级管理员账号
        MemberInfoEntity memberInfo = memberInfoService.createTenantSuperAdmin(tenantId);
        resp.setMemberId(memberInfo.getMemberId());
        resp.setAccountName(memberInfo.getAccountName());
        return resp;
    }

    private void checkTenantNameExists(String tenantName, Long tenantId) {
        LambdaQueryWrapper<TenantInfoEntity> tenantQueryWrapper = new LambdaQueryWrapper<>();
        tenantQueryWrapper.eq(TenantInfoEntity::getTenantName, tenantName);
        TenantInfoEntity existsTenantInfo = this.baseMapper.selectOne(tenantQueryWrapper);
        if (null != existsTenantInfo) {
            if (null != tenantId) {
                if (tenantId.equals(existsTenantInfo.getTenantId())) {
                    throw new BusinessException(ErrorCodeEnum.CODE_5001, "租户名称已存在");
                }
            } else {
                throw new BusinessException(ErrorCodeEnum.CODE_5001, "租户名称已存在");
            }
        }
    }


}




