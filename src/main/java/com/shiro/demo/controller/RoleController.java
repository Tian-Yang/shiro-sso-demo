package com.shiro.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.service.RoleInfoService;
import com.shiro.demo.vo.PageResp;
import com.shiro.demo.vo.role.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色管理 Controller
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleInfoService roleInfoService;

    /**
     * 新增角色
     *
     * @param roleAddVO
     * @return
     */
    @PostMapping("/add")
    CommonResp<Void> add(@Validated @RequestBody RoleAddVO roleAddVO) {
        roleInfoService.addRole(roleAddVO);
        return CommonResp.success();
    }


    /**
     * 查询角色
     *
     * @param roleListQueryReqVO
     * @return
     */
    @PostMapping("/list")
    CommonResp<PageResp<RoleListQueryRespVO>> list(@Validated @RequestBody RoleListQueryReqVO roleListQueryReqVO) {
        return CommonResp.success(PageResp.parsePage(roleInfoService.pageQuery(roleListQueryReqVO)));
    }

    //TODO 修改角色

    /**
     * 查看角色
     *
     * @param reqVO
     * @return
     */
    @PostMapping("/query")
    CommonResp<RoleQueryRespVO> query(@Validated @RequestBody RoleQueryReqVO reqVO) {
        return CommonResp.success(roleInfoService.query(reqVO));
    }


}
