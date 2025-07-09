package com.GreenEnergy.gestionProyectos.DTO;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ProjectRequest", description = "DTO para crear un nuevo proyecto")
public class ProjectRequest {

    @Schema(description = "ID del proyecto", example = "1")
    private Long proyectoId;

    @Schema(description = "ID del cliente que solicita el proyecto", example = "10", required = true)
    private Long clienteId;

    @Schema(description = "Nombre del proyecto", example = "Instalación Solar en Casa Pérez")
    private String nombre;

    @Schema(description = "Cantidad de paneles que serán instalados", example = "20")
    private int cantidadPaneles;

    @Schema(description = "Fecha de inicio del proyecto", example = "2025-07-01")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin del proyecto", example = "2025-07-10")
    private LocalDate fechaFin;
}
