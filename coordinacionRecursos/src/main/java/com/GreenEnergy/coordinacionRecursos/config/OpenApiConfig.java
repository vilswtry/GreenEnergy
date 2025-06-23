package com.GreenEnergy.coordinacionRecursos.config;

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
                .title("Coordinacion de recursos de Green energy")
                .version("1.0")
                .description("Documentación de un microservicio encargado de la correcta asignacion de recursos y manejo de inventario de Green Energy SPA")
            );
    }

}
