package com.codingcat.securityone.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 뷰리졸버 설정
    @Override
    public void configureViewResolvers(
            ViewResolverRegistry registry
    ) {

        // 머스테치 리졸버 설정
        MustacheViewResolver resolver = new MustacheViewResolver();
        resolver.setCharset("UTF-8");
        resolver.setContentType("text/html;charset=UTF-8");
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html"); // .html을 만들어도 머스테치라고 인식함
        registry.viewResolver(resolver);
    }
}
