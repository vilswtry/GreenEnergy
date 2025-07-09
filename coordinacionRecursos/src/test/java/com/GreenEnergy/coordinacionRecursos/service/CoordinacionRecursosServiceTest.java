package com.GreenEnergy.coordinacionRecursos.service;

import com.GreenEnergy.coordinacionRecursos.model.Material;
import com.GreenEnergy.coordinacionRecursos.repository.MaterialRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    void reponerMaterial_cantidadInvalida() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.reponerMaterial("MAT001", 0);
        });

        assertEquals("La cantidad a reponer debe ser mayor a cero.", exception.getMessage());
    }

    @Test
    void reponerMaterial_codigoInvalido() {
        when(materialRepository.findByCodigoMaterial("MAT7")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.reponerMaterial("MAT7", 5);
        });

        assertEquals("Código de material inválido: MAT7", exception.getMessage());
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

    @Test
    void listarMaterialesFaltantes_retornarSoloFaltantes() {
        Material ps = new Material();
        ps.setCodigoMaterial("PS");
        ps.setStock(5);

        Material inv = new Material();
        inv.setCodigoMaterial("INV");
        inv.setStock(100);

        when(materialRepository.findAll()).thenReturn(List.of(ps, inv));

        List<Material> faltantes = service.listarMaterialesFaltantes();

        assertEquals(1, faltantes.size());
        assertEquals("PS", faltantes.get(0).getCodigoMaterial());
    }

    @Test
    void listarMateriales_retornaLista() {
        Material mat1 = new Material();
        mat1.setId(1L);

        Material mat2 = new Material();
        mat2.setId(2L);

        when(materialRepository.findAll()).thenReturn(List.of(mat1, mat2));

        List<Material> resultado = service.listarMateriales();

        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
    }

}
