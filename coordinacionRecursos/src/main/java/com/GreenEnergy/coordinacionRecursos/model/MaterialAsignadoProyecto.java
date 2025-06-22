package com.GreenEnergy.coordinacionRecursos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "material_asignado_proyecto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialAsignadoProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long proyectoId;
    private String codigoMaterial;
    private int cantAsignado;

}
