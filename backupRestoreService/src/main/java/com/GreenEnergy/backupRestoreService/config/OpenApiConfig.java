package com.GreenEnergy.backupRestoreService.config;

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
                .title("Backup, restore y monitoreo de Green Energy SPA")
                .version("1.0")
                .description("Documentación de un microservicio encargado de respaldar, restaurar y monitorear el estado de la aplicacion de Green Energy SPA")
            );
    }

}
