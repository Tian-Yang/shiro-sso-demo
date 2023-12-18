package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.cache.LocalCache;
import com.shiro.demo.constants.LocalCacheConstant;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.entity.*;
import com.shiro.demo.enums.ErrorCodeEnum;
import com.shiro.demo.enums.MemberIdentityTypeEnum;
import com.shiro.demo.exception.BusinessException;
import com.shiro.demo.mapper.MemberInfoMapper;
import com.shiro.demo.service.*;
import com.shiro.demo.util.CharacterUtil;
import com.shiro.demo.util.PasswordEncryption;
import com.shiro.demo.util.SnowflakeIdGenerator;
import com.shiro.demo.vo.member.MemberAddReqVO;
import com.shiro.demo.vo.member.MemberAddRespVO;
import com.shiro.demo.vo.member.MemberListQueryReqVO;
import com.shiro.demo.vo.member.MemberListQueryRespVO;
import com.shiro.demo.vo.menu.MenuQueryVO;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TianYang
 * @description 针对表【member_info】的数据库操作Service实现
 * @createDate 2023-12-10 16:51:34
 */
@Service
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfoEntity>
        implements MemberInfoService {

    @Resource
    private LocalCache localCache;

    @Resource
    private RoleInfoService roleInfoService;

    @Resource
    private MemberRoleService memberRoleService;

    @Resource
    private MenuInfoService menuInfoService;

    @Resource
    private RoleMenuInfoService roleMenuInfoService;

    @Resource
    private TenantMenuInfoService tenantMenuInfoService;

    @Override
    public MemberInfoEntity queryByAccountName(String accountName) {
        LambdaQueryWrapper<MemberInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MemberInfoEntity::getAccountName, accountName);
        queryWrapper.eq(MemberInfoEntity::getBusinessDomainCode, AuthContext.getBusinessDomainCode());
        Long tenantId = AuthContext.getTenantId();
        if (null == tenantId) {
            queryWrapper.isNull(MemberInfoEntity::getTenantId);
        }
        String key = LocalCacheConstant.MEMBER_INFO_CACHE + "_" + accountName + "_" + tenantId;
        MemberInfoEntity cachedMemberInfo = (MemberInfoEntity) localCache.get(key);
        if (null != cachedMemberInfo) {
            return cachedMemberInfo;
        }
        MemberInfoEntity memberInfoEntity = this.getBaseMapper().selectOne(queryWrapper);
        if (null == memberInfoEntity) {
            return null;
        }
        localCache.put(key, memberInfoEntity);
        return memberInfoEntity;
    }

    public void addMembrerInfo(MemberInfoEntity memberInfoEntity, Long tenantId) {
        this.save(memberInfoEntity);
    }

    @Transactional
    @Override
    public MemberAddRespVO addMemberInfo(MemberAddReqVO memberAddReqVO) {
        String password = memberAddReqVO.getPassword();
        String accountName = memberAddReqVO.getAccountName();
        String realName = memberAddReqVO.getRealName();
        String status = memberAddReqVO.getStatus();
        String roleId = memberAddReqVO.getRoleId();

        //校验账户名是否存在
        if (accountNameUniquenessValidate(accountName)) {
            throw new BusinessException(ErrorCodeEnum.CODE_4001, "账户名已存在");
        }
        //校验角色ID是否已存在
        RoleInfoEntity existsRole = roleInfoService.queryById(Long.parseLong(roleId));
        if (null == existsRole) {
            throw new BusinessException(ErrorCodeEnum.CODE_4002, "账号关联的角色不存在");
        }
        MemberInfoEntity memberInfoEntity = new MemberInfoEntity();
        memberInfoEntity.setPassword(PasswordEncryption.bCryptHashpw(password));
        memberInfoEntity.setAccountName(accountName);
        memberInfoEntity.setRealName(realName);
        memberInfoEntity.setStatus(Integer.parseInt(status));
        Long menberId = addMenberInfo(memberInfoEntity, MemberIdentityTypeEnum.SAAS_AMIN);
        //新增用户、角色关系
        memberRoleService.addMenberRole(menberId, Long.parseLong(roleId));
        MemberAddRespVO resp = new MemberAddRespVO();
        resp.setMemberId(menberId);
        return resp;
    }

    @Override
    public IPage<MemberListQueryRespVO> pageQuery(MemberListQueryReqVO memberListQueryReqVO) {
        return this.baseMapper.pageQuery(memberListQueryReqVO);
    }

    @Override
    public String generateName(String chineseName) {
        try {
            String pinyinName = CharacterUtil.chineseToPinying(chineseName);
            LambdaQueryWrapper<MemberInfoEntity> memberQueryWrapper = new LambdaQueryWrapper<>();
            memberQueryWrapper.eq(MemberInfoEntity::getRealName, chineseName);
            long exitsNums = this.count(memberQueryWrapper);
            if (exitsNums > 0) {
                pinyinName = pinyinName + exitsNums;
            }
            return pinyinName;
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public MemberInfoEntity createTenantSuperAdmin(Long tenantId) {
        //创建租户管理员用户账号
        MemberInfoEntity memberInfoEntity = new MemberInfoEntity();
        memberInfoEntity.setTenantId(tenantId);
        memberInfoEntity.setAccountName("admin");
        memberInfoEntity.setIdentityType(MemberIdentityTypeEnum.SAAS_SUPER_ADMIN.getCode());
        memberInfoEntity.setPassword("$2a$10$b2IXlkgBNv5xBYQkrNNRSuMn8CA3Q8jsx7YUuXLUFbY0aIHarosgG");
        memberInfoEntity.setBusinessDomainCode(AuthContext.getBusinessDomainCode());
        this.save(memberInfoEntity);
        Long memberId = memberInfoEntity.getMemberId();
        Long roleId = SnowflakeIdGenerator.nextId();
        //创建租户超管角色
        RoleInfoEntity roleInfo = new RoleInfoEntity();
        roleInfo.setRoleId(roleId);
        roleInfo.setRoleCode("tenant_super_admin");
        roleInfo.setRoleName("租户超级管理员");
        roleInfo.setBusinessDomainCode(AuthContext.getBusinessDomainCode());
        roleInfo.setTenantId(tenantId);
        roleInfoService.save(roleInfo);
        //创建租户菜单关联数据
        List<MenuInfoEntity> menuList = menuInfoService.queryAllMenus();
        if (!CollectionUtils.isEmpty(menuList)) {

            List<TenantMenuInfoEntity> tenantMenuList = new ArrayList<>();
            List<RoleMenuInfoEntity> roleMenuList = new ArrayList<>();
            for (MenuInfoEntity menu : menuList) {
                Long menuId = menu.getMenuId();
                //新增租户菜单数据
                TenantMenuInfoEntity tenantMenuInfoEntity = new TenantMenuInfoEntity();
                tenantMenuInfoEntity.setTenantMenuId(SnowflakeIdGenerator.nextId());
                tenantMenuInfoEntity.setTenantId(tenantId);
                tenantMenuInfoEntity.setMenuId(menuId);
                tenantMenuInfoEntity.setBusinessDomainCode(AuthContext.getBusinessDomainCode());
                tenantMenuList.add(tenantMenuInfoEntity);
                //新增角色菜单信息
                RoleMenuInfoEntity roleMenuInfoEntity = new RoleMenuInfoEntity();
                roleMenuInfoEntity.setRoleMenuId(SnowflakeIdGenerator.nextId());
                roleMenuInfoEntity.setTenantId(tenantId);
                roleMenuInfoEntity.setRoleId(roleId);
                roleMenuInfoEntity.setMenuId(menuId);
                roleMenuInfoEntity.setBusinessDomainCode(AuthContext.getBusinessDomainCode());
                roleMenuList.add(roleMenuInfoEntity);
            }
            //新增租户菜单表
            tenantMenuInfoService.addList(tenantMenuList);
            //创建租户角色与菜单关联关系
            roleMenuInfoService.addList(roleMenuList);
        }

        //创建租户用户与角色关联信息
        MemberRoleEntity memberRoleEntity = new MemberRoleEntity();
        memberRoleEntity.setMemberRoleId(SnowflakeIdGenerator.nextId());
        memberRoleEntity.setMemberId(memberId);
        memberRoleEntity.setRoleId(roleId);
        memberRoleEntity.setTenantId(tenantId);
        memberRoleService.save(memberRoleEntity);
        return memberInfoEntity;
    }

    private Long addMenberInfo(MemberInfoEntity memberInfoEntity, MemberIdentityTypeEnum memberIdentityTypeEnum) {
        memberInfoEntity.setIdentityType(memberIdentityTypeEnum.getCode());
        memberInfoEntity.setBusinessDomainCode(AuthContext.getBusinessDomainCode());
        Long tenantId = AuthContext.getTenantId();
        if (null != tenantId) {
            memberInfoEntity.setTenantId(tenantId);
        }
        this.save(memberInfoEntity);
        Long menberId = memberInfoEntity.getMemberId();
        return menberId;
    }

    /**
     * 账号名唯一性校验
     *
     * @param accountName
     */
    private boolean accountNameUniquenessValidate(String accountName) {
        LambdaQueryWrapper<MemberInfoEntity> acctQueryWapper = Wrappers.lambdaQuery();
        acctQueryWapper.eq(MemberInfoEntity::getAccountName, accountName);
        acctQueryWapper.eq(MemberInfoEntity::getBusinessDomainCode, AuthContext.getBusinessDomainCode());
        Long tenantId = AuthContext.getTenantId();
        if (null != tenantId) {
            acctQueryWapper.eq(MemberInfoEntity::getTenantId, tenantId);
        }
        return this.getBaseMapper().exists(acctQueryWapper);
    }

    private String getIndenityType() {
        Long tenantId = AuthContext.getTenantId();
        String businessDomainCode = AuthContext.getBusinessDomainCode();
        return null;
    }


}




