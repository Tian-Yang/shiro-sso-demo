package com.shiro.demo.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;


@Getter
@Setter
public class PageResp<T> {

    private List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    private long total = 0;

    /**
     * 每页显示条数，默认 10
     */
    private long size = 10;

    /**
     * 当前页
     */
    private long current = 1;

    public static <T> PageResp<T> parsePage(IPage<T> page) {
        PageResp<T> pagePart = new PageResp<>();
        pagePart.current = page.getCurrent();
        pagePart.total = page.getTotal();
        pagePart.size = page.getSize();
        pagePart.records = page.getRecords();
        return pagePart;
    }

}
