package com.GreenEnergy.gestionUsuarios.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa a un usuario del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Column(unique = true, nullable = false)
    @Schema(description = "RUT único del usuario", example = "12345678-9")
    private String rut;

    @Column(unique = true, nullable = false)
    @Schema(description = "Correo electrónico único del usuario", example = "juan.perez@example.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Número de teléfono del usuario", example = "+56912345678")
    private String telefono;

    @Column(unique = true, nullable = false)
    @Schema(description = "Contraseña cifrada del usuario")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Rol asignado al usuario")
    private Rol rol;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Datos de cliente asociados")
    private Cliente cliente;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Datos de técnico asociados")
    private Tecnico tecnico;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Datos de técnico de soporte asociados")
    private TecnicoSoporte tecnicoSoporte;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Datos de administrador asociados")
    private Administrador administrador;

}
