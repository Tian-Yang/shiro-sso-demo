package com.shiro.demo.vo.member;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberAddRespVO implements Serializable {
    private static final long serialVersionUID = 8083698891582587628L;
    private Long memberId;
}
