package com.GreenEnergy.gestionUsuarios.config;

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
                                .title("Gestión de usuarios Green Energy SPA")
                                .version("1.0")
                                .description(
                                        "Documentación de un Microservicio encragado de la gestion de usuarios de la empresa Green Energy SPA"));
    }
}
