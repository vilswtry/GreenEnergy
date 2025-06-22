package com.GreenEnergy.atencionClientes.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoSolicitud; // instalación o mantención
    private String usuario;
    private String password;
    private String telefono;
    private String comentarios;

    private LocalDate fechaSolicitud = LocalDate.now();
    private String estado = "pendiente";
}
