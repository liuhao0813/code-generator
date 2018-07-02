package com.anjiel.it.code.entity;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    private String entityName;
    private String tableName;
    private String pkName;
    private List<Field> displayFields;

    private Field columnName;

    private List<String> importList = new ArrayList<>();

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public List<Field> getDisplayFields() {
        return displayFields;
    }

    public void setDisplayFields(List<Field> displayFields) {
        this.displayFields = displayFields;
    }

    public Field getColumnName() {
        return columnName;
    }

    public void setColumnName(Field columnName) {
        this.columnName = columnName;
    }

    public List<String> getImportList() {
        return importList;
    }

    public void setImportList(List<String> importList) {
        this.importList = importList;
    }
}
