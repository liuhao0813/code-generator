package com.anjiel.it.code.config;

public class CodeGeneratorProperties {


    /**
     * 默认的包名称
     */
    private String basePackage = "com.example.it";
    /**
     * 默认的模块名称
     */
    private String module = "demo";
    /**
     * 默认的数据库名称
     */
    private String targetDatabase = "test";

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTargetDatabase() {
        return targetDatabase;
    }

    public void setTargetDatabase(String targetDatabase) {
        this.targetDatabase = targetDatabase;
    }
}
