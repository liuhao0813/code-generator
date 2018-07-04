package com.anjiel.it.code;

import com.anjiel.it.code.config.CodePropertiesConfig;
import com.anjiel.it.code.entity.Entity;
import com.anjiel.it.code.service.ITableService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup(){
       mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    private static  final  String filedir = ApplicationTests.class.getClassLoader().getResource("templates").getPath();


    @Autowired
    private ITableService tableService;


    @Autowired
    private CodePropertiesConfig codePropertiesConfig;



    @Test
    public void testGeneratorFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/generator")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

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
    public void findConfigTest(){
        System.err.println(codePropertiesConfig.getTarget().getBasePackage());

    }


}
