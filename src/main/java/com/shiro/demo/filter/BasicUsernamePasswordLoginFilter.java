package com.shiro.demo.filter;

import com.alibaba.fastjson.JSON;
import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.constants.RespCode;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.entity.BusinessDomainHostEntity;
import com.shiro.demo.entity.MemberInfoEntity;
import com.shiro.demo.service.BusinessDomainHostService;
import com.shiro.demo.service.MemberInfoService;
import com.shiro.demo.token.UsernamePasswordBCryptToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
            throw new AuthenticationException(RespCode.CODE_1001);
        }


        UsernamePasswordBCryptToken usernamePasswordBCryptToken = new UsernamePasswordBCryptToken(username, password, rememberMe, host, clientHost);
        usernamePasswordBCryptToken.setMemberInfo(memberInfo);
        usernamePasswordBCryptToken.setBusinessDomainCode(businessDomainCode);
        return usernamePasswordBCryptToken;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        if (null != e) {
            try {
                response.getWriter().write(JSON.toJSONString(CommonResp.fail(RespCode.CODE_401, e.getMessage())));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return false;
    }

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) {
        AuthContext.clear();
    }
}
