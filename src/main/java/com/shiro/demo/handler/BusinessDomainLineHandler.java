package com.shiro.demo.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.shiro.demo.context.AuthContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义租户规则
 * Author TianYang
 * Date 2023/12/9 21:08
 */
public class BusinessDomainLineHandler implements TenantLineHandler {

    static List<String> ignoreTables = new ArrayList<>();

    static {
        ignoreTables.add("business_domain_host");
    }

    @Override
    public Expression getTenantId() {
        return new StringValue(AuthContext.getBusinessDomainCode());
    }

    @Override
    public String getTenantIdColumn() {
        return "business_domain_code";
    }


    @Override
    public boolean ignoreTable(String tableName) {
        //可配置忽略表名
        return ignoreTables.contains(tableName);
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }
}
