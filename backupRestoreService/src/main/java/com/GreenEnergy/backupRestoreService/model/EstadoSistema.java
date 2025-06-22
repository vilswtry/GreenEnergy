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

@Entity
@Table(name = "monitoreos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Double cpuUsada;

    @Column(nullable = false)
    private Long memoriaLibre;

    @Column(nullable = false)
    private Long memoriaUsada;

    @Column(nullable = false)
    private String estadoSistema;

    @Column(nullable = false)
    private LocalDateTime fecha;
    
}
