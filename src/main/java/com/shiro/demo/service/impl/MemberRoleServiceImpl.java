package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.entity.MemberRoleEntity;
import com.shiro.demo.mapper.MemberRoleMapper;
import com.shiro.demo.service.MemberRoleService;
import com.shiro.demo.util.SnowflakeIdGenerator;
import org.springframework.stereotype.Service;

/**
 * @author TianYang
 * @description 针对表【member_role】的数据库操作Service实现
 * @createDate 2023-12-14 14:11:24
 */
@Service
public class MemberRoleServiceImpl extends ServiceImpl<MemberRoleMapper, MemberRoleEntity>
        implements MemberRoleService {

    @Override
    public void addMenberRole(Long menberId, Long roleId) {
        MemberRoleEntity memberRoleEntity = new MemberRoleEntity();
        memberRoleEntity.setMemberRoleId(SnowflakeIdGenerator.nextId());
        memberRoleEntity.setMemberId(menberId);
        memberRoleEntity.setBusinessDomainCode(AuthContext.getBusinessDomainCode());
        memberRoleEntity.setRoleId(roleId);
        this.save(memberRoleEntity);
    }
}




