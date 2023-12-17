package com.shiro.demo.controller;

import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.service.TenantInfoService;
import com.shiro.demo.vo.tenant.TenantAddReqVO;
import com.shiro.demo.vo.tenant.TenantAddRespVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 租户管理 Controller
 */
@RestController
@RequestMapping("/tenant")
public class TenantController {


    @Resource
    private TenantInfoService tenantInfoService;

    /**
     * 新增租户
     *
     * @param tenantAddReqVO
     * @return
     */
    @PostMapping("/add")
    CommonResp<TenantAddRespVO> add(@RequestBody TenantAddReqVO tenantAddReqVO) {

        return CommonResp.success(tenantInfoService.add(tenantAddReqVO));
    }


}
