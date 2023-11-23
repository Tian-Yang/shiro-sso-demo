package com.shiro.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
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

    /*
        @Bean(name = "globalConfig")
        public GlobalConfig globalConfig(@Qualifier("myBatisPlusMetaObjectHandler") MyBatisPlusMetaObjectHandler metaObjectHandler) {
            GlobalConfig globalConfig = new GlobalConfig();
            globalConfig.setMetaObjectHandler(metaObjectHandler);
            return globalConfig;
        }
    */
    @Bean
    public DataSource dataSource(@Qualifier("jdbcConfig") JdbcConfig jdbcConfig) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(jdbcConfig.getUrl());
        dataSource.setUsername(jdbcConfig.getUsername());
        dataSource.setPassword(jdbcConfig.getPassword());
        dataSource.setDriverClassName(jdbcConfig.getDriverClassName());
        dataSource.setDbType(DbType.MYSQL.getDb());
        return dataSource;
    }

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);


        //添加分页功能================================================================================================
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        sqlSessionFactory.setPlugins(interceptor);

        return sqlSessionFactory.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
