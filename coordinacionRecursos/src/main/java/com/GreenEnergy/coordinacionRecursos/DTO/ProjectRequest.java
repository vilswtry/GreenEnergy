package com.GreenEnergy.coordinacionRecursos.DTO;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para asignar recursos a un proyecto")
public class ProjectRequest {

    @Schema(description = "ID del proyecto", example = "1001")
    private Long proyectoId;

    @Schema(description = "Cantidad de paneles necesarios para el proyecto", example = "25")
    private int cantidadPaneles;

    @Schema(description = "Fecha de inicio del proyecto", example = "2025-07-05")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de finalización del proyecto", example = "2025-07-10")
    private LocalDate fechaFin;
}
