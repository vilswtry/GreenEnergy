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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/proyectos")
@Tag(name = "Gestión de Proyectos", description = "Operaciones relacionadas con proyectos y mantenciones")
public class ProyectoController {

    private final ProyectoMantencionService proyectoService;

    public ProyectoController(ProyectoMantencionService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @Operation(summary = "Crear un nuevo proyecto", description = "Crea un proyecto nuevo y asigna recursos si están disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proyecto creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proyecto.class))),
            @ApiResponse(responseCode = "400", description = "No se pudo crear el proyecto o asignar recursos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crearProyecto(@RequestBody ProjectRequest request) {
        try {
            Proyecto nuevoProyecto = proyectoService.crearProyecto(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProyecto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo crear o asignar recursos al proyecto: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al procesar la solicitud.");
        }
    }

    @Operation(summary = "Crear una nueva mantención", description = "Crea una nueva mantención y asigna recursos si están disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mantención creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Mantencion.class))),
            @ApiResponse(responseCode = "400", description = "No se pudo crear la mantención o asignar recursos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/mantencion")
    public ResponseEntity<?> crearMantencion(@RequestBody MantencionRequest request) {
        try {
            Mantencion nuevaMantencion = proyectoService.crearMantencion(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMantencion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo crear o asignar recursos a la mantención: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al procesar la solicitud.");
        }
    }

    @Operation(summary = "Listar todos los proyectos", description = "Obtiene la lista completa de proyectos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de proyectos obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Proyecto.class)))),
            @ApiResponse(responseCode = "204", description = "No hay proyectos disponibles"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Proyecto>> listarProyectos() {
        try {
            List<Proyecto> proyectos = proyectoService.listarProyectos();
            if (proyectos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar todas las mantenciones", description = "Obtiene la lista completa de mantenciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de mantenciones obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Mantencion.class)))),
            @ApiResponse(responseCode = "204", description = "No hay mantenciones disponibles"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/mantenciones")
    public ResponseEntity<List<Mantencion>> listarMantenciones() {
        try {
            List<Mantencion> mantenciones = proyectoService.listarMantenciones();
            if (mantenciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(mantenciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener un proyecto por su ID", description = "Devuelve un proyecto específico según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proyecto.class))),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtenerProyecto(@PathVariable Long id) {
        try {
            return proyectoService.obtenerProyectoPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener una mantención por su ID", description = "Devuelve una mantención específica según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mantención encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Mantencion.class))),
            @ApiResponse(responseCode = "404", description = "Mantención no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/mantencion/{id}")
    public ResponseEntity<Mantencion> obtenerMantencion(@PathVariable Long id) {
        try {
            return proyectoService.obtenerMantencionPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar parcialmente un proyecto", description = "Actualiza campos específicos de un proyecto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proyecto.class))),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarProyecto(@PathVariable Long id, @RequestBody ProjectUpdateRequest request) {
        try {
            Proyecto actualizado = proyectoService.actualizarProyecto(id, request);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al procesar la solicitud.");
        }
    }

    @Operation(summary = "Cancelar un proyecto", description = "Cancela un proyecto existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto cancelado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proyecto.class))),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error al cancelar el proyecto"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al procesar la solicitud.");
        }
    }

    @Operation(summary = "Cancelar una mantención", description = "Cancela una mantención existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mantención cancelada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Mantencion.class))),
            @ApiResponse(responseCode = "404", description = "Mantención no encontrada"),
            @ApiResponse(responseCode = "400", description = "Error al cancelar la mantención"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al procesar la solicitud.");
        }
    }

    @Operation(summary = "Listar proyectos en curso", description = "Obtiene la lista de proyectos que están actualmente en curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de proyectos en curso obtenida", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Proyecto.class)))),
            @ApiResponse(responseCode = "204", description = "No hay proyectos en curso"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/en-curso")
    public ResponseEntity<List<Proyecto>> proyectosEnCurso() {
        try {
            List<Proyecto> proyectos = proyectoService.proyectosEnCurso();
            if (proyectos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Finalizar un proyecto anticipadamente", description = "Finaliza un proyecto antes de la fecha prevista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto finalizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proyecto.class))),
            @ApiResponse(responseCode = "400", description = "No se pudo finalizar el proyecto"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarProyectoAnticipadamente(@PathVariable Long id) {
        try {
            Proyecto proyectoFinalizado = proyectoService.finalizarProyectoAnticipadamente(id);
            return ResponseEntity.ok(proyectoFinalizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo finalizar el proyecto: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al procesar la solicitud.");
        }
    }

}
