package com.GreenEnergy.gestionProyectos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import com.GreenEnergy.gestionProyectos.DTO.*;
import com.GreenEnergy.gestionProyectos.model.*;
import com.GreenEnergy.gestionProyectos.service.ProyectoMantencionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ProyectoControllerTest {

    @Mock
    private ProyectoMantencionService service;

    @InjectMocks
    private ProyectoController controller;

    private Proyecto proyecto;
    private Mantencion mantencion;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        proyecto = new Proyecto(1L, "Proyecto A", 10, LocalDate.now(), LocalDate.now().plusDays(10),
                Proyecto.EstadoProyecto.CREADO, false, 5L);

        mantencion = new Mantencion(1L, "Mantencion A", LocalDate.now().plusDays(5), 5,
                Mantencion.EstadoMantencion.CREADO, false, 5l);
    }

    @Test
    void crearProyecto_exitoso() {
        Long clienteId = 5L;

        ProjectRequest request = new ProjectRequest();
        request.setNombre("Proyecto A");
        request.setCantidadPaneles(10);
        request.setFechaInicio(LocalDate.now());
        request.setFechaFin(LocalDate.now().plusDays(10));
        request.setClienteId(clienteId);

        proyecto.setClienteId(clienteId);

        when(service.crearProyecto(any())).thenReturn(proyecto);

        ResponseEntity<?> response = controller.crearProyecto(request);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Proyecto);
        Proyecto resultado = (Proyecto) response.getBody();
        assertEquals(clienteId, resultado.getClienteId());
    }

    @Test
    void crearProyecto_errorAsignacion() {
        ProjectRequest request = new ProjectRequest();
        request.setNombre("Proyecto A");
        request.setCantidadPaneles(10);
        request.setFechaInicio(LocalDate.now());
        request.setFechaFin(LocalDate.now().plusDays(10));
        request.setClienteId(1L);

        when(service.crearProyecto(any())).thenThrow(new RuntimeException("Error asignando recursos"));

        ResponseEntity<?> response = controller.crearProyecto(request);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Error asignando recursos"));
    }

    @Test
    void crearMantencion_exitoso() {
        MantencionRequest request = new MantencionRequest();
        request.setNombre("Mantencion A");
        request.setFechaMantencion(LocalDate.now().plusDays(5));
        request.setCantidadPaneles(5);
        request.setClienteId(1L);

        when(service.crearMantencion(any())).thenReturn(mantencion);

        ResponseEntity<?> response = controller.crearMantencion(request);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Mantencion);
    }

    @Test
    void crearMantencion_errorAsignacion() {
        MantencionRequest request = new MantencionRequest();
        request.setNombre("Mantencion A");
        request.setFechaMantencion(LocalDate.now().plusDays(5));
        request.setCantidadPaneles(5);
        request.setClienteId(1L);

        when(service.crearMantencion(any())).thenThrow(new RuntimeException("Error asignando recursos"));

        ResponseEntity<?> response = controller.crearMantencion(request);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Error asignando recursos"));
    }

    @Test
    void listarProyectos() {
        when(service.listarProyectos()).thenReturn(List.of(proyecto));
        ResponseEntity<List<Proyecto>> response = controller.listarProyectos();

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void listarMantenciones() {
        when(service.listarMantenciones()).thenReturn(List.of(mantencion));
        ResponseEntity<List<Mantencion>> response = controller.listarMantenciones();

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void obtenerProyecto_existe() {
        when(service.obtenerProyectoPorId(1L)).thenReturn(Optional.of(proyecto));
        ResponseEntity<Proyecto> response = controller.obtenerProyecto(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(proyecto, response.getBody());
    }

    @Test
    void obtenerProyecto_noExiste() {
        when(service.obtenerProyectoPorId(1L)).thenReturn(Optional.empty());
        ResponseEntity<Proyecto> response = controller.obtenerProyecto(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void obtenerMantencion_existe() {
        when(service.obtenerMantencionPorId(1L)).thenReturn(Optional.of(mantencion));
        ResponseEntity<Mantencion> response = controller.obtenerMantencion(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mantencion, response.getBody());
    }

    @Test
    void obtenerMantencion_noExiste() {
        when(service.obtenerMantencionPorId(1L)).thenReturn(Optional.empty());
        ResponseEntity<Mantencion> response = controller.obtenerMantencion(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void actualizarProyecto_exitoso() {
        ProjectUpdateRequest update = new ProjectUpdateRequest("Nuevo Nombre", LocalDate.now(),
                LocalDate.now().plusDays(5));
        when(service.actualizarProyecto(eq(1L), any())).thenReturn(proyecto);

        ResponseEntity<?> response = controller.actualizarProyecto(1L, update);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(proyecto, response.getBody());
    }

    @Test
    void actualizarProyecto_error() {
        ProjectUpdateRequest update = new ProjectUpdateRequest("Nuevo Nombre", LocalDate.now(),
                LocalDate.now().plusDays(5));
        when(service.actualizarProyecto(eq(1L), any())).thenThrow(new RuntimeException("No encontrado"));

        ResponseEntity<?> response = controller.actualizarProyecto(1L, update);
        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("No encontrado"));
    }

    @Test
    void cancelarProyecto_exitoso() {
        when(service.cancelarProyecto(1L)).thenReturn(proyecto);

        ResponseEntity<?> response = controller.cancelarProyecto(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(proyecto, response.getBody());
    }

    @Test
    void cancelarProyecto_noEncontrado() {
        when(service.cancelarProyecto(1L)).thenThrow(new NoSuchElementException());

        ResponseEntity<?> response = controller.cancelarProyecto(1L);
        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("no encontrado")
                || response.getBody().toString().contains("Proyecto no encontrado"));
    }

    @Test
    void cancelarProyecto_error() {
        when(service.cancelarProyecto(1L)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = controller.cancelarProyecto(1L);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Error"));
    }

    @Test
    void cancelarMantencion_exitoso() {
        when(service.cancelarMantencion(1L)).thenReturn(mantencion);

        ResponseEntity<?> response = controller.cancelarMantencion(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mantencion, response.getBody());
    }

    @Test
    void cancelarMantencion_noEncontrada() {
        when(service.cancelarMantencion(1L)).thenThrow(new NoSuchElementException());

        ResponseEntity<?> response = controller.cancelarMantencion(1L);
        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("no encontrada")
                || response.getBody().toString().contains("Mantención no encontrada"));
    }

    @Test
    void cancelarMantencion_error() {
        when(service.cancelarMantencion(1L)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = controller.cancelarMantencion(1L);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Error"));
    }

    @Test
    void proyectosEnCurso() {
        when(service.proyectosEnCurso()).thenReturn(List.of(proyecto));

        ResponseEntity<List<Proyecto>> response = controller.proyectosEnCurso();
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void finalizarProyectoAnticipadamente_exitoso() {
        when(service.finalizarProyectoAnticipadamente(1L)).thenReturn(proyecto);

        ResponseEntity<?> response = controller.finalizarProyectoAnticipadamente(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(proyecto, response.getBody());
    }

    @Test
    void finalizarProyectoAnticipadamente_error() {
        when(service.finalizarProyectoAnticipadamente(1L)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = controller.finalizarProyectoAnticipadamente(1L);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Error"));
    }
}
