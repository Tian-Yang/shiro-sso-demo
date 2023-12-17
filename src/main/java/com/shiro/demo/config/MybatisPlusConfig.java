package com.shiro.demo.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.shiro.demo.handler.BusinessDomainLineHandler;
import com.shiro.demo.interceptor.MyTenantLineInnerInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MybatisPlusConfig {


    /**
     * MyBatis 插件整合
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(@Qualifier("businessDomainLineHandler")BusinessDomainLineHandler businessDomainLineHandler) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        //添加租户插件
        MyTenantLineInnerInterceptor tenantLineInnerInterceptor = new MyTenantLineInnerInterceptor();
        tenantLineInnerInterceptor.setTenantLineHandler(businessDomainLineHandler);
        interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        //多个插件使用的情况，请将分页插件放到 插件执行链 最后面。如在租户插件前面，会出现 COUNT 执行 SQL 不准确问题
        //添加分页插件
        paginationInnerInterceptor.setMaxLimit(10L); // 单页分页条数限制，默认 10 条，设置为 -1 不限制
        paginationInnerInterceptor.setOverflow(false); // 单页分页条数超过限制是否调整为默认分页条数，默认 false
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);//如果配置多个插件,切记分页最后添加
        return interceptor;
    }


    @Bean
    public BusinessDomainLineHandler businessDomainLineHandler(){
        return new BusinessDomainLineHandler();
    }

   /* @Bean
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }*/

   /* @Bean
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
*/
   /* @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }  */
}
