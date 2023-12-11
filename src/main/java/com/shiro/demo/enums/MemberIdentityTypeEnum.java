package com.shiro.demo.enums;

/**
 * 用户身份类型
 * Author TianYang
 * Date 2023/12/10 19:10
 */
public enum MemberIdentityTypeEnum {
    SAAS_SUPER_ADMIN("saas_super_admin", "Saas超管"),
    TENANT_SUPER_ADMIN("tenant_super_admin", "租户超管"),
    TENANT_ADMIN("tenant_admin", "租户管理员"),
    TENANT_USER("tenant_user", "租户内用户"),
    UNBOUND_IDENTITY("unbound_identity", "未绑定身份");

    private String code;

    private String desc;

    MemberIdentityTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
