package com.GreenEnergy.gestionUsuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.gestionUsuarios.model.Administrador;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador,Long>{

}
