package com.GreenEnergy.gestionUsuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.GreenEnergy.gestionUsuarios.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long>{

}
