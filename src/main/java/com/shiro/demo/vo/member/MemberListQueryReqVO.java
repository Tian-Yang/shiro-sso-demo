package com.shiro.demo.vo.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class MemberListQueryReqVO extends Page implements Serializable {
    private static final long serialVersionUID = -5460901350621242584L;
    /**
     * 用户姓名
     */
    @Pattern(regexp = "^[\\u4e00-\\u9fa5·]{1,30}$", message = "请输入正确的中文名，最大长度30字符")
    private String realName;
    /**
     * 用户状态
     * 0:禁用
     * 1:启用
     */
    @Pattern(regexp = "^(0|1)$", message = "未识别的用户状态")
    private String status;
}
