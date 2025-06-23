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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/sistema")
@Tag(name = "Estado del Sistema", description = "Operaciones relacionadas con el monitoreo y consulta del estado del sistema")
public class EstadoSistemaController {
    @Autowired
    private EstadoSistemaService estadoSistemaService;

    @Operation(summary = "Monitorear el estado actual del sistema")
    @ApiResponse(responseCode = "200", description = "Estado del sistema retornado correctamente")
    @PostMapping("/monitorear")
    public ResponseEntity<EstadoSistema> monitorearSistema() {
        EstadoSistema estado = estadoSistemaService.monitorearSistema();
        return ResponseEntity.ok(estado);
    }

    @Operation(summary = "Listar todos los estados monitoreados del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de estados retornada correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay estados registrados")
    })
    @GetMapping("/estados")
    public ResponseEntity<List<EstadoSistema>> listarEstados() {
        List<EstadoSistema> estados = estadoSistemaService.findAll();
        if (estados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estados);
    }

    @Operation(summary = "Obtener un estado específico del sistema por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado encontrado"),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    })
    @GetMapping("/estados/{id}")
    public ResponseEntity<?> obtenerPorId(
        @Parameter(description = "ID del estado a obtener", required = true)
        @PathVariable Long id) {
        try {
            EstadoSistema estado = estadoSistemaService.findById(id);
            return ResponseEntity.ok(estado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Obtener el último estado monitoreado del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Último estado retornado correctamente"),
        @ApiResponse(responseCode = "404", description = "No hay estados registrados")
    })
    @GetMapping("/estados/ultimo")
    public ResponseEntity<EstadoSistema> obtenerUltimoEstado() {
        EstadoSistema lastStatus = estadoSistemaService.getLastStatus();
        if (lastStatus != null) {
            return ResponseEntity.ok(lastStatus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
