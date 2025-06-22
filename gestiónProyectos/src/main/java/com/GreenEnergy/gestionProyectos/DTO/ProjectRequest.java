package com.GreenEnergy.gestionProyectos.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
    
    private Long proyectoId;
    private String nombre;
    private int cantidadPaneles;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}