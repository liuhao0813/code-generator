package com.anjiel.it.code.service;

import com.anjiel.it.code.entity.Entity;
import com.anjiel.it.code.entity.Field;

import java.util.List;


/**
 * 相关服务接口
 */
public interface ITableService {


    /**
     * 根据对应的schema对象查询对应的表集合
     * @param scheme  数据库名称
     * @return
     */
    List<Entity> findTableListBySchema(String scheme, String tableName);


    /**
     * 根据schema和表名称取得对应的列信息
     * @param schema   数据库名称
     * @param table    表名称
     * @return
     */
    List<Field> findColumnListByTable(String schema, String table);

 }
