package com.GreenEnergy.gestionProyectos.DTO;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ProjectUpdateRequest", description = "DTO utilizado para actualizar los detalles de un proyecto existente.")
public class ProjectUpdateRequest {

    @Schema(description = "Nuevo nombre del proyecto", example = "Instalación Paneles Norte")
    private String nombre;

    @Schema(description = "Nueva fecha de inicio del proyecto", example = "2025-08-01")
    private LocalDate fechaInicio;

    @Schema(description = "Nueva fecha de fin del proyecto", example = "2025-08-15")
    private LocalDate fechaFin;
}
