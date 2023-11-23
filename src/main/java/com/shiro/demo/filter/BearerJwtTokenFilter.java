package com.shiro.demo.filter;

import com.alibaba.fastjson.JSON;
import com.shiro.demo.annotations.UnAuth;
import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.constans.RespCode;
import com.shiro.demo.context.HandlerMappingContext;
import com.shiro.demo.token.BearerJwtToken;
import org.apache.http.auth.AUTH;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BearerHttpAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt Token处理过滤器
 *
 * @Desc 自定义过滤器，处理Http Bearer协议携带的Jwt Token的请求
 * Author TianYang
 * Date 2023/11/14 16:30
 */
public class BearerJwtTokenFilter extends BearerHttpAuthenticationFilter {


    @Resource
    HandlerMappingContext handlerMappingContext;

    private Map<String, Boolean> requestAuthMap = new HashMap<>();

    /**
     * 重写createBearerToken方法
     *
     * @param token
     * @param request
     * @return
     */
    protected AuthenticationToken createBearerToken(String token, ServletRequest request) {
        return new BearerJwtToken(token, request.getRemoteHost());
    }


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

    /**
     * 重写onAccessDenied方法，定义返回
     *
     * @param request
     * @param response
     * @return boolean
     * @Author TianYang
     * @Date 2023/11/17 16:13
     */
  /*  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        response.getWriter().write(JSON.toJSONString(CommonResp.fail(RespCode.CODE_401, "")));
        return false;
    }*/
    @Override
    protected void cleanup(ServletRequest request, ServletResponse response, Exception existing)
            throws ServletException, IOException {
        super.cleanup(request, response, existing);
        if (null != existing) {
            response.getWriter().write(JSON.toJSONString(CommonResp.fail(RespCode.CODE_401, existing.getMessage())));
        }
    }

    /**
     * 重写isAccessAllowed，扩展自定义注解免认证功能。
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return boolean
     * @Author TianYang
     * @Date 2023/11/21 17:42
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        if (isUnAuthMethod(request)) {
            return true;
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }


    private boolean isUnAuthMethod(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();
        if (requestAuthMap.containsKey(requestURI)) {
            return requestAuthMap.get(requestURI);
        }
        //根据request 获取Spring Controller方法
        //获取Spring 控制器方法上的自定义注解
        //根据自定义注解判断是否放行
        for (HandlerMapping handlerMapping : handlerMappingContext.getHandlerMappings().values()) {
            HandlerExecutionChain executionChain;
            try {
                executionChain = handlerMapping.getHandler(httpServletRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (executionChain != null && executionChain.getHandler() instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) executionChain.getHandler();
                // 获取 Controller 方法
                Method method = handlerMethod.getMethod();
                UnAuth unAuth = method.getAnnotation(UnAuth.class);
                boolean isUnAuthMethod = null != unAuth;
                //缓存结果，减少性能开销
                requestAuthMap.put(requestURI, isUnAuthMethod);
                return isUnAuthMethod;
            }

        }
        return false;
    }

}
