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



 }
