package com.shiro.demo.config;

import com.shiro.demo.authenticat.BearerTokenCredentialsMatcher;
import com.shiro.demo.authenticat.PasswordBCryptCredentialsMatcher;
import com.shiro.demo.cache.AuthRedisCacheManager;
import com.shiro.demo.filter.BasicUsernamePasswordLoginFilter;
import com.shiro.demo.filter.BearerJwtTokenFilter;
import com.shiro.demo.relam.BearerJwtTokenRealm;
import com.shiro.demo.relam.UsernamePasswordRealm;
import com.shiro.demo.service.RedisService;
import com.shiro.demo.strategy.CustomizeFirstSuccessfulStrategy;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.config.ShiroAnnotationProcessorConfiguration;
import org.apache.shiro.spring.config.ShiroBeanConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroRequestMappingConfig;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * ShiroBeanConfiguration:配置Shiro的生命周期和事件
 * ShiroAnnotationProcessorConfiguration:启用Shiro的注释处理
 * ShiroWebConfiguration:为网络使用配置Shiro Beans（SecurityManager、SessionManager等）
 * ShiroWebFilterConfiguration:配置Shiro的网络过滤器
 * ShiroRequestMappingConfig:使用Shiro的UrlPathHelper实现配置Spring，以确保URL处理两个框架相同
 */

@Configuration
@Import({ShiroBeanConfiguration.class,
        ShiroAnnotationProcessorConfiguration.class,
        ShiroRequestMappingConfig.class})
public class ShiroConfig {

    @Bean
    public AuthRedisCacheManager authRedisCacheManager(@Qualifier("redisService") RedisService redisService) {
        AuthRedisCacheManager authRedisCacheManager = new AuthRedisCacheManager(redisService);
        return authRedisCacheManager;
    }

    /**
     * 自定义Realm
     *
     * @param authRedisCacheManager
     * @return
     */
    @Bean
    @Primary
    public Realm usernamePasswordRealm(@Qualifier("authRedisCacheManager") AuthRedisCacheManager authRedisCacheManager) {
        UsernamePasswordRealm jwtRealm = new UsernamePasswordRealm();
        //开启认证缓存
        jwtRealm.setAuthenticationCachingEnabled(true);
        //开启授权缓存
        jwtRealm.setAuthorizationCachingEnabled(true);
        //设置cacheManager
        jwtRealm.setCacheManager(authRedisCacheManager);
        //设置认证缓存
        jwtRealm.setAuthenticationCache(authRedisCacheManager.getCache("sharedAuthenticationCache"));
        //设置授权缓存
        jwtRealm.setAuthorizationCache(authRedisCacheManager.getCache("sharedAuthorizationCache"));
        //设置自定义凭证验证
        jwtRealm.setCredentialsMatcher(new PasswordBCryptCredentialsMatcher());
        return jwtRealm;
    }

    /**
     * 自定义Http Bearer协议Jwt Token处理Realm
     *
     * @param authRedisCacheManager
     * @return org.apache.shiro.realm.Realm
     * @Author TianYang
     * @Date 2023/11/15 17:55
     */
    @Bean
    @Primary
    public BearerJwtTokenRealm bearerJwtTokenRealm(@Qualifier("authRedisCacheManager") AuthRedisCacheManager authRedisCacheManager) {
        BearerJwtTokenRealm bearerJwtTokenRealm = new BearerJwtTokenRealm();
        bearerJwtTokenRealm.setCredentialsMatcher(new BearerTokenCredentialsMatcher());
        bearerJwtTokenRealm.setAuthenticationCachingEnabled(true);
        bearerJwtTokenRealm.setAuthorizationCachingEnabled(true);
        bearerJwtTokenRealm.setCacheManager(authRedisCacheManager);
        //设置认证缓存
        bearerJwtTokenRealm.setAuthenticationCache(authRedisCacheManager.getCache("sharedAuthenticationCache"));
        //设置授权缓存
        bearerJwtTokenRealm.setAuthorizationCache(authRedisCacheManager.getCache("sharedAuthorizationCache"));
        return bearerJwtTokenRealm;
    }

