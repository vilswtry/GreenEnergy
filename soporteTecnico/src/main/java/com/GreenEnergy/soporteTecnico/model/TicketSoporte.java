package com.GreenEnergy.soporteTecnico.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "tickets_soporte")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un ticket de soporte técnico")
public class TicketSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del ticket", example = "123")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(description = "Usuario que abrió el ticket", required = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "tecnico_soporte_id")
    @Schema(description = "Técnico de soporte asignado al ticket")
    private Usuario tecnicoSoporte;

    @Column(nullable = false)
    @Schema(description = "Asunto del ticket", example = "Problema con panel solar")
    private String asunto;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Schema(description = "Descripción detallada del problema o consulta", example = "El panel no está generando energía")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Respuesta dada por el técnico de soporte", example = "Revisaremos el panel el próximo lunes")
    private String respuesta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado actual del ticket", example = "CREADO")
    private EstadoTicket estado;

    @Column(name = "fecha_creacion", nullable = false)
    @Schema(description = "Fecha y hora de creación del ticket")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    @Schema(description = "Fecha y hora de la última actualización del ticket")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        estado = EstadoTicket.CREADO;
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    @Schema(description = "Estados posibles para un ticket de soporte")
    public enum EstadoTicket {
        CREADO,
        EN_PROGRESO,
        CERRADO
    }
}
