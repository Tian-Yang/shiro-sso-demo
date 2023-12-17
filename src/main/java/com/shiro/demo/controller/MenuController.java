package com.shiro.demo.controller;

import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.service.MenuInfoService;
import com.shiro.demo.vo.menu.MenuAddVO;
import com.shiro.demo.vo.menu.MenuQueryVO;
import com.shiro.demo.vo.menu.MenuVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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


    /**
     * 查询菜单:可访问的
     *
     * @return
     */
    @PostMapping("/queryAccessibleMenus")
    CommonResp<MenuVO> queryAccessibleMenus(@Validated @RequestBody MenuQueryVO menuQueryVO) {
       ;
        return CommonResp.success( menuInfoService.queryAccessibleMenus(menuQueryVO));
    }

    /**
     * 查询菜单:所有的
     *
     * @return
     */
    @PostMapping("/queryAllMenus")
    CommonResp<MenuVO> queryAllMenus(@Validated @RequestBody MenuQueryVO menuQueryVO) {
        List<MenuVO> menuVOList = menuInfoService.queryAllMenus(menuQueryVO);
        return CommonResp.success(menuVOList);
    }
}
