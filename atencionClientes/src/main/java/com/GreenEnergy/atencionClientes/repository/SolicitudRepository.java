package com.GreenEnergy.atencionClientes.repository;

import com.GreenEnergy.atencionClientes.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
}
