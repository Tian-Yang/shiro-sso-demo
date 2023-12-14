package com.shiro.demo.context;

/**
 * 认证上下文
 * Author TianYang
 * Date 2023/12/9 21:06
 */
public class AuthContext {

    /**
     * 业务域编码
     */
    private static final ThreadLocal<String> businessDomainCodeThreadLocal = new ThreadLocal<>();

    /**
     * 系统域编码
     */
    private static final ThreadLocal<String> appDomainCodeThreadLocal = new ThreadLocal<>();

    /**
     * 租户ID
     */
    private static final ThreadLocal<Long> tenantIdThreadLocal = new ThreadLocal<Long>();

    /**
     * 会员ID
     */
    private static final ThreadLocal<Long> memberIdThreadLocal = new ThreadLocal<>();


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

    public static void setTenantId(Long tenantId) {
        if(null!=tenantId){
            tenantIdThreadLocal.set(tenantId);
        }
    }

    public static Long getTenantId() {
        return tenantIdThreadLocal.get();
    }

    public static void deleteTenantId() {
        tenantIdThreadLocal.remove();
    }

    public static void setMemberId(Long memberId) {
        memberIdThreadLocal.set(memberId);
    }

    public static Long getMemberId() {
        return memberIdThreadLocal.get();
    }

    public static void deleteMemberId() {
        memberIdThreadLocal.remove();
    }

    public static void clear() {
        deleteBusinessDomainCode();
        deleteAppDomainCode();
        deleteTenantId();
        deleteMemberId();
    }

}
