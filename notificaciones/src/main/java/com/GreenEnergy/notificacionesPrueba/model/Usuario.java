package com.GreenEnergy.notificacionesPrueba.model;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa a un usuario del sistema")
public class Usuario {

    @Id
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Schema(description = "RUT único del usuario", example = "12345678-9")
    private String rut;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String email;

    @Schema(description = "Número de teléfono del usuario", example = "+56912345678")
    private String telefono;

    @Schema(description = "Contraseña cifrada del usuario")
    private String password;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Rol asignado al usuario", example = "CLIENTE")
    private Rol rol;

    public enum Rol {
        CLIENTE, TECNICO, ADMINISTRADOR, TECNICO_SOPORTE
    }
}
