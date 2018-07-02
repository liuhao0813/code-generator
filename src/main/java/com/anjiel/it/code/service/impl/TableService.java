package com.anjiel.it.code.service.impl;

import com.anjiel.it.code.entity.Entity;
import com.anjiel.it.code.entity.Field;
import com.anjiel.it.code.service.ITableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService implements ITableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Entity> findTableListBySchema(String scheme,String tableName) {

        String sql = "SELECT TABLE_SCHEMA,TABLE_NAME,TABLE_COMMENT FROM `TABLES` where TABLE_SCHEMA=? and table_name = ?";
        List<Entity> entityList = jdbcTemplate.query(sql, preparedStatement -> {
            preparedStatement.setString(1, scheme);
            preparedStatement.setString(2, tableName);}
                ,
                (resultSet, i) -> {

            Entity entity = new Entity();
            entity.setTableName(resultSet.getString(2));
            List<Field> fieldList = findColumnListByTable(scheme,tableName);
            entity.setDisplayFields(fieldList);
            return entity;
        });

        return entityList;
    }

    @Override
    public List<Field> findColumnListByTable(String schame, String table) {

        String sql="SELECT COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,DATA_TYPE,COLUMN_KEY,COLUMN_COMMENT " +
                    "FROM `COLUMNS` " +
                    "WHERE TABLE_SCHEMA=? AND TABLE_NAME=?";

        return jdbcTemplate.query(sql, (PreparedStatementSetter) preparedStatement -> {
            preparedStatement.setString(1, schame);
            preparedStatement.setString(2, table);
        }, (resultSet, i) -> {
            Field field =new Field();
            field.setColumnName(resultSet.getString(1));
            field.setColumnType(resultSet.getString(2));
            field.setNullAble(resultSet.getString(3));
            field.setDataType(resultSet.getString(4));
            field.setNullAble(resultSet.getString(5));
            field.setComments(resultSet.getString(6));
            return field;
        });
    }

}
