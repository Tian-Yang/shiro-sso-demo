package com.shiro.demo.bean;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.util.ObjectBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Builder
@Data
public class EsSearchReq<T> {
    /**
     * 要查询的索引名称
     */
    private String indexName;
    /**
     * 分页大小，默认1000
     */
    private Integer size;

    /**
     * 起始行
     */
    private Integer from;
    /**
     * 查询函数
     */
    private Function<Query.Builder, ObjectBuilder<Query>> queryFn;
    /**
     * 聚合Map
     */
    private Map<String, Aggregation> aggsMap = new HashMap<>();

    /**
     * 自动滚动分页ID
     */
    private String scorllId;
}
