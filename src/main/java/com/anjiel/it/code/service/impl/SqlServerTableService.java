package com.anjiel.it.code.service.impl;

import com.anjiel.it.code.entity.Entity;
import com.anjiel.it.code.entity.Field;
import com.anjiel.it.code.service.ITableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.util.List;

/**
 * 查询mysqlTable表信息
 *
 * @author liuhao
 */
public class SqlServerTableService implements ITableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据数据名，表名查询表信息（包括字段信息）
     * @param scheme  数据库名称
     * @param tableName
     * @return
     */
    @Override
    public List<Entity> findTableListBySchema(String scheme,String tableName) {


        //String sql = "SELECT TABLE_SCHEMA,TABLE_NAME,TABLE_COMMENT FROM `TABLES` where TABLE_SCHEMA=? and table_name = ?";
        String sql = "SELECT o.id, o.name TABLE_NAME, CONVERT(varchar(100), p.value) TABLE_COMMENT,o.crdate createTime,o.xtype engine\n" +
                "FROM sysobjects o LEFT JOIN sys.extended_properties p on o.id=p.major_id and o.info = p.minor_id\n" +
                "WHERE o.xtype='U' AND o.name = ?";
        List<Entity> entityList = jdbcTemplate.query(sql, preparedStatement -> {
            preparedStatement.setString(1, tableName);}
                ,
                (resultSet, i) -> {
            Entity entity = new Entity();
            entity.setTableName(resultSet.getString(2));
            entity.setTableComments(resultSet.getString(3));
            //查询字段详情，构建VO和Mapper.xml时使用
            List<Field> fieldList = findColumnListByTable(scheme,tableName);
            entity.setDisplayFields(fieldList);
            return entity;
        });

        return entityList;
    }

    private List<Field> findColumnListByTable(String schame, String table) {

        /*String sql="SELECT COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,DATA_TYPE,COLUMN_KEY,COLUMN_COMMENT " +
                    "FROM `COLUMNS` " +
                    "WHERE TABLE_SCHEMA=? AND TABLE_NAME=?";*/

        String sql="SELECT c.name COLUMN_NAME, t.name COLUMN_TYPE, t.name DATA_TYPE,CONVERT(varchar(100), p.value) COLUMN_COMMENT \n" +
        "FROM syscolumns c LEFT JOIN systypes t on c.xtype = t.xusertype LEFT JOIN sysobjects o on o.id=c.id LEFT JOIN sys.extended_properties p ON c.id=p.major_id AND c.colid=p.minor_id\n" +
        "where o.name=?";

        return jdbcTemplate.query(sql, (PreparedStatementSetter) preparedStatement -> {
            preparedStatement.setString(1, table);
        }, (resultSet, i) -> {
            Field field =new Field();
            field.setColumnName(resultSet.getString(1));
            field.setColumnType(resultSet.getString(2));
            field.setDataType(resultSet.getString(3));
            field.setComments(resultSet.getString(4));
            return field;
        });
    }

}
