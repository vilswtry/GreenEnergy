package com.GreenEnergy.gestionProyectos.DTO;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "MantencionRequest", description = "DTO para crear una mantención")
public class MantencionRequest {

    @Schema(description = "ID de la mantención", example = "1")
    private Long mantencionId;

    @Schema(description = "Nombre del cliente o proyecto", example = "Juan Pérez")
    private String nombre;

    @Schema(description = "Fecha programada para la mantención", example = "2025-07-15")
    private LocalDate fechaMantencion;

    @Schema(description = "Cantidad de paneles involucrados en la mantención", example = "10")
    private int cantidadPaneles;
}
