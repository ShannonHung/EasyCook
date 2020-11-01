package com.seminar.easyCookWeb.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                //this returns an instance of the API selector builder
                //which essentially controls the endpoints exposed by Swagger
                .select()
                //for the request handlers and the past selectors for any
                .apis(RequestHandlerSelectors.any())
                //documentation available for the entire locations API
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }
    //Swagger provide some default values in its reponse that you can customize here
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "EasyCook的API",
                "This API returns a list of Cars.",
                "1.0",
                "http://www.easycook.com/",
                new Contact("ShannonHung", "www.easycook.com", "b10709036@mail.ntust.edu.tw"),
                "License of API", "http://www.easycook.com/license", Collections.emptyList());
    }

}
