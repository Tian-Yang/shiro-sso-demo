package com.shiro.demo.controller;

import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.vo.role.RoleAddVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理 Controller
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    //TODO 新增角色
    CommonResp<Void> add(@RequestBody RoleAddVO roleAddVO) {
        return CommonResp.success();
    }

    //TODO 修改角色

    //TODO 查看角色
}
