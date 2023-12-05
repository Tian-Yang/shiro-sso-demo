package com.shiro.demo.controller;

import com.shiro.demo.annotations.UnAuth;
import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.bean.TestReq;
import com.shiro.demo.dto.AuthReqDto;
import com.shiro.demo.jwt.CsrfJwtTokenPayload;
import com.shiro.demo.principal.JwtPrincipalMap;
import com.shiro.demo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class AuthController {


    //@RequiresRoles("admin")
    //@RequiresPermissions("hello")
    @RequestMapping("/hello")
    @ResponseBody
    public String index() {
        log.info("hello");
        Subject subject = SecurityUtils.getSubject();
        log.info("is login:{}", subject.isAuthenticated());
        return "hello shiro";
    }


    @RequiresPermissions("account:create")
    @RequestMapping("/checkPermission")
    @ResponseBody
    public String checkPermission() {
        return "has Permission";
    }

    @UnAuth
    @RequestMapping("/requiresGuest")
    @ResponseBody
    public String requiresGuest() {
        log.info("requiresGuest");
        return "requiresGuest";
    }

    @PostMapping("/auth")
    @ResponseBody
    public CommonResp auth(HttpServletResponse httpServletResponse, @RequestBody AuthReqDto authReqDto) {
        Subject subject = SecurityUtils.getSubject();
        JwtPrincipalMap jwtPrincipalMap = (JwtPrincipalMap) subject.getPrincipals();
        CsrfJwtTokenPayload csrfJwtTokenPayload = new CsrfJwtTokenPayload();
        csrfJwtTokenPayload.setSub((String) jwtPrincipalMap.getPrimaryPrincipal());
        csrfJwtTokenPayload.setExp(JwtUtil.getExpirationTimeMillisOfSeconds(60));
        csrfJwtTokenPayload.setCsrfSerialNo(authReqDto.getCsrfSerialNo());
        csrfJwtTokenPayload.setCsrfSerialNo(authReqDto.getCsrfSerialNo());
        //生成CSRF Token
        String csrfToken = JwtUtil.createJwtToken(csrfJwtTokenPayload);
        httpServletResponse.setHeader("X-CSRF-TOKEN", csrfToken);
        return CommonResp.success(jwtPrincipalMap);
    }




    @PostMapping("/login")
    @ResponseBody
    public CommonResp<String> login(HttpServletRequest request) {
        String userName = request.getParameter("username");
        Subject currentUser = SecurityUtils.getSubject();
        boolean isAuthenticated = currentUser.isAuthenticated();
        String token = "";
        if (isAuthenticated) {
            JwtPrincipalMap jwtPrincipalMap = (JwtPrincipalMap) currentUser.getPrincipals();
            token = jwtPrincipalMap.getJwtToken();
        }
        log.info("userName:{},password:{}", userName);
        return CommonResp.success(token);
    }

    @PostMapping("/loginOut")
    @ResponseBody
    public CommonResp<Void> loginOut() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return CommonResp.success();
    }


    @PostMapping("/testReq")
    @ResponseBody
    public CommonResp testReq(@RequestBody TestReq testReq) {
        log.info("testReq:{}", testReq);
        return CommonResp.success();
    }


}
