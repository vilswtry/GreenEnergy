package com.GreenEnergy.generacionReportes.service;

import com.GreenEnergy.generacionReportes.model.Mantencion;
import com.GreenEnergy.generacionReportes.model.Proyecto;
import com.GreenEnergy.generacionReportes.model.Reporte;
import com.GreenEnergy.generacionReportes.repository.MantencionRepository;
import com.GreenEnergy.generacionReportes.repository.ProyectoRepository;
import com.GreenEnergy.generacionReportes.repository.ReporteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private MantencionRepository mantencionRepository;

    @InjectMocks
    private ReporteService reporteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearReporte_conProyectoFinalizado_deberiaGuardarYRetornarReporte() {
        Proyecto proyecto = new Proyecto();
        proyecto.setId(1L);
        proyecto.setEstado(Proyecto.EstadoProyecto.FINALIZADO);
        proyecto.setFechaInicio(LocalDate.of(2025, 1, 1));
        proyecto.setFechaFin(LocalDate.of(2025, 1, 5));

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(reporteRepository.save(any(Reporte.class))).thenAnswer(i -> i.getArgument(0));

        Reporte resultado = reporteService.crearReporte(1L, "PROYECTO", "ninguno", "buena", "alta");

        assertNotNull(resultado);
        assertEquals("PROYECTO", resultado.getTipoProyecto());
        assertEquals(5, resultado.getDuracionDias());
        assertEquals("buena", resultado.getRetroalimentacionCliente());
    }

    @Test
    void crearReporte_conMantencionFinalizada_deberiaGuardarYRetornarReporte() {
        Mantencion mantencion = new Mantencion();
        mantencion.setId(2L);
        mantencion.setEstado(Mantencion.EstadoMantencion.FINALIZADO);

        when(mantencionRepository.findById(2L)).thenReturn(Optional.of(mantencion));
        when(reporteRepository.save(any(Reporte.class))).thenAnswer(i -> i.getArgument(0));

        Reporte resultado = reporteService.crearReporte(2L, "MANTENCION", "problema x", "respuesta", "eficiente");

        assertNotNull(resultado);
        assertEquals("MANTENCION", resultado.getTipoProyecto());
        assertEquals(1L, resultado.getDuracionDias());
        assertEquals("eficiente", resultado.getEficiencia());
    }

    @Test
    void crearReporte_proyectoNoFinalizado_deberiaLanzarExcepcion() {
        Proyecto proyecto = new Proyecto();
        proyecto.setId(3L);
        proyecto.setEstado(Proyecto.EstadoProyecto.EN_PROGRESO);

        when(proyectoRepository.findById(3L)).thenReturn(Optional.of(proyecto));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reporteService.crearReporte(3L, "PROYECTO", "", "", ""));

        assertEquals("El proyecto no está finalizado", ex.getMessage());
    }

    @Test
    void crearReporte_tipoInvalido_deberiaLanzarExcepcion() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reporteService.crearReporte(4L, "INVALIDO", "", "", ""));

        assertEquals("Tipo inválido: debe ser 'PROYECTO' o 'MANTENCION'", ex.getMessage());
    }

    @Test
    void crearReporte_proyectoInexistente_excepcion() {
        when(proyectoRepository.findById(8L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reporteService.crearReporte(8L, "PROYECTO", "", "", ""));

        assertEquals("Proyecto no encontrado", ex.getMessage());
    }

    @Test
    void crearReporte_mantencionInexistente_excepcion() {
        when(mantencionRepository.findById(8L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reporteService.crearReporte(8L, "MANTENCION", "", "", ""));

        assertEquals("Mantención no encontrada", ex.getMessage());
    }

    @Test
    void buscarReportesPorFechaCreacion_conResultados() {
        LocalDate fecha = LocalDate.of(2025, 6, 1);
        when(reporteRepository.findByFechaCreacion(fecha)).thenReturn(Collections.singletonList(new Reporte()));

        assertEquals(1, reporteService.buscarReportesPorFechaCreacion(fecha).size());
    }

    @Test
    void buscarReportesPorProyectoId_conResultados() {
        when(reporteRepository.findByIdProyectoMantencion(100L)).thenReturn(Collections.singletonList(new Reporte()));

        assertEquals(1, reporteService.buscarReportesPorProyectoId(100L).size());
    }

    @Test
    void listarReeportes() {
        Reporte reporte1 = new Reporte();
        reporte1.setId(1L);

        Reporte reporte2 = new Reporte();
        reporte2.setId(2L);

        when(reporteRepository.findAll()).thenReturn(List.of(reporte1, reporte2));

        List<Reporte> resultado = reporteService.listarReportes();

        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
    }
}
