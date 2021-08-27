package com.tanklab.mathless.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

    // http://localhost:8080/swagger-ui.html     原路径
    // http://localhost:8080/doc.html            新路径

    // 配置swagger2 核心配置 docket
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2) // 指定api类型为swagger2
                .apiInfo(apiInfo()) // 用于定义api文档汇总信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tanklab.mathless.controller"))
                .paths(PathSelectors.any()) // 所有controller
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Mathless Api")
                .description("专为 Mathless 平台提供的api文档")
                .version("1.0.0")
                .build();
    }
}
