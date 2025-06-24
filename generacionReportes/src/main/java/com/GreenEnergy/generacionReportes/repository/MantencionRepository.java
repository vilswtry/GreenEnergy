package com.GreenEnergy.generacionReportes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.generacionReportes.model.Mantencion;

@Repository
public interface MantencionRepository extends JpaRepository<Mantencion, Long> {

}
