package com.GreenEnergy.notificacionesPrueba.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "notificaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa una notificación enviada a un usuario")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la notificación", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @Schema(description = "Usuario destinatario de la notificación")
    private Usuario usuario;

    @Schema(description = "Asunto de la notificación", example = "Cambio de estado")
    private String asunto;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Contenido o mensaje de la notificación", example = "Su servicio ha sido actualizado.")
    private String mensaje;

    @Column(name = "fecha_envio")
    @Schema(description = "Fecha y hora en que se envió la notificación", example = "2025-06-23T15:30:00")
    private LocalDateTime fechaEnvio;

    @PrePersist
    public void prePersist() {
        this.fechaEnvio = LocalDateTime.now();
    }
}
