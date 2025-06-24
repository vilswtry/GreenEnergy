package com.GreenEnergy.generacionReportes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo(){
        return new OpenAPI()
            .info(
                new Info()
                .title("Generacion de reportes Green energy")
                .version("1.0")
                .description("Documentación de un microservicio encargado de la generacion de reportes de los proyectos de instalacion y mantencion de paneles solares de Green Energy SPA")
            );
    }

}