    /**
     * 定义过滤器链
     *
     * @return org.apache.shiro.spring.web.config.ShiroFilterChainDefinition
     * @Author TianYang
     * @Date 2023/11/15 17:56
     */
    @Bean
    @Primary
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        //不拦截登录请求。注意：anon必须放在authc之前否则不生效

        chainDefinition.addPathDefinition("/testEs", "anon");

        chainDefinition.addPathDefinition("/testReq", "anon");

        //登录请求走http basic协议
        chainDefinition.addPathDefinition("/login", "authcBasic");

        // logged in users with the 'admin' role
        chainDefinition.addPathDefinition("/admin/**", "authcBearer, roles[admin]");

        // logged in users with the 'document:read' permission
        chainDefinition.addPathDefinition("/docs/**", "authcBearer, perms[document:read]");

        //普通请求走http bearer协议
        chainDefinition.addPathDefinition("/**", "authcBearer");

        return chainDefinition;
    }

    /**
     * 配置Session会话
     *
     * @return org.apache.shiro.web.session.mgt.DefaultWebSessionManager
     * @Author TianYang
     * @Date 2023/11/17 10:41
     */
    @Bean
    @Primary
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        //禁用会话cookie，防止在登录生成Token后，后续请求不需要Token校验 直接可以通过认证。
        defaultWebSessionManager.setSessionIdCookieEnabled(false);
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(false);
        return defaultWebSessionManager;
    }

    /**
     * 自定义securityManager
     *
     * @param usernamePasswordRealm
     * @param bearerJwtTokenRealm
     * @return org.apache.shiro.web.mgt.DefaultWebSecurityManager
     * @Author TianYang
     * @Date 2023/11/15 17:56
     */
    @Bean(name = "securityManager")
    @Primary
    public DefaultSecurityManager securityManager(@Qualifier("usernamePasswordRealm") Realm usernamePasswordRealm, @Qualifier("bearerJwtTokenRealm") Realm bearerJwtTokenRealm, @Qualifier("defaultWebSessionManager") DefaultWebSessionManager defaultWebSessionManager, @Qualifier("authenticator") Authenticator authenticator, @Qualifier("authorizer") Authorizer authorizer) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置认证组件
        securityManager.setAuthenticator(authenticator);
        //设置Realms
        securityManager.setRealms(CollectionUtils.asList(usernamePasswordRealm, bearerJwtTokenRealm));
        //设置会话管理器
        securityManager.setSessionManager(defaultWebSessionManager);
        //设置授权组件
        securityManager.setAuthorizer(authorizer);
        SecurityUtils.setSecurityManager(securityManager);

        return securityManager;
    }

    @Bean
    @Primary
    public Authenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new CustomizeFirstSuccessfulStrategy());
        return authenticator;
    }

    /**
     * 配置自定义认证处理器
     *
     * @param bearerJwtTokenRealm
     * @return org.apache.shiro.authz.Authorizer
     * @Author TianYang
     * @Date 2023/11/21 11:12
     */
    @Bean
    @Primary
    public Authorizer authorizer(@Qualifier("bearerJwtTokenRealm") BearerJwtTokenRealm bearerJwtTokenRealm) {
        return bearerJwtTokenRealm;
    }


    /**
     * 自定义过滤器
     *
     * @param securityManager
     * @param shiroFilterChainDefinition
     * @return
     */
    @Bean
    @Primary
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultSecurityManager securityManager, ShiroFilterChainDefinition shiroFilterChainDefinition, @Qualifier("bearerJwtTokenFilter") BearerJwtTokenFilter bearerJwtTokenFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = new HashMap<>();
        //自定义Http Authorization:Bearer <token> 过滤器
        filters.put("authcBearer", bearerJwtTokenFilter);
        //自定义Http Authorization:Basic <Base64(userName:password)> 过滤器
        filters.put("authcBasic", new BasicUsernamePasswordLoginFilter());
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());
        return shiroFilterFactoryBean;

    }

    @Bean
    @Primary
    public BearerJwtTokenFilter bearerJwtTokenFilter() {
        return new BearerJwtTokenFilter();
    }

    @ModelAttribute(name = "subject")
    public Subject subject() {
        return SecurityUtils.getSubject();
    }
}
