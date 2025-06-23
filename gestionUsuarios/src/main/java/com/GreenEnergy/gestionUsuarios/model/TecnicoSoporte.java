package com.GreenEnergy.gestionUsuarios.model;

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
@Table(name = "tecnicos_soporte")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa a un técnico de soporte")
public class TecnicoSoporte {

    @Id
    @Schema(description = "ID único del técnico de soporte", example = "1")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @Schema(description = "Datos del usuario asociado al técnico de soporte")
    private Usuario usuario;

}
