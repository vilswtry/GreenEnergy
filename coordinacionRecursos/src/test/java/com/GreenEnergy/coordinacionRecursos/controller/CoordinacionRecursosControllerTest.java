package com.GreenEnergy.coordinacionRecursos.controller;

import com.GreenEnergy.coordinacionRecursos.model.Material;
import com.GreenEnergy.coordinacionRecursos.service.CoordinacionRecursosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CoordinacionRecursosController.class)
public class CoordinacionRecursosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoordinacionRecursosService coordinacionService;

    @Test
    void listarMateriales_OK() throws Exception {
        when(coordinacionService.listarMateriales()).thenReturn(List.of(new Material()));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/coordinacion/materiales"))
                .andExpect(status().isOk());
    }

    @Test
    void asignarRecursos_OK() throws Exception {
        String jsonRequest = """
            {
                "fechaInicio": "2025-07-10",
                "fechaFin": "2025-07-15"
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/coordinacion/asignar")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Recursos asignados correctamente al proyecto."));
    }

    @Test
    void asignarRecursos_fechaInvalida() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/coordinacion/asignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"proyectoId\":1,\"cantidadPaneles\":10}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void asignarRecursosMantencion_OK() throws Exception {
        String jsonRequest = """
            {
                "fechaInicio": "2025-07-10",
                "fechaFin": "2025-07-15"
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/coordinacion/asignar-mantencion")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Recursos de mantención asignados correctamente."));
    }

    @Test
    void devolverMaterialesProyecto_OK() throws Exception {
        doNothing().when(coordinacionService).devolverMaterialesProyecto(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/coordinacion/devolver/1"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerMaterialesFaltantes_vacio() throws Exception {
        when(coordinacionService.listarMaterialesFaltantes()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/coordinacion/materiales/faltantes"))
                .andExpect(status().isOk())
                .andExpect(content().string("No hace falta reponer ningún material."));
    }

    @Test
    void buscarMaterialPorCodigo_OK() throws Exception {
        Material material = new Material(1L, "PS", "Panel Solar", "unidad", 15);
        
        when(coordinacionService.buscarMaterialPorCodigo("PS")).thenReturn(material);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/coordinacion/materiales/PS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoMaterial").value("PS"))
                .andExpect(jsonPath("$.nombreMaterial").value("Panel Solar"));
    }

    @Test
    void buscarMaterialPorCodigo_noExiste() throws Exception {
        when(coordinacionService.buscarMaterialPorCodigo("XYZ")).thenThrow(new RuntimeException("Código inválido"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/coordinacion/materiales/XYZ"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Código inválido"));
    }
}
