package com.shiro.demo.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TestReq {

    private LocalDateTime dateTime;

    private Integer num;

    private String str;

    private BigDecimal number;
}
