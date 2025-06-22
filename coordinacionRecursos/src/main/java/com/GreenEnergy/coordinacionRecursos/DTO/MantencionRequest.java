package com.GreenEnergy.coordinacionRecursos.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MantencionRequest {
    
    private Long mantencionId;
    private LocalDate fechaMantencion;
    private int cantidadPaneles;
}
