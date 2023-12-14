package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.entity.RoleInfoEntity;
import com.shiro.demo.mapper.RoleInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author TianYang
* @description 针对表【role_info】的数据库操作Service实现
* @createDate 2023-12-13 14:49:16
*/
@Service
public class RoleinfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfoEntity>
    implements IService<RoleInfoEntity> {

}




