package com.anjiel.it.code.controller;

import com.anjiel.it.code.common.CodeConstants;
import com.anjiel.it.code.config.CodePropertiesConfig;
import com.anjiel.it.code.entity.Entity;
import com.anjiel.it.code.utils.CodeUtils;
import com.anjiel.it.code.config.CodeGeneratorConfig;
import com.anjiel.it.code.entity.Field;
import com.anjiel.it.code.service.ITableService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping
@CrossOrigin
public class GenaratorCodeController {

    @Autowired
    private TemplateEngine codeTemplateEngine;

    @Autowired
    private ITableService tableService;

    @Autowired
    private CodePropertiesConfig codePropertiesConfig;

    @GetMapping("/generator")
    public String generatorCode(HttpServletResponse response) {

        String schame = codePropertiesConfig.getTarget().getTargetDatabase();
        String basePackage = codePropertiesConfig.getTarget().getBasePackage();
        String module = codePropertiesConfig.getTarget().getModule();

        List<Entity> entityList = tableService.findTableListBySchema(schame, "sys_role");

        for (Entity entity : entityList) {

            final String tableName = entity.getTableName();
            String entityName = CodeUtils.convertEntityName(tableName);
            entity.setEntityName(entityName);
            entity.setLowerFirstEntityName(CodeUtils.convertFieldName(entityName));
            List<Field> fieldList = entity.getDisplayFields();
            for (Field field : fieldList) {
                String fieldName = CodeUtils.convertFieldName(field.getColumnName());
                String dataType = field.getDataType();
                //如果这个字段是主键包含ID则设置为主键
                if (StringUtils.equals("PRI", field.getColumnKey()) || field.getColumnName().endsWith("id")) {

                    entity.setPkName(fieldName);
                    entity.setPkField(field);
                }
                field.setUpserFirstFieldName(CodeUtils.convertEntityName(fieldName));
                field.setFieldName(fieldName);
                field.setFieldType(CodeUtils.covertFieldType(dataType));
                if (CodeConstants.IMPORT_DATA_TYPE_MAP.containsKey(dataType)) {
                    String importType = CodeConstants.IMPORT_DATA_TYPE_MAP.get(dataType);
                    entity.getImportList().add(importType);
                }
                field.setJdbcType(CodeConstants.JDBC_TYPE_MAP.get(dataType));
                //field.setJavaType(CodeConstants.JAVA_TYPE_MAP.get(dataType));
            }

            final Context ctx = new Context(new Locale("en"));
            ctx.setVariable("basePackage", basePackage);
            ctx.setVariable("module", module);
            ctx.setVariable("entity", entity);

            CodeUtils.generatorMapperFile(ctx, codeTemplateEngine);
            CodeUtils.generatorDaoFile(ctx, codeTemplateEngine);
            CodeUtils.generatorVoFile(ctx, codeTemplateEngine);
            CodeUtils.generatorServiceFile(ctx, codeTemplateEngine);
            CodeUtils.generatorServiceImplFile(ctx, codeTemplateEngine);
            CodeUtils.generatorControllerFile(ctx, codeTemplateEngine);
        }

        return "success";
    }


}
