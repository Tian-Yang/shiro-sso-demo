package com.shiro.demo.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.shiro.demo.context.AuthContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
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
        ignoreTables.add("menu_info");

    }

    @Override
    public Expression getTenantId() {
        LongValue tenantId = null;
        Long tenantIdValue = AuthContext.getTenantId();
        if (null != tenantIdValue) {
            tenantId = new LongValue(tenantIdValue);
        }
        return tenantId;
    }

    @Override
    public String getTenantIdColumn() {
        return "tenant_id";
    }


    @Override
    public boolean ignoreTable(String tableName) {

        if (null == getTenantId()) {
            return true;
        }
        //可配置忽略表名
        return ignoreTables.contains(tableName);
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }
}
