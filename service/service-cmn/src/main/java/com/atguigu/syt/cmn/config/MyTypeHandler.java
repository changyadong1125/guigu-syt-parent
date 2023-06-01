package com.atguigu.syt.cmn.config;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn.config
 * class:n
 *
 * @author: smile
 * @create: 2023/5/31-22:54
 * @Version: v1.0
 * @Description:
 */
@MappedTypes(String.class)
@MappedJdbcTypes(JdbcType.BIGINT)
public class MyTypeHandler implements TypeHandler<String> {


    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        long id = rs.getLong("id");
        return "parent-"+id;
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
