package com.shiro.demo.controller;

import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.service.MenuInfoService;
import com.shiro.demo.vo.member.MemuAddVO;
import com.shiro.demo.vo.menu.MenuAddVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 菜单管理 Controller
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuInfoService menuInfoService;

    /**
     * 新增菜单
     */
    @PostMapping("/add")
    CommonResp<Void> add(@Validated @RequestBody MenuAddVO menuAddVO) {
        menuInfoService.addMenu(menuAddVO);
        return CommonResp.success();
    }

    //TODO 修改菜单

    //TODO 查看菜单
}
