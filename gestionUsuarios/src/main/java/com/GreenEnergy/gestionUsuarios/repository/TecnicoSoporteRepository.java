package com.GreenEnergy.gestionUsuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.GreenEnergy.gestionUsuarios.model.TecnicoSoporte;

@Repository
public interface TecnicoSoporteRepository extends JpaRepository<TecnicoSoporte,Long>{

}
