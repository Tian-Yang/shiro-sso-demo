package com.shiro.demo.vo.tenant;

import lombok.Data;

import java.io.Serializable;

@Data
public class TenantAddRespVO implements Serializable {

    private static final long serialVersionUID = -8616072918640965701L;

    /**
     * 租户ID
     */
    private Long tenantId;
}
