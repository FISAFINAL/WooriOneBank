package com.fisa.woorionebank.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    /**
     * Swagger URL
     * http://localhost:8080/swagger-ui/index.html
     **/
    private final String TITLE = "우리원뱅킹";
    private final String DESCRIPTION = "우리원뱅킹 REST API입니다.";
    private final String VERSION = "V1.0.0";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title(TITLE)
                .description(DESCRIPTION)
                .version(VERSION);
    }
}
