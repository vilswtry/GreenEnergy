package com.GreenEnergy.gestionProyectos.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MantencionRequest {


    private Long mantencionId;
    private String nombre;
    private LocalDate fechaMantencion;
    private int cantidadPaneles;
}
