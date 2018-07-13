package com.anjiel.it.code.common;

import java.util.HashMap;
import java.util.Map;


/**
 * 常量类
 *
 *
 * @author liuhao
 */
public class CodeConstants {


    /**
     * 数据库中的dataType字段和JavaVO中字段类型的映射
     */
    public static final Map<String, String> JAVA_TYPE_MAP = new HashMap<String, String>() {{
        put("varchar", "String");
        put("int", "Integer");
        put("tinyint", "Integer");
        put("date", "LocalDate");
    }};

    /**
     * 数据库中的dataType字段和Mytatis中的映射文件中的jdbcType进行映射
     */
    public static final Map<String, String> JDBC_TYPE_MAP = new HashMap<String, String>() {{
        put("varchar", "VARCHAR");
        put("int", "INTEGER");
        put("tinyint", "INTEGER");
        put("date", "DATE");
    }};


    /**
     * 数据库中的dataTypa字段和JAVA中的类型进行映射，主要是在生成java对象的时候，导包用
     */
    public static final Map<String, String> IMPORT_DATA_TYPE_MAP = new HashMap<String, String>() {{
        put("date", "java.time.LocalDate");
    }};
}
