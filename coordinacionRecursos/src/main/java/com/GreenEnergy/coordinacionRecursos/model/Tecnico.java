package com.GreenEnergy.coordinacionRecursos.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name= "tecnicos")
@AllArgsConstructor
@NoArgsConstructor
public class Tecnico {
    @Id
    private Long id;

    @Column(nullable = false)
    private String especialidad;

    @ElementCollection
    @CollectionTable(name = "tecnico_fechas_ocupadas", joinColumns = @JoinColumn(name = "tecnico_id"))
    @Column(name = "fecha")
    private Set<LocalDate> fechasOcupadas = new HashSet<>();

}
