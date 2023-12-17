package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.entity.RoleInfoEntity;
import com.shiro.demo.mapper.RoleInfoMapper;
import com.shiro.demo.service.MenuInfoService;
import com.shiro.demo.service.RoleInfoService;
import com.shiro.demo.service.RoleMenuInfoService;
import com.shiro.demo.util.SnowflakeIdGenerator;
import com.shiro.demo.vo.menu.MenuVO;
import com.shiro.demo.vo.role.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TianYang
 * @description 针对表【role_info】的数据库操作Service实现
 * @createDate 2023-12-13 14:49:16
 */
@Service
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfoEntity>
        implements RoleInfoService {

    @Resource
    private RoleMenuInfoService roleMenuInfoService;

    @Resource
    private MenuInfoService menuInfoService;

    @Override
    public void addRole(RoleAddVO roleAddVO) {
        String roleName = roleAddVO.getRoleName();
        String roleCode = roleAddVO.getRoleCode();
        String roleStatus = roleAddVO.getRoleStatus();
        List<String> roleIds = roleAddVO.getMenuIds();

        Long roleId = SnowflakeIdGenerator.nextId();
        RoleInfoEntity roleInfoEntity = new RoleInfoEntity();
        roleInfoEntity.setRoleId(roleId);
        roleInfoEntity.setRoleCode(roleCode);
        roleInfoEntity.setRoleName(roleName);
        roleInfoEntity.setBusinessDomainCode(AuthContext.getBusinessDomainCode());
        roleInfoEntity.setStatus(Integer.parseInt(roleStatus));
        this.save(roleInfoEntity);
        if (!CollectionUtils.isEmpty(roleIds)) {
            roleMenuInfoService.addRoleMenuInfo(roleId, roleIds);
        }
    }

    @Override
    public IPage<RoleListQueryRespVO> pageQuery(RoleListQueryReqVO roleListQueryReqVO) {
        return this.baseMapper.pageQuery(roleListQueryReqVO);
    }

    @Override
    public RoleQueryRespVO query(RoleQueryReqVO req) {
        String roleId = req.getRoleId();
        RoleInfoEntity roleInfoEntity = this.baseMapper.selectById(roleId);
        RoleQueryRespVO respVO = new RoleQueryRespVO();
        if (null == roleInfoEntity) {
            return respVO;
        }
        BeanUtils.copyProperties(roleInfoEntity, respVO);
        List<MenuVO> menuList = menuInfoService.queryAllMenusByRoleId(roleInfoEntity.getRoleId());
        if (!CollectionUtils.isEmpty(menuList)) {
            respVO.setMenuList(menuList);
        }
        return respVO;
    }

    @Override
    public RoleInfoEntity queryById(Long roleId) {
        return this.baseMapper.selectById(roleId);
    }
}




