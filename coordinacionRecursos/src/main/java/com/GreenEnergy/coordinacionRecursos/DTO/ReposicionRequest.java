package com.GreenEnergy.coordinacionRecursos.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para reponer stock de un material")
public class ReposicionRequest {

    @Schema(description = "Código del material a reponer", example = "MAT-001")
    private String codigoMaterial;

    @Schema(description = "Cantidad de unidades a reponer", example = "50")
    private int cantidad;
}
