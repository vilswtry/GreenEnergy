package com.GreenEnergy.generacionReportes.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mantenciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mantencion {

    @Id
    private Long id;

    private String nombre;
    private LocalDate fechaMantencion;
    private int cantidadPaneles;

    public enum EstadoMantencion {
        CREADO, EN_PROGRESO, CANCELADO, FINALIZADO
    }

    @Enumerated(EnumType.STRING)
    private EstadoMantencion estado;

    private boolean recursosAsignados;
}