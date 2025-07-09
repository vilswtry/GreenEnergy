package com.GreenEnergy.gestionProyectos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.*;

import com.GreenEnergy.gestionProyectos.DTO.*;
import com.GreenEnergy.gestionProyectos.model.*;
import com.GreenEnergy.gestionProyectos.service.ProyectoMantencionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(ProyectoController.class)
public class ProyectoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProyectoMantencionService service;

    @Autowired
    private ObjectMapper objectMapper;

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
    void crearProyecto_exitoso() throws Exception {
        ProjectRequest request = new ProjectRequest(null, 10L, "casa sur", 5, LocalDate.now(),
                LocalDate.now().plusDays(10));

        when(service.crearProyecto(any())).thenReturn(proyecto);

        mockMvc.perform(post("/api/proyectos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").value(5L));
    }

    @Test
    void crearProyecto_errorAsignacion() throws Exception {
        ProjectRequest request = new ProjectRequest(null, 10L, "Proeycto A", 5,
                LocalDate.now(), LocalDate.now().plusDays(10));

        when(service.crearProyecto(any())).thenThrow(new RuntimeException("Error asignando recursos"));

        mockMvc.perform(post("/api/proyectos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error asignando recursos")));
    }

    @Test
    void crearMantencion_exitoso() throws Exception {
        MantencionRequest request = new MantencionRequest(null, 5L, "MAntencion A", LocalDate.now(), 20);

        when(service.crearMantencion(any())).thenReturn(mantencion);

        mockMvc.perform(post("/api/proyectos/mantencion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Mantencion A"));
    }

    @Test
    void crearMantencion_errorAsignacion() throws Exception {
        MantencionRequest request = new MantencionRequest(null, 5L, "MAntencion A", LocalDate.now(), 20);

        when(service.crearMantencion(any())).thenThrow(new RuntimeException("Error asignando recursos"));

        mockMvc.perform(post("/api/proyectos/mantencion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error asignando recursos")));
    }

    @Test
    void listarProyectos() throws Exception {
        when(service.listarProyectos()).thenReturn(List.of(proyecto));

        mockMvc.perform(get("/api/proyectos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Proyecto A"));
    }

    @Test
    void listarMantenciones() throws Exception {
        when(service.listarMantenciones()).thenReturn(List.of(mantencion));

        mockMvc.perform(get("/api/proyectos/mantenciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mantencion A"));
    }

    @Test
    void obtenerProyecto_existe() throws Exception {
        when(service.obtenerProyectoPorId(1L)).thenReturn(Optional.of(proyecto));

        mockMvc.perform(get("/api/proyectos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Proyecto A"));
    }

    @Test
    void obtenerProyecto_noExiste() throws Exception {
        when(service.obtenerProyectoPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/proyectos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerMantencion_existe() throws Exception {
        when(service.obtenerMantencionPorId(1L)).thenReturn(Optional.of(mantencion));

        mockMvc.perform(get("/api/proyectos/mantencion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Mantencion A"));
    }

    @Test
    void obtenerMantencion_noExiste() throws Exception {
        when(service.obtenerMantencionPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/proyectos/mantencion/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarProyecto_exitoso() throws Exception {
        ProjectUpdateRequest update = new ProjectUpdateRequest("Nuevo Nombre", LocalDate.now(),
                LocalDate.now().plusDays(5));

        when(service.actualizarProyecto(eq(1L), any())).thenReturn(proyecto);

        mockMvc.perform(patch("/api/proyectos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Proyecto A"));
    }

    @Test
    void actualizarProyecto_error() throws Exception {
        ProjectUpdateRequest update = new ProjectUpdateRequest("Nuevo Nombre", LocalDate.now(),
                LocalDate.now().plusDays(5));

        when(service.actualizarProyecto(eq(1L), any())).thenThrow(new RuntimeException("No encontrado"));

        mockMvc.perform(patch("/api/proyectos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No encontrado")));
    }

    @Test
    void cancelarProyecto_exitoso() throws Exception {
        when(service.cancelarProyecto(1L)).thenReturn(proyecto);

        mockMvc.perform(put("/api/proyectos/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Proyecto A"));
    }

    @Test
    void cancelarProyecto_noEncontrado() throws Exception {
        when(service.cancelarProyecto(1L)).thenThrow(new NoSuchElementException("Proyecto no encontrado"));

        mockMvc.perform(put("/api/proyectos/1/cancelar"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Proyecto no encontrado")));
    }

    @Test
    void cancelarProyecto_error() throws Exception {
        when(service.cancelarProyecto(1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(put("/api/proyectos/1/cancelar"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error")));
    }

    @Test
    void cancelarMantencion_exitoso() throws Exception {
        when(service.cancelarMantencion(1L)).thenReturn(mantencion);

        mockMvc.perform(put("/api/proyectos/1/cancelar-mantencion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Mantencion A"));
    }

    @Test
    void cancelarMantencion_noEncontrada() throws Exception {
        when(service.cancelarMantencion(1L)).thenThrow(new NoSuchElementException("Mantención no encontrada"));

        mockMvc.perform(put("/api/proyectos/1/cancelar-mantencion"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Mantención no encontrada")));
    }

    @Test
    void cancelarMantencion_error() throws Exception {
        when(service.cancelarMantencion(1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(put("/api/proyectos/1/cancelar-mantencion"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error")));
    }

    @Test
    void proyectosEnCurso() throws Exception {
        when(service.proyectosEnCurso()).thenReturn(List.of(proyecto));

        mockMvc.perform(get("/api/proyectos/en-curso"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Proyecto A"));
    }

    @Test
    void finalizarProyectoAnticipadamente_exitoso() throws Exception {
        when(service.finalizarProyectoAnticipadamente(1L)).thenReturn(proyecto);

        mockMvc.perform(put("/api/proyectos/1/finalizar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Proyecto A"));
    }

    @Test
    void finalizarProyectoAnticipadamente_error() throws Exception {
        when(service.finalizarProyectoAnticipadamente(1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(put("/api/proyectos/1/finalizar"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error")));
    }
}
