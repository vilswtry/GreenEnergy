package com.GreenEnergy.gestionProyectos.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GreenEnergy.gestionProyectos.DTO.MantencionRequest;
import com.GreenEnergy.gestionProyectos.DTO.ProjectRequest;
import com.GreenEnergy.gestionProyectos.DTO.ProjectUpdateRequest;
import com.GreenEnergy.gestionProyectos.model.Mantencion;
import com.GreenEnergy.gestionProyectos.model.Proyecto;
import com.GreenEnergy.gestionProyectos.service.ProyectoMantencionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/proyectos")
@Tag(name = "Gestión de Proyectos", description = "Operaciones relacionadas con proyectos y mantenciones")
public class ProyectoController {

    private final ProyectoMantencionService proyectoService;

    public ProyectoController(ProyectoMantencionService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @Operation(summary = "Crear un nuevo proyecto")
    @PostMapping
    public ResponseEntity<?> crearProyecto(@RequestBody ProjectRequest request) {
        try {
            Proyecto nuevoProyecto = proyectoService.crearProyecto(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProyecto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo crear o asignar recursos al proyecto: " + e.getMessage());
        }
    }

    @Operation(summary = "Crear una nueva mantención")
    @PostMapping("/mantencion")
    public ResponseEntity<?> crearMantencion(@RequestBody MantencionRequest request) {
        try {
            Mantencion nuevaMantencion = proyectoService.crearMantencion(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMantencion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo crear o asignar recursos a la mantención: " + e.getMessage());
        }
    }

    @Operation(summary = "Listar todos los proyectos")
    @GetMapping
    public ResponseEntity<List<Proyecto>> listarProyectos() {
        return ResponseEntity.ok(proyectoService.listarProyectos());
    }

    @Operation(summary = "Listar todas las mantenciones")
    @GetMapping("/mantenciones")
    public ResponseEntity<List<Mantencion>> listarMantenciones() {
        return ResponseEntity.ok(proyectoService.listarMantenciones());
    }

    @Operation(summary = "Obtener un proyecto por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtenerProyecto(@PathVariable Long id) {
        return proyectoService.obtenerProyectoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener una mantención por su ID")
    @GetMapping("/mantencion/{id}")
    public ResponseEntity<Mantencion> obtenerMantencion(@PathVariable Long id) {
        return proyectoService.obtenerMantencionPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar parcialmente un proyecto")
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarProyecto(@PathVariable Long id, @RequestBody ProjectUpdateRequest request) {
        try {
            Proyecto actualizado = proyectoService.actualizarProyecto(id, request);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Cancelar un proyecto")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarProyecto(@PathVariable Long id) {
        try {
            Proyecto cancelado = proyectoService.cancelarProyecto(id);
            return ResponseEntity.ok(cancelado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Proyecto no encontrado con ID: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al cancelar proyecto: " + e.getMessage());
        }
    }

    @Operation(summary = "Cancelar una mantención")
    @PutMapping("/{id}/cancelar-mantencion")
    public ResponseEntity<?> cancelarMantencion(@PathVariable Long id) {
        try {
            Mantencion cancelado = proyectoService.cancelarMantencion(id);
            return ResponseEntity.ok(cancelado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Mantención no encontrada con ID: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al cancelar mantención: " + e.getMessage());
        }
    }

    @Operation(summary = "Listar proyectos en curso")
    @GetMapping("/en-curso")
    public ResponseEntity<List<Proyecto>> proyectosEnCurso() {
        return ResponseEntity.ok(proyectoService.proyectosEnCurso());
    }

    @Operation(summary = "Finalizar un proyecto anticipadamente")
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarProyectoAnticipadamente(@PathVariable Long id) {
        try {
            Proyecto proyectoFinalizado = proyectoService.finalizarProyectoAnticipadamente(id);
            return ResponseEntity.ok(proyectoFinalizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo finalizar el proyecto: " + e.getMessage());
        }
    }

}
