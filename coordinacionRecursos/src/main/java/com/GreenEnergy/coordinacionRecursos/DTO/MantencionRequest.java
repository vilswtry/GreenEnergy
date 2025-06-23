package com.GreenEnergy.coordinacionRecursos.DTO;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para asignar recursos a una mantención")
public class MantencionRequest {

    @Schema(description = "ID de la mantención", example = "1")
    private Long mantencionId;

    @Schema(description = "Fecha de la mantención", example = "2025-07-01")
    private LocalDate fechaMantencion;

    @Schema(description = "Cantidad de paneles que requiere la mantención", example = "10")
    private int cantidadPaneles;
}
