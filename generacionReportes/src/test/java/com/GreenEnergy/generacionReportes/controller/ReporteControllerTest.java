package com.GreenEnergy.generacionReportes.controller;

import com.GreenEnergy.generacionReportes.model.Reporte;
import com.GreenEnergy.generacionReportes.service.ReporteService;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReporteController.class)
public class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @Test
    void listarTodos_deberiaRetornarListaDeReportes() throws Exception {
        Reporte r = new Reporte();
        r.setId(1L);
        r.setIdProyectoMantencion(100L);
        r.setTipoProyecto("PROYECTO");

        when(reporteService.listarReportes()).thenReturn(List.of(r));

        mockMvc.perform(get("/reportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void crearReporte_deberiaCrearYRetornarReporte() throws Exception {
        Reporte nuevo = new Reporte();
        nuevo.setId(1L);
        nuevo.setTipoProyecto("PROYECTO");

        when(reporteService.crearReporte(1L, "PROYECTO", "ninguno", "buena", "alta")).thenReturn(nuevo);

        mockMvc.perform(post("/reportes")
                        .param("idProyectoMantencion", "1")
                        .param("tipo", "PROYECTO")
                        .param("problemas", "ninguno")
                        .param("retroalimentacion", "buena")
                        .param("eficiencia", "alta"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPorProyectoId_siNoHay_deberiaRetornar404() throws Exception {
        when(reporteService.buscarReportesPorProyectoId(10L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/reportes/proyecto/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorFecha_formatoInvalido_deberiaRetornar400() throws Exception {
        mockMvc.perform(get("/reportes/fecha")
                        .param("fecha", "2025/01/01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorFecha_sinResultados_deberiaRetornar404() throws Exception {
        when(reporteService.buscarReportesPorFechaCreacion(LocalDate.parse("2025-06-01")))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/reportes/fecha")
                        .param("fecha", "2025-06-01"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorFecha_conResultados_deberiaRetornar200() throws Exception {
        Reporte r = new Reporte();
        r.setId(1L);
        when(reporteService.buscarReportesPorFechaCreacion(LocalDate.parse("2025-06-01")))
                .thenReturn(List.of(r));

        mockMvc.perform(get("/reportes/fecha")
                        .param("fecha", "2025-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
