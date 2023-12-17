package com.shiro.demo.vo.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class MemberNameGenerateVO implements Serializable {

    private static final long serialVersionUID = 1676822194379249090L;
    /**
     * 中文名称
     */
    @NotBlank(message = "名称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,10}", message = "请输入正确的中文姓名")
    private String chineseName;
}
