package com.GreenEnergy.gestionProyectos.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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
@Table(name = "proyectos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Proyecto", description = "Entidad que representa un proyecto de instalación solar.")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del proyecto", example = "1")
    private Long id;

    @Schema(description = "Nombre del proyecto", example = "Instalación solar barrio norte")
    private String nombre;

    @Schema(description = "Cantidad de paneles solares en el proyecto", example = "50")
    private int cantidadPaneles;

    @Schema(description = "Fecha de inicio del proyecto", example = "2025-07-01")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin del proyecto", example = "2025-07-30")
    private LocalDate fechaFin;

    public enum EstadoProyecto {
        CREADO, EN_PROGRESO, CANCELADO, FINALIZADO
    }

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual del proyecto", example = "CREADO")
    private EstadoProyecto estado;

    @Schema(description = "Indica si los recursos han sido asignados al proyecto", example = "false")
    private boolean recursosAsignados;

    @Schema(description = "ID del cliente asociado a este proyecto", example = "10")
    @Column(nullable = false)
    private Long clienteId;

}
