package com.shiro.demo.vo.role;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 菜单名称
 */
@Data
public class RoleListQueryReqVO extends Page{

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 橘色状态：0 禁用，1启用
     */
    @Pattern(regexp = "^(0|1)$", message = "未识别的角色状态")
    private String roleStatus;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String upateTime;
}
