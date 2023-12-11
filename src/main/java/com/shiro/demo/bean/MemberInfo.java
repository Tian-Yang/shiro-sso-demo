package com.shiro.demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shiro.demo.enums.MemberStatusEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员信息
 * Author TianYang
 * Date 2023/11/15 14:05
 */
@Data
public class MemberInfo implements Serializable {
    private static final long serialVersionUID = 4376316309558854223L;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 会员名称
     */
    private String name;

    /**
     * 业务域编码
     */
    private String businessDomainCode;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 用户类型
     *
     * @see com.shiro.demo.enums.MemberIdentityTypeEnum
     */
    private String identityType;

    /**
     * 用户状态
     *
     * @see MemberStatusEnum
     */
    private int status;

    @JsonIgnore
    private String memberAuthCacheKey;

    public String getMemberAuthCacheKey() {
        String key = businessDomainCode + "_" + memberId;
        if (null != tenantId) {
            key = key + "_" + tenantId;
        }
        return key;
    }
}
