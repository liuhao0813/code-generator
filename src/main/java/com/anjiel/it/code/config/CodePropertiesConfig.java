package com.anjiel.it.code.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Code生成目录的配置
 */
@Component
@ConfigurationProperties(prefix = "code.generator")
public class CodePropertiesConfig {


    /**
     * 默认生成的地址
     */
    CodeGeneratorProperties target = new CodeGeneratorProperties();

    public CodeGeneratorProperties getTarget() {
        return target;
    }

    public void setTarget(CodeGeneratorProperties target) {
        this.target = target;
    }
}
