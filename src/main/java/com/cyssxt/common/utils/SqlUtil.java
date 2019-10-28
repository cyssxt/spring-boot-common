package com.cyssxt.common.utils;

import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlUtil {


    public static String getSql(String tableName, List<String> fields, List<Object> values){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format("insert into %s ",tableName));
        stringBuffer.append("(");
        stringBuffer.append(String.join(",",fields));
        stringBuffer.append(") values ");
        List<String> tmp = new ArrayList<>();
        for(int i=0;i<values.size();i++){
            tmp.add("?");
        }
        stringBuffer.append("(");
        stringBuffer.append(String.join(",",tmp));
        stringBuffer.append(")");
        String sql = stringBuffer.toString();
        return sql;

    }

    public static String getUpdateQuery(String tableName, List<String> fields,List<String> updateFields){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format("update %s set ",tableName));
        List<String> updates = new ArrayList<>();
        for(int i=0;i<updateFields.size();i++){
            String updateField = updateFields.get(i);
            updates.add(String.format("%s=?",updateField));
        }
        stringBuffer.append(String.join(",",updates));
        stringBuffer.append(" where ");
        List<String> wheres = new ArrayList<>();
        for(int i=0;i<fields.size();i++){
            String field = fields.get(i);
            wheres.add(String.format("%s=?",field));
        }
        stringBuffer.append(String.join(" and ",wheres));
        return stringBuffer.toString();

    }

    public static String getQuerySql(String tableName, List<String> fields){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format("select * from %s ",tableName));
        stringBuffer.append(" where ");
        for(int i=0;i<fields.size();i++){
            String field = fields.get(i);
            stringBuffer.append(String.format(" %s=?",field));
            if(i<fields.size()-1){
                stringBuffer.append(" and ");
            }
        }
        return stringBuffer.toString();

    }

    public static Map<String,Integer> getFieldTypes(String tableName, DataSource dataSource){
        String sql = String.format("select * from %s",tableName);
        Connection connection=null;
        PreparedStatement ps=null;
        Map<String, Integer> result = new HashMap<>();
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String columnName = rsmd.getColumnName(i);
                Integer columnType = rsmd.getColumnType(i);
                result.put(columnName.toUpperCase(),columnType);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(ps!=null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
