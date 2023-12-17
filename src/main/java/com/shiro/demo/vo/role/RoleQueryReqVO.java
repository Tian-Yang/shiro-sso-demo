package com.shiro.demo.vo.role;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class RoleQueryReqVO implements Serializable {
    private static final long serialVersionUID = 4479356563785892842L;

    @NotBlank(message = "roleId不能为空")
    private String roleId;
}
