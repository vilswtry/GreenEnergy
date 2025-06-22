package com.GreenEnergy.coordinacionRecursos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.coordinacionRecursos.model.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByCodigoMaterial(String codigoMaterial);
}
