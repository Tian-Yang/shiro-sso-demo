package com.shiro.demo.controller;

import com.shiro.demo.bean.CommonResp;
import org.springframework.web.bind.annotation.*;

/**
 * 认证系统 Member API
 * Author TianYang
 * Date 2023/12/7 14:35
 */
@RestController("/member")
public class MemberController {

    @GetMapping("/query")
    public CommonResp<Void> query() {
        return CommonResp.success();
    }

    @PostMapping("/add")
    public CommonResp<Void> add() {
        return CommonResp.success();
    }

    @PutMapping("/update")
    public CommonResp<Void> update() {
        return CommonResp.success();
    }

    @DeleteMapping("/delete")
    public CommonResp<Void> delete() {
        return CommonResp.success();
    }


}
