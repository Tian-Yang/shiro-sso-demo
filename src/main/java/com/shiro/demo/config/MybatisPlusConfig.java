package com.shiro.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.shiro.demo.handler.BusinessDomainLineHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class MybatisPlusConfig {

    /*@Bean
    public DataSource dataSource(@Qualifier("jdbcConfig") JdbcConfig jdbcConfig) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(jdbcConfig.getUrl());
        dataSource.setUsername(jdbcConfig.getUsername());
        dataSource.setPassword(jdbcConfig.getPassword());
        dataSource.setDriverClassName(jdbcConfig.getDriverClassName());
        dataSource.setDbType(DbType.MYSQL.getDb());
        return dataSource;
    }*/

   /* @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //添加租户功能
        TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor();
        tenantLineInnerInterceptor.setTenantLineHandler(new BusinessDomainLineHandler());
        interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        //添加分页功能
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        sqlSessionFactory.setPlugins(interceptor);

        return sqlSessionFactory.getObject();
    }*/

    /**
     * 整合分页插件
     * @return
     */
    @Bean
    @InterceptorIgnore(tenantLine = "true")
    public PaginationInnerInterceptor paginationInterceptor() {
        PaginationInnerInterceptor interceptor = new PaginationInnerInterceptor();
        // 设置分页插件的一些属性，如下为默认值
        interceptor.setMaxLimit(500L); // 单页分页条数限制，默认 500 条，设置为 -1 不限制
        interceptor.setOverflow(true); // 单页分页条数超过限制是否调整为默认分页条数，默认 false
        interceptor.setDbType(DbType.MYSQL);
        return interceptor;
    }

    /**
     * 整合多租户插件
     * @return
     */
    @Bean
    public BusinessDomainLineHandler businessDomainLineHandler(){
        BusinessDomainLineHandler businessDomainLineHandler = new BusinessDomainLineHandler();
        return businessDomainLineHandler;
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
