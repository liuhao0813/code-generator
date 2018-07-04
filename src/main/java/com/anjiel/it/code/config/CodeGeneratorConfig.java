package com.anjiel.it.code.config;

import com.anjiel.it.code.service.ITableService;
import com.anjiel.it.code.service.impl.MySqlTableService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class CodeGeneratorConfig {


    @Bean
    @ConditionalOnMissingBean
    public ITableService tableService(){
        return new MySqlTableService();
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
        templateResolver.setPrefix("/code/");
        templateResolver.setSuffix(".vm");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding("utf-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }


}
