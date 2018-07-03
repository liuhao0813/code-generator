package com.anjiel.it.code.utils;


import com.anjiel.it.code.entity.Entity;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.anjiel.it.code.common.CodeConstants.JAVA_TYPE_MAP;


/**
 * 常用的工具类
 */
public class CodeUtils {


    /**
     * 将表名转换称实体名称
     *
     * @param tableName
     * @return
     */
    public static String convertEntityName(String tableName) {
        String className = "";
        String[] tempStr = tableName.split("_");
        for (String str : tempStr) {
            className += str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
        }
        return className;
    }

    /**
     * 将表字段转换称实体属性名称
     *
     * @param cloumnName 表列名
     * @return
     */
    public static String convertFieldName(String cloumnName) {
        String filed = convertEntityName(cloumnName);
        String fileNames = filed.substring(0, 1).toLowerCase() + filed.substring(1, filed.length());
        return fileNames;
    }


    /**
     * 获取代码的生成跟路径  默认是当前项目路径下
     *
     * @return
     */
    public static String projectRootDir() {
        String projectRootDir = System.getProperty("user.dir");
        return projectRootDir;
    }


    /**
     * 获得文件的生成路径，如果路径不存在，就创建，否则就直接返回对应的路径
     *
     * @param basePackage
     * @param module
     * @param packageType
     * @return
     */
    public static String getFileGeneratorPath(String basePackage, String module, String packageType) {

        String mapperFilePath = projectRootDir() + "/" + basePackage.replace(".", "/") + "/" + module + "/" + packageType + "/";
        File file = new File(mapperFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return mapperFilePath;
    }


    /**
     * 在指定的路径下生成对应的文件
     *
     * @param mapperContent  文件内容
     * @param mapperfilePath 指定的路径
     * @param fileName       指定的文件名
     * @throws IOException
     */
    public static void generatorFile(String mapperContent, String mapperfilePath, String fileName) {
        File file = new File(mapperfilePath + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(mapperContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据数据库中的dataType获取指定的实体字段类型
     *
     * @param dataType
     * @return
     */
    public static String covertFieldType(String dataType) {
        if (JAVA_TYPE_MAP.containsKey(dataType)) {
            return JAVA_TYPE_MAP.get(dataType);
        }
        return "";
    }

    /**
     * 生成Mybatis映射文件
     * @param ctx
     * @param templateEngine
     * @throws IOException
     */
    public static void generatorMapperFile(Context ctx, TemplateEngine templateEngine) {

        String basePackage = (String) ctx.getVariable("basePackage");
        String module  = (String) ctx.getVariable("module");
        Entity entity = (Entity) ctx.getVariable("entity");

        String mapperFilePath = getFileGeneratorPath(basePackage, module, "mapper");
        String mapperContent = templateEngine.process("IxxMapper", ctx);

        generatorFile(mapperContent,mapperFilePath,"I"+entity.getEntityName()+"Mapper.xml");

    }

    /**
     * 生成MybatisDao文件
     * @param ctx
     * @param templateEngine
     * @throws IOException
     */
    public static void generatorDaoFile(Context ctx, TemplateEngine templateEngine) {

        String basePackage = (String) ctx.getVariable("basePackage");
        String module  = (String) ctx.getVariable("module");
        Entity entity = (Entity) ctx.getVariable("entity");

        String mapperFilePath = getFileGeneratorPath(basePackage, module, "mapper");
        String mapperContent = templateEngine.process("IxxDao", ctx);

        generatorFile(mapperContent,mapperFilePath,"I"+entity.getEntityName()+"Mapper.java");

    }

    public static void generatorVoFile(Context ctx, TemplateEngine emailTemplateEngine) {
        String basePackage = (String) ctx.getVariable("basePackage");
        String module  = (String) ctx.getVariable("module");
        Entity entity = (Entity) ctx.getVariable("entity");

        String mapperFilePath = getFileGeneratorPath(basePackage, module, "entity");
        String mapperContent = emailTemplateEngine.process("xxVO", ctx);

        generatorFile(mapperContent,mapperFilePath,entity.getEntityName()+"VO.java");
    }

    public static void generatorServiceFile(Context ctx, TemplateEngine emailTemplateEngine) {
        String basePackage = (String) ctx.getVariable("basePackage");
        String module  = (String) ctx.getVariable("module");
        Entity entity = (Entity) ctx.getVariable("entity");

        String mapperFilePath = getFileGeneratorPath(basePackage, module, "service");
        String mapperContent = emailTemplateEngine.process("IxxService", ctx);

        generatorFile(mapperContent,mapperFilePath,"I"+entity.getEntityName()+"Service.java");
    }

    public static void generatorServiceImplFile(Context ctx, TemplateEngine emailTemplateEngine) {
        String basePackage = (String) ctx.getVariable("basePackage");
        String module  = (String) ctx.getVariable("module");
        Entity entity = (Entity) ctx.getVariable("entity");

        String mapperFilePath = getFileGeneratorPath(basePackage, module, "service/impl");
        String mapperContent = emailTemplateEngine.process("xxService", ctx);

        generatorFile(mapperContent,mapperFilePath,entity.getEntityName()+"Service.java");
    }
}
