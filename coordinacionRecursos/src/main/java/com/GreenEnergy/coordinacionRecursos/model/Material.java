package com.GreenEnergy.coordinacionRecursos.model;

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
@Table(name = "Inventario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Material", description = "Representa un material disponible en el inventario")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del material", example = "1")
    private long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Código único del material", example = "MAT-001")
    private String codigoMaterial;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre del material", example = "Panel Solar")
    private String nombreMaterial;

    @Column(nullable = false)
    @Schema(description = "Unidad de medida del material", example = "unidad")
    private String unidadMedida;

    @Schema(description = "Cantidad disponible en stock", example = "100")
    private int stock;

}
