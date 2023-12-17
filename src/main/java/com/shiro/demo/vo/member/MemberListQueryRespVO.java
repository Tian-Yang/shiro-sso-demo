package com.shiro.demo.vo.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

@Data
public class MemberListQueryRespVO implements Serializable {
    private static final long serialVersionUID = -4268855511719170137L;
    /**
     * 用户名
     */
    private String realName;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    /**
     * 更新时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;
}
