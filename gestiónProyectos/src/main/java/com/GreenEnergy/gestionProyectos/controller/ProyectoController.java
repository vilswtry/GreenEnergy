package com.GreenEnergy.gestionProyectos.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GreenEnergy.gestionProyectos.DTO.MantencionRequest;
import com.GreenEnergy.gestionProyectos.DTO.ProjectRequest;
import com.GreenEnergy.gestionProyectos.DTO.ProjectUpdateRequest;
import com.GreenEnergy.gestionProyectos.model.Mantencion;
import com.GreenEnergy.gestionProyectos.model.Proyecto;
import com.GreenEnergy.gestionProyectos.service.ProyectoMantencionService;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoMantencionService proyectoService;

    public ProyectoController(ProyectoMantencionService proyectoService) {
        this.proyectoService = proyectoService;
    }

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

    @GetMapping
    public ResponseEntity<List<Proyecto>> listarProyectos() {
        return ResponseEntity.ok(proyectoService.listarProyectos());
    }

    @GetMapping("/mantenciones")
    public ResponseEntity<List<Mantencion>> listarMantenciones() {
        return ResponseEntity.ok(proyectoService.listarMantenciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtenerProyecto(@PathVariable Long id) {
        return proyectoService.obtenerProyectoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mantencion/{id}")
    public ResponseEntity<Mantencion> obtenerMantencion(@PathVariable Long id) {
        return proyectoService.obtenerMantencionPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarProyecto(@PathVariable Long id, @RequestBody ProjectUpdateRequest request) {
        try {
            Proyecto actualizado = proyectoService.actualizarProyecto(id, request);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

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

    @GetMapping("/en-curso")
    public ResponseEntity<List<Proyecto>> proyectosEnCurso() {
        return ResponseEntity.ok(proyectoService.proyectosEnCurso());
    }

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