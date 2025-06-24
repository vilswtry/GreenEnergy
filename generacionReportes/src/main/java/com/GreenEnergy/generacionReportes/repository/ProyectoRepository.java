package com.GreenEnergy.generacionReportes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.generacionReportes.model.Proyecto;


@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

}
