package com.anjiel.it.code.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.anjiel.it.code.service.ITableService;
import com.anjiel.it.code.service.impl.MySqlTableService;
import com.anjiel.it.code.service.impl.SqlServerTableService;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.sql.DataSource;

@Configuration
public class CodeGeneratorConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    @ConditionalOnMissingBean
    public ITableService tableService(){
        String driverClassName = "";
        if(dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            driverClassName = hikariDataSource.getDriverClassName();
        }else if(dataSource instanceof DruidDataSource){
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            driverClassName = druidDataSource.getDriverClassName();
        }
        if(StringUtils.contains(driverClassName,"sqlserver")){
            return new SqlServerTableService();
        }else if(StringUtils.contains(driverClassName,"oracle")) {
            return new MySqlTableService();
        } else {
            return new MySqlTableService();
        }
    }


    /**
     * 模版解析器配置,使用text模版解析器
     * @return
     */
    @Bean
    public TemplateEngine codeTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        // Resolver for TEXT emails
        templateEngine.addTemplateResolver(textTemplateResolver());
        //templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

    private ITemplateResolver textTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(1));
        //templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
        String driverClassName = "";
        if(dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            driverClassName = hikariDataSource.getDriverClassName();
        }else if(dataSource instanceof DruidDataSource){
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            driverClassName = druidDataSource.getDriverClassName();
        }
        if(StringUtils.contains(driverClassName,"sqlserver")){
            templateResolver.setPrefix("/sqlserver-code/");
        }else if(StringUtils.contains(driverClassName,"oracle")) {
            templateResolver.setPrefix("/oracle-code/");
        } else {
            templateResolver.setPrefix("/mysql-code/");
        }
        templateResolver.setSuffix(".vm");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding("utf-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }


}
