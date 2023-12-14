package com.shiro.demo.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.shiro.demo.annotations.UnAuth;
import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.bean.TestReq;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.dto.AuthReqDto;
import com.shiro.demo.enums.ErrorCodeEnum;
import com.shiro.demo.exception.BusinessException;
import com.shiro.demo.jwt.CsrfJwtTokenPayload;
import com.shiro.demo.principal.JwtPrincipalMap;
import com.shiro.demo.service.RedisService;
import com.shiro.demo.util.JwtUtil;
import com.shiro.demo.vo.login.GraphCaptchaVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.shiro.demo.constants.CommonCOnstant.RANDOM_BASE_STRING;
import static com.shiro.demo.constants.RedisKeyConstant.REDIS_GRAPH_CAPTCHA_KEY;

@Slf4j
@RestController
public class AuthController {

    @Resource
    private RedisService redisService;


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
        log.info("userName:{}", userName);
        return CommonResp.success(token);
    }

    /**
     * 获取验证码
     *
     * @param length
     * @return
     */
    @UnAuth
    @GetMapping("/obtain-graph-captcha")
    public CommonResp<GraphCaptchaVO> genGraphCaptcha(@RequestParam(required = false, defaultValue = "4") int length) {

        Assert.isTrue(length >= 4 && length <= 8,
                () -> new BusinessException(ErrorCodeEnum.CODE_1001, "验证码长度应为 4 到 8 之间!"));

        RandomGenerator randomGenerator = new RandomGenerator(RANDOM_BASE_STRING, length);
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(98, 36, length, 10);
        captcha.setGenerator(randomGenerator);
        captcha.createCode();
        String code = captcha.getCode();
        log.debug("图形验证码: {}", code);
        String uid = IdUtil.fastSimpleUUID();
        redisService.set(REDIS_GRAPH_CAPTCHA_KEY + uid, code, 5 * 60 * 1000);
        byte[] imageBytes = captcha.getImageBytes();
        String base64Captcha = "data:image/png;base64," + Base64.encode(imageBytes);
        return CommonResp.success(GraphCaptchaVO.builder().captcha(base64Captcha).uid(uid).build());
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
