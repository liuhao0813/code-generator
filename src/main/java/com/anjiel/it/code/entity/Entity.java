package com.anjiel.it.code.entity;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    private String entityName;
    private String lowerFirstEntityName;
    private String tableName;
    private String tableComments;
    private String pkName;
    private List<Field> displayFields;

    private Field pkField;

    private List<String> importList = new ArrayList<>();

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getLowerFirstEntityName() {
        return lowerFirstEntityName;
    }

    public void setLowerFirstEntityName(String lowerFirstEntityName) {
        this.lowerFirstEntityName = lowerFirstEntityName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComments() {
        return tableComments;
    }

    public void setTableComments(String tableComments) {
        this.tableComments = tableComments;
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

    public Field getPkField() {
        return pkField;
    }

    public void setPkField(Field pkField) {
        this.pkField = pkField;
    }

    public List<String> getImportList() {
        return importList;
    }

    public void setImportList(List<String> importList) {
        this.importList = importList;
    }
}
