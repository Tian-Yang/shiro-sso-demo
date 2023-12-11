package com.shiro.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.entity.BusinessDomainEntity;
import com.shiro.demo.mapper.BusinessDomainMapper;
import com.shiro.demo.service.BusinessDomainService;
import org.springframework.stereotype.Service;

/**
* @author TianYang
* @description 针对表【business_domain】的数据库操作Service实现
* @createDate 2023-12-10 10:18:28
*/
@Service
public class BusinessDomainServiceImpl extends ServiceImpl<BusinessDomainMapper, BusinessDomainEntity>
        implements BusinessDomainService {

}




