package com.anjiel.it.code.entity;

public class Field {

    private String columnName;
    private String fieldName;
    private String fieldType;
    private String upserFirstFieldName;
    private String columnType;
    private String javaType;
    private String jdbcType;
    private String dataType;
    private String importedClass;
    private String comments;
    private String nullAble;
    private String columnKey;


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getUpserFirstFieldName() {
        return upserFirstFieldName;
    }

    public void setUpserFirstFieldName(String upserFirstFieldName) {
        this.upserFirstFieldName = upserFirstFieldName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getImportedClass() {
        return importedClass;
    }

    public void setImportedClass(String importedClass) {
        this.importedClass = importedClass;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getNullAble() {
        return nullAble;
    }

    public void setNullAble(String nullAble) {
        this.nullAble = nullAble;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
