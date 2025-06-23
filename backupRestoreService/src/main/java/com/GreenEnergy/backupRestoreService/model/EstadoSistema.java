package com.GreenEnergy.backupRestoreService.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "monitoreos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "EstadoSistema", description = "Entidad que representa el estado de monitoreo del sistema")
public class EstadoSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del monitoreo", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @Column(nullable = false)
    @Schema(description = "Porcentaje de CPU usada", example = "30.5", required = true)
    private Double cpuUsada;

    @Column(nullable = false)
    @Schema(description = "Memoria libre en bytes", example = "2048000", required = true)
    private Long memoriaLibre;

    @Column(nullable = false)
    @Schema(description = "Memoria usada en bytes", example = "4096000", required = true)
    private Long memoriaUsada;

    @Column(nullable = false)
    @Schema(description = "Estado descriptivo del sistema", example = "Sistema funcionando correctamente", required = true)
    private String estadoSistema;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora del monitoreo", example = "2025-06-23T15:30:00", required = true)
    private LocalDateTime fecha;
    
}
