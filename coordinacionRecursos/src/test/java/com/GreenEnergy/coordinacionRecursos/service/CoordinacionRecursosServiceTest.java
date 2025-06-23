package com.GreenEnergy.coordinacionRecursos.service;

import com.GreenEnergy.coordinacionRecursos.model.Material;
import com.GreenEnergy.coordinacionRecursos.repository.MaterialRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CoordinacionRecursosServiceTest {

    @InjectMocks
    private CoordinacionRecursosService service;

    @Mock
    private MaterialRepository materialRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void reponerMaterial_exitoso() {
        Material material = new Material(1L, "MAT001", "Panel Solar", "unidad", 5);
        when(materialRepository.findByCodigoMaterial("MAT001")).thenReturn(Optional.of(material));
        when(materialRepository.save(any(Material.class))).thenReturn(material);

        Material result = service.reponerMaterial("MAT001", 10);
        assertEquals(15, result.getStock());
    }

    @Test
    void buscarMaterialPorCodigo_existe() {
        Material material = new Material(1L, "MAT001", "Panel Solar", "unidad", 10);
        when(materialRepository.findByCodigoMaterial("MAT001")).thenReturn(Optional.of(material));

        Material result = service.buscarMaterialPorCodigo("MAT001");
        assertEquals("MAT001", result.getCodigoMaterial());
    }

    @Test
    void listarMaterialesFaltantes_sinFaltantes_retornaListaVacia() {
        when(materialRepository.findAll()).thenReturn(List.of(
                new Material(1L, "MAT001", "Panel Solar", "unidad", 10),
                new Material(2L, "MAT002", "Cable", "metro", 5)));

        List<Material> faltantes = service.listarMaterialesFaltantes();

        assertNotNull(faltantes);
        assertTrue(faltantes.isEmpty(), "La lista de faltantes debería estar vacía");
    }

}
