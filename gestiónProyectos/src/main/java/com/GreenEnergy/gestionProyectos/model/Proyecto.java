package com.GreenEnergy.gestionProyectos.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Proyectos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int cantidadPaneles;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public enum EstadoProyecto {
        CREADO, EN_PROGRESO, CANCELADO, FINALIZADO
    }

    @Enumerated(EnumType.STRING)
    private EstadoProyecto estado;

    private boolean recursosAsignados;

}
