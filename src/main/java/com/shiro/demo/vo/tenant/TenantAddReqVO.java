package com.shiro.demo.vo.tenant;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class TenantAddReqVO implements Serializable {

    private static final long serialVersionUID = 4054207457117317754L;

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
    @Pattern(regexp = "^[a-zA-Z\\u4E00-\\u9FA5（）()]{1,200}$", message = "租户名称只能包含中文、英文、中英文括号且最大长度200字符")
    private String tenantName;
}
