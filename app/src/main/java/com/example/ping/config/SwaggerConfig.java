package com.example.ping.config;

import com.example.ping.config.properties.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    @Autowired
    public Docket api(SwaggerProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .build())
            .forCodeGeneration(true)
            .select()
            .paths(PathSelectors.regex(properties.getInclude()))
            .build();
    }

}
