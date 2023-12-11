package com.shiro.demo.context;

/**
 * 认证上下文
 * Author TianYang
 * Date 2023/12/9 21:06
 */
public class AuthContext {
    private static final ThreadLocal<String> businessDomainCodeThreadLocal = new ThreadLocal<>();

    private static final ThreadLocal<String> appDomainCodeThreadLocal = new ThreadLocal<>();

    private static final ThreadLocal<Long> tenantIdThreadLocal = new ThreadLocal<Long>();


    public static void setBusinessDomainCode(String businessDomainCode) {
        businessDomainCodeThreadLocal.set(businessDomainCode);
    }

    public static String getBusinessDomainCode() {
        return businessDomainCodeThreadLocal.get();
    }

    public static void deleteBusinessDomainCode() {
        businessDomainCodeThreadLocal.remove();
    }

    public static void setAppDomainCode(String appDomainCode) {
        appDomainCodeThreadLocal.set(appDomainCode);
    }

    public static String getAppDomainCode() {
        return appDomainCodeThreadLocal.get();
    }

    public static void deleteAppDomainCode() {
        appDomainCodeThreadLocal.remove();
    }

    public static void setTenantId(long tenantId) {
        tenantIdThreadLocal.set(tenantId);
    }

    public static Long getTenantId() {
        return tenantIdThreadLocal.get();
    }

    public static void deleteTenantId() {
        tenantIdThreadLocal.remove();
    }

    public static void clear() {
        deleteBusinessDomainCode();
        deleteAppDomainCode();
        deleteTenantId();
    }

}
