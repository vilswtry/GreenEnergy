package com.GreenEnergy.backupRestoreService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GreenEnergy.backupRestoreService.model.EstadoSistema;
import com.GreenEnergy.backupRestoreService.service.EstadoSistemaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/sistema")
@Tag(name = "Estado del Sistema", description = "Operaciones relacionadas con el monitoreo y consulta del estado del sistema")
public class EstadoSistemaController {
    @Autowired
    private EstadoSistemaService estadoSistemaService;

    @Operation(summary = "Monitorear el estado actual del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del sistema retornado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoSistema.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/monitorear")
    public ResponseEntity<EstadoSistema> monitorearSistema() {
        try {
            EstadoSistema estado = estadoSistemaService.monitorearSistema();
            return ResponseEntity.ok(estado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar todos los estados monitoreados del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estados retornada correctamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstadoSistema.class)))),
            @ApiResponse(responseCode = "204", description = "No hay estados registrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estados")
    public ResponseEntity<List<EstadoSistema>> listarEstados() {
        try {
            List<EstadoSistema> estados = estadoSistemaService.findAll();
            if (estados.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(estados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener un estado específico del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoSistema.class))),
            @ApiResponse(responseCode = "404", description = "Estado no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estados/{id}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID del estado a obtener", required = true) @PathVariable Long id) {
        try {
            EstadoSistema estado = estadoSistemaService.findById(id);
            return ResponseEntity.ok(estado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estado no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @Operation(summary = "Obtener el último estado monitoreado del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Último estado retornado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoSistema.class))),
            @ApiResponse(responseCode = "404", description = "No hay estados registrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estados/ultimo")
    public ResponseEntity<EstadoSistema> obtenerUltimoEstado() {
        try {
            EstadoSistema lastStatus = estadoSistemaService.getLastStatus();
            if (lastStatus != null) {
                return ResponseEntity.ok(lastStatus);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
