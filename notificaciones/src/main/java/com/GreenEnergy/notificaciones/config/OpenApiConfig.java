package com.GreenEnergy.notificaciones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Notificaciones Green Energy SPA")
                                .version("1.0")
                                .description(
                                        "Documentación de un microservicio encargado de las notificaciones de la empresa Green Energy SPA"));
    }
}
