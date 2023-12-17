package com.shiro.demo.vo.tenant;

import lombok.Data;
import org.apache.ibatis.ognl.OgnlRuntime;

import java.io.Serializable;

@Data
public class TenantAddRespVO implements Serializable {

    private static final long serialVersionUID = -8616072918640965701L;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 超级管理员memberId
     */
    private Long memberId;

    /**
     * 超级管理员账号名称
     */
    private String accountName;
}
