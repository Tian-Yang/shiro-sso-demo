package com.shiro.demo.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.enums.MemberIdentityTypeEnum;
import com.shiro.demo.util.ClearUtil;
import com.shiro.demo.constants.RedisKeyConstant;
import com.shiro.demo.constants.RespCode;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.entity.BusinessDomainHostEntity;
import com.shiro.demo.entity.MemberInfoEntity;
import com.shiro.demo.enums.ErrorCodeEnum;
import com.shiro.demo.exception.BusinessException;
import com.shiro.demo.service.BusinessDomainHostService;
import com.shiro.demo.service.MemberInfoService;
import com.shiro.demo.service.RedisService;
import com.shiro.demo.token.UsernamePasswordBCryptToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户名密码认证过滤器
 * Author TianYang
 * Date 2023/11/30 14:42
 */
public class BasicUsernamePasswordLoginFilter extends BasicHttpAuthenticationFilter {

    @Resource
    private BusinessDomainHostService businessDomainHostService;

    @Resource
    private MemberInfoService memberInfoService;

    @Resource
    private RedisService redisService;

    protected AuthenticationToken createToken(String username, String password,
                                              ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        boolean rememberMe = isRememberMe(httpServletRequest);
        String host = getHost(httpServletRequest);
        //从X-Forwarded-Host中获取客户端host
        String clientHost = httpServletRequest.getHeader("X-Forwarded-Host");

        BusinessDomainHostEntity domainHostEntity = businessDomainHostService.queryByHost(clientHost);
        if (null == domainHostEntity) {
            throw new AuthenticationException(RespCode.CODE_1000);
        }
        //根据 Host查询对应的业务域编码、租户编码
        Long tenantId = domainHostEntity.getTenantId();
        String businessDomainCode = domainHostEntity.getBusinessDomainCode();
        AuthContext.setBusinessDomainCode(businessDomainCode);
        if (null != tenantId) {
            AuthContext.setTenantId(tenantId);
        }
        //根据userName+租户ID从数据库回去对应的凭证
        MemberInfoEntity memberInfo = memberInfoService.queryByAccountName(username);
        if (null == memberInfo) {
            throw new BusinessException(ErrorCodeEnum.CODE_1006, "用户名不存在");
        }

        String identityType = memberInfo.getIdentityType();
        if (null == tenantId) {
            if (!MemberIdentityTypeEnum.SAAS_SUPER_ADMIN.getCode().equals(identityType)) {
                throw new BusinessException(ErrorCodeEnum.CODE_1005, "账号非当前业务域管理员账号，禁止登录");
            }
        }


        UsernamePasswordBCryptToken usernamePasswordBCryptToken = new UsernamePasswordBCryptToken(username, password, rememberMe, host, clientHost);
        usernamePasswordBCryptToken.setMemberInfo(memberInfo);
        usernamePasswordBCryptToken.setBusinessDomainCode(businessDomainCode);
        return usernamePasswordBCryptToken;
    }


    /**
     * 扩展isAccessAllowed
     *
     * @param request     The current HTTP servlet request.
     * @param response    The current HTTP servlet response.
     * @param mappedValue The array of configured HTTP methods as strings. This is empty if no methods are configured.
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //增加图形验证码校验
        String captcha = StringUtils.EMPTY;
        String uid = StringUtils.EMPTY;
        captcha = httpServletRequest.getHeader("captcha");
        uid = httpServletRequest.getHeader("uid");
        /*try {
            BufferedReader reader = httpServletRequest.getReader();
            String requestBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            if(StringUtils.isNotBlank(requestBody)){
                JSONObject jsonObject= JSON.parseObject(requestBody);
                captcha=  jsonObject.getString("captcha");
                uid = jsonObject.getString("uid");
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCodeEnum.CODE_9999, "系统异常");
        }*/
        /*if (StringUtils.isBlank(captcha)) {
            throw new BusinessException(ErrorCodeEnum.CODE_1002, "登录验证码不能为空");
        }*/
        if (!redisService.hasKey(RedisKeyConstant.REDIS_GRAPH_CAPTCHA_KEY + uid)) {
            throw new BusinessException(ErrorCodeEnum.CODE_1003, "验证码错误或已失效");
        }
      /*  String existsCaptcha = (String) redisService.get(RedisKeyConstant.REDIS_GRAPH_CAPTCHA_KEY + uid);
        if (!captcha.equalsIgnoreCase(existsCaptcha)) {
            throw new BusinessException(ErrorCodeEnum.CODE_1004, "验证码错误");

        }*/
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        if (null != e) {
            try {
                response.getWriter().write(JSON.toJSONString(CommonResp.fail(Integer.parseInt(RespCode.CODE_401), e.getMessage())));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return false;
    }

    /* @Override
     public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws IOException {
     }
 */

    /**
     * 重写clearup方法对异常进行处理
     *
     * @param request  the incoming {@code ServletRequest}
     * @param response the outgoing {@code ServletResponse}
     * @param existing any exception that might have occurred while executing the {@code FilterChain} or
     *                 pre or post advice, or {@code null} if the pre/chain/post execution did not throw an {@code Exception}.
     * @throws IOException
     */
    @Override
    protected void cleanup(ServletRequest request, ServletResponse response, Exception existing)
            throws IOException {
        ClearUtil.cleanup(response, existing);
    }
}
