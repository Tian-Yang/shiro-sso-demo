package com.shiro.demo.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期类型处理器
 * Author TianYang
 * Date 2023/12/14 16:03
 */
@Component
public class DateStringTypeHandler implements TypeHandler<String> {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, s);
    }

    @Override
    public String getResult(ResultSet resultSet, String s) throws SQLException {
        Date date = resultSet.getTimestamp(s);
        return (date != null) ? sdf.format(date) : null;
    }

    @Override
    public String getResult(ResultSet resultSet, int i) throws SQLException {
        Date date = resultSet.getTimestamp(i);
        return (date != null) ? sdf.format(date) : null;
    }

    @Override
    public String getResult(CallableStatement callableStatement, int i) throws SQLException {
        Date date = callableStatement.getTimestamp(i);
        return (date != null) ? sdf.format(date) : null;
    }
}