package com.GreenEnergy.gestionProyectos.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdateRequest {
    
    private String nombre; 
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}

