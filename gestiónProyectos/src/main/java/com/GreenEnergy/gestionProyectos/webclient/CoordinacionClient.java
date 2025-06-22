package com.GreenEnergy.gestionProyectos.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.GreenEnergy.gestionProyectos.DTO.MantencionRequest;
import com.GreenEnergy.gestionProyectos.DTO.ProjectRequest;

import reactor.core.publisher.Mono;

@Component
public class CoordinacionClient {
        private final WebClient webClient;

        public CoordinacionClient(@Value("${coordinacion-service.url}") String coordinacionServiceUrl) {
                this.webClient = WebClient.builder().baseUrl(coordinacionServiceUrl).build();
        }

        public void asignarRecursos(ProjectRequest request) {
                webClient.post()
                                .uri("/asignar")
                                .bodyValue(request)
                                .retrieve()
                                .onStatus(status -> status.is4xxClientError(),response -> response.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new RuntimeException("Error 4xx en Coordinación: "+ errorMessage))))
                                .onStatus(status -> status.is5xxServerError(),response -> response.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new RuntimeException("Error 5xx en Coordinación: "+ errorMessage))))

                                .bodyToMono(Void.class).block();
        }

        public void asignarRecursosMantencion(MantencionRequest request) {
                webClient.post()
                                .uri("/asignar-mantencion")
                                .bodyValue(request)
                                .retrieve()
                                .onStatus(status -> status.is4xxClientError(),response -> response.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new RuntimeException("Error 4xx en Coordinación: "+ errorMessage))))
                                .onStatus(status -> status.is5xxServerError(),response -> response.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new RuntimeException("Error 5xx en Coordinación: "+ errorMessage))))

                                .bodyToMono(Void.class).block();
        }

        public void devolverMaterialesMantencion(Long mantencionId) {
                webClient.post()
                                .uri(uriBuilder -> uriBuilder.path("/devolver-mantencion/{mantencionId}").build(mantencionId))
                                .retrieve()
                                .onStatus(status -> status.is4xxClientError(),response -> response.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new RuntimeException("Error 4xx en Coordinación: "+ errorMessage))))
                                .onStatus(status -> status.is5xxServerError(),response -> response.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new RuntimeException("Error 5xx en Coordinación: "+ errorMessage))))

                                .bodyToMono(Void.class).block();
        }

        public void devolverMaterialesProyecto(Long proyectoId) {
                webClient.post()
                                .uri(uriBuilder -> uriBuilder.path("/devolver/{proyectoId}").build(proyectoId))
                                .retrieve()
                                .onStatus(status -> status.is4xxClientError(),response -> response.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new RuntimeException("Error 4xx en Coordinación: "+ errorMessage))))
                                .onStatus(status -> status.is5xxServerError(),response -> response.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new RuntimeException("Error 5xx en Coordinación: "+ errorMessage))))

                                .bodyToMono(Void.class).block();
        }

}
