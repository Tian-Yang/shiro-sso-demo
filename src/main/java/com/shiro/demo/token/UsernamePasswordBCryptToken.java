package com.shiro.demo.token;

import com.shiro.demo.cryptography.BcryptCredentialFactory;
import com.shiro.demo.entity.MemberInfoEntity;
import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordBCryptToken extends UsernamePasswordToken {

    private String clientHost;


    private String businessDomainCode;

    private MemberInfoEntity memberInfo;


    public UsernamePasswordBCryptToken(final String username, final String password, final boolean rememberMe, final String host, final String clientHost) {
        super(username, password, rememberMe, host);
        this.clientHost = clientHost;
    }

    @Override
    public String getPrincipal() {
        String key = businessDomainCode + "_" + memberInfo.getMemberId();
        Long tenantId = memberInfo.getTenantId();
        if (null != tenantId) {
            key = key + "_" + tenantId;
        }
        return key;
    }


    public Object getCredentials() {
        return new String(getPassword());
    }

    public String getClientHost() {
        return clientHost;
    }

    public String getBusinessDomainCode() {
        return businessDomainCode;
    }

    public void setBusinessDomainCode(String businessDomainCode) {
        this.businessDomainCode = businessDomainCode;
    }

    public MemberInfoEntity getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfoEntity memberInfo) {
        this.memberInfo = memberInfo;
    }
}
