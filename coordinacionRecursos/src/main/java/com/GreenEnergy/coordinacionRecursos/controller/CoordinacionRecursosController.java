package com.GreenEnergy.coordinacionRecursos.controller;

import java.util.List;
import com.GreenEnergy.coordinacionRecursos.service.CoordinacionRecursosService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GreenEnergy.coordinacionRecursos.DTO.MantencionRequest;
import com.GreenEnergy.coordinacionRecursos.DTO.ProjectRequest;
import com.GreenEnergy.coordinacionRecursos.DTO.ReposicionRequest;
import com.GreenEnergy.coordinacionRecursos.model.Material;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/coordinacion")
@Tag(name = "Coordinación de Recursos", description = "Endpoints para la asignación y gestión de materiales y técnicos")
public class CoordinacionRecursosController {

    @Autowired
    private CoordinacionRecursosService coordinacionService;

    @PostMapping("/asignar")
    @Operation(summary = "Asignar recursos a un proyecto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recursos asignados correctamente"),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas"),
            @ApiResponse(responseCode = "409", description = "Error en la asignación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> asignarRecursos(@RequestBody ProjectRequest request) {
        try {
            if (request.getFechaInicio() == null || request.getFechaFin() == null) {
                return ResponseEntity.badRequest().body("Las fechas de inicio y fin del proyecto son requeridas.");
            }
            if (request.getFechaInicio().isAfter(request.getFechaFin())) {
                return ResponseEntity.badRequest().body("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }

            coordinacionService.asignarRecursosProyecto(request);
            return ResponseEntity.ok("Recursos asignados correctamente al proyecto.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error en la asignación. " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    @PostMapping("/asignar-mantencion")
    @Operation(summary = "Asignar recursos a una mantención")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recursos de mantención asignados correctamente"),
            @ApiResponse(responseCode = "409", description = "Error en la asignación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> asignarMantencion(@RequestBody MantencionRequest request) {
        try {
            coordinacionService.asignarRecursosMantencion(request);
            return ResponseEntity.ok("Recursos de mantención asignados correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error en la asignación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    @PostMapping("/devolver/{proyectoId}")
    @Operation(summary = "Devolver materiales de un proyecto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Materiales devueltos correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al devolver materiales del proyecto"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> devolverMaterialesProyecto(@PathVariable Long proyectoId) {
        try {
            coordinacionService.devolverMaterialesProyecto(proyectoId);
            return ResponseEntity.ok("Materiales devueltos correctamente para proyecto ID: " + proyectoId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al devolver materiales del proyecto: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    @PostMapping("/devolver-mantencion/{mantencionId}")
    @Operation(summary = "Devolver materiales de una mantención")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Materiales devueltos correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al devolver materiales de la mantención"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> devolverMaterialesMantencion(@PathVariable Long mantencionId) {
        try {
            coordinacionService.devolverMaterialesMantencion(mantencionId);
            return ResponseEntity.ok("Materiales devueltos correctamente para mantención ID: " + mantencionId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al devolver materiales de la mantención: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    @GetMapping("/materiales")
    @Operation(summary = "Listar todos los materiales")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de materiales obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Material.class)))),
            @ApiResponse(responseCode = "204", description = "No hay materiales disponibles"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Material>> listarMateriales() {
        try {
            List<Material> materiales = coordinacionService.listarMateriales();
            if (materiales.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(materiales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/materiales/faltantes")
    @Operation(summary = "Obtener materiales faltantes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materiales faltantes o mensaje informativo", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Material.class))),
                    @Content(mediaType = "text/plain")
            }),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerMaterialesFaltantes() {
        try {
            List<Material> faltantes = coordinacionService.listarMaterialesFaltantes();
            if (faltantes.isEmpty()) {
                return ResponseEntity.ok("No hace falta reponer ningún material.");
            } else {
                return ResponseEntity.ok(faltantes);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    @PostMapping("/materiales/reponer")
    @Operation(summary = "Reponer stock de un material")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material repuesto correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al reponer material"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> reponerMaterial(@RequestBody ReposicionRequest request) {
        try {
            Material material = coordinacionService.reponerMaterial(request.getCodigoMaterial(), request.getCantidad());
            return ResponseEntity.ok("Material repuesto correctamente. Nuevo stock: " + material.getStock());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al reponer material: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    @GetMapping("/materiales/{codigoMaterial}")
    @Operation(summary = "Buscar material por código")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Material.class))),
            @ApiResponse(responseCode = "400", description = "Material no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigoMaterial) {
        try {
            Material material = coordinacionService.buscarMaterialPorCodigo(codigoMaterial);
            return ResponseEntity.ok(material);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

}
