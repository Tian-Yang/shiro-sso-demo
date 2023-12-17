package com.shiro.demo.vo.menu;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class MenuQueryVO {
    @Pattern(regexp = "^(left|top|right|button)$", message = "未识别的M菜单类型")
    private String menuPosition;
}
