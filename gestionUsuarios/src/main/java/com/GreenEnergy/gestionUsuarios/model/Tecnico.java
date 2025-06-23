package com.GreenEnergy.gestionUsuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tecnicos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa a un técnico especializado")
public class Tecnico {

    @Id
    @Schema(description = "ID único del técnico", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Especialidad del técnico", example = "electricista")
    private String especialidad;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @Schema(description = "Datos del usuario asociado al técnico")
    private Usuario usuario;

}
