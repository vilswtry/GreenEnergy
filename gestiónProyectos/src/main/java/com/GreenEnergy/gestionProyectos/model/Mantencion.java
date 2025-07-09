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
@Table(name = "mantenciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Mantencion", description = "Entidad que representa una mantención de paneles solares.")
public class Mantencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la mantención", example = "1")
    private Long id;

    @Schema(description = "Nombre o título de la mantención", example = "Mantención mensual sector sur")
    private String nombre;

    @Schema(description = "Fecha en que se realiza la mantención", example = "2025-07-01")
    private LocalDate fechaMantencion;

    @Schema(description = "Cantidad de paneles que se revisarán en la mantención", example = "20")
    private int cantidadPaneles;

    public enum EstadoMantencion {
        CREADO, EN_PROGRESO, CANCELADO, FINALIZADO
    }

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual de la mantención", example = "CREADO")
    private EstadoMantencion estado;

    @Schema(description = "Indica si los recursos ya fueron asignados para esta mantención", example = "true")
    private boolean recursosAsignados;

    @Schema(description = "ID del cliente asociado a esta mantención", example = "10")
    @Column(nullable = false)
    private Long clienteId;
}
