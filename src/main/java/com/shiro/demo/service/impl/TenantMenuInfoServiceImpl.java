package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.entity.TenantMenuInfoEntity;
import com.shiro.demo.mapper.TenantMenuInfoMapper;
import com.shiro.demo.service.TenantMenuInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TianYang
 * @description 针对表【tenant_menu_info】的数据库操作Service实现
 * @createDate 2023-12-17 15:12:45
 */
@Service
public class TenantMenuInfoServiceImpl extends ServiceImpl<TenantMenuInfoMapper, TenantMenuInfoEntity>
        implements TenantMenuInfoService {

    @Override
    public void addList(List<TenantMenuInfoEntity> tenantMenuInfoList) {
        this.saveBatch(tenantMenuInfoList);
    }
}




