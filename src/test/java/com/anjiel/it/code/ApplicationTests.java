package com.anjiel.it.code;

import com.anjiel.it.code.config.CodeGeneratorConfig;
import com.anjiel.it.code.entity.Entity;
import com.anjiel.it.code.entity.Field;
import com.anjiel.it.code.service.ITableService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    private static  final  String filedir = ApplicationTests.class.getClassLoader().getResource("templates").getPath();


    @Autowired
    private ITableService tableService;


    @Autowired
    private CodeGeneratorConfig codeGeneratorConfig;



    @Test
    public void contextLoads() throws IOException {



        System.out.println(filedir);

        VelocityEngine velocityEngine = new VelocityEngine();
        Properties properties = new Properties();
        properties.setProperty(velocityEngine.FILE_RESOURCE_LOADER_PATH,filedir);
        properties.setProperty(velocityEngine.ENCODING_DEFAULT,"UTF-8");

        velocityEngine.init(properties);


        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("name","Velocity");
        velocityContext.put("project","Jakarta");

        Template template =velocityEngine.getTemplate("news.vm");
        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext,stringWriter);
        //Velocity.mergeTemplate("news.vm",velocityContext,stringWriter);
        System.out.println("template:"+stringWriter);

        String s = "We are using $project $name to render this.";
        stringWriter=new StringWriter();
        Velocity.evaluate(velocityContext,stringWriter,"myString",s);
        System.out.println("String:"+ stringWriter);

    }






    @Test
    public void findTableBySchameTest(){
        List<Entity> list= tableService.findTableListBySchema("blog","sys_user");

        list.forEach(item-> System.out.println(item.getTableName()+"---->"+item.getTableName()));
    }


    @Test
    public void findColumnByTable(){
        List<Field> listColumn = tableService.findColumnListByTable("blog","sys_user");
        listColumn.forEach(item-> System.out.println(item.getColumnName()+"------>"+item.getComments()));
    }

    @Test
    public void findConfigTest(){
        System.err.println(codeGeneratorConfig.getTarget().getBasePackage());

    }


}
