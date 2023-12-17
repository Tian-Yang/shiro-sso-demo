package com.shiro.demo.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.shiro.demo.context.AuthContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 配置自动填充
 */
@Component
public class MybatisMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();

        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);

        this.strictInsertFill(metaObject, "createBy", Long.class, AuthContext.getMemberId());
        this.strictInsertFill(metaObject, "updateBy", Long.class, AuthContext.getMemberId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
        this.strictUpdateFill(metaObject, "updateBy", Long.class, AuthContext.getMemberId());
    }

}
