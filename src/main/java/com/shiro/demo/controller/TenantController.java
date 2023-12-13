package com.shiro.demo.controller;

import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.vo.tenant.TenantAddReqVO;
import com.shiro.demo.vo.tenant.TenantAddRespVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户管理 Controller
 */
@RestController
@RequestMapping("/tenant")
public class TenantController {

    // 新增租户
    @PostMapping("/add")
    CommonResp<TenantAddRespVO> add(@RequestBody TenantAddReqVO tenantAddReqVO) {
        TenantAddRespVO respVO = new TenantAddRespVO();
        //TODO 新增租户逻辑
        return CommonResp.success(respVO);
    }

}
