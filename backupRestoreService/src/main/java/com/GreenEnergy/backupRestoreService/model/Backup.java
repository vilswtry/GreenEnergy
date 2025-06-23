package com.GreenEnergy.backupRestoreService.model;

import java.util.Date;

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
@Table(name = "backups")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Backup", description = "Entidad que representa un respaldo de base de datos")
public class Backup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del respaldo", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre del archivo de respaldo", example = "backup_20230623.sql", required = true)
    private String filename; 

    @Column(nullable = false)
    @Schema(description = "Fecha de creación del respaldo", example = "2023-06-23T14:30:00Z", required = true)
    private Date createdAt;
}
