package com.GreenEnergy.gestionUsuarios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa a un Cliente dentro del sistema")
public class Cliente {

    @Id
    @Schema(description = "ID único del cliente, que coincide con el usuario asociado", example = "1")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @Schema(description = "Usuario asociado con el rol de cliente")
    private Usuario usuario;

}
