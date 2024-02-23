package com.tech.ada.java.lojadeva.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI lojaDevaOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Loja Deva API")
                        .description("API Rest utilizando Java SpringBoot para loja Deva")
                        .version("1.0"));
    }
}