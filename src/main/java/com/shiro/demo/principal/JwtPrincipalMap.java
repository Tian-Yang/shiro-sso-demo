package com.shiro.demo.principal;

import com.shiro.demo.bean.MemberInfo;
import com.shiro.demo.constants.JwtTokenConstant;
import org.apache.shiro.subject.SimplePrincipalMap;

public class JwtPrincipalMap extends SimplePrincipalMap {
    private static final long serialVersionUID = 2461873285984188888L;

    public String getJwtToken() {
        return (String) super.ensureCombinedPrincipals().get("token");
    }


    public long getTokenExpiredTime() {
        return (long) super.ensureCombinedPrincipals().get(JwtTokenConstant.TOKEN_EXPIRED_TIME);
    }


    /**
     * 重写getPrimaryPrincipal方法，获取认证主体唯一标识，例如：memberId
     *
     * @return java.lang.Object
     * @Author TianYang
     * @Date 2023/11/17 15:06
     */
    @Override
    public Object getPrimaryPrincipal() {
        return super.ensureCombinedPrincipals().get(JwtTokenConstant.PRIMARY_PRINCIPAL_KEY);
    }


    public void setErrorMsg(String errorMsg) {
        super.ensureCombinedPrincipals().put(JwtTokenConstant.AUTH_ERROR_MESSAGE, errorMsg);
    }

    public String getErrorMsg() {
        Object error = super.ensureCombinedPrincipals().get(JwtTokenConstant.AUTH_ERROR_MESSAGE);
        return null == error ? "" : (String) error;
    }

    public MemberInfo getMemberInfo(){
        return (MemberInfo) super.ensureCombinedPrincipals().get(JwtTokenConstant.MEMBER_INFO);
    }
}
