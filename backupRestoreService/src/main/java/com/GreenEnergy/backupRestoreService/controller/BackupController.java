package com.GreenEnergy.backupRestoreService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GreenEnergy.backupRestoreService.model.Backup;
import com.GreenEnergy.backupRestoreService.service.BackupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Backup Controller", description = "API para gestionar backups de la base de datos")

@RestController
@RequestMapping("/backups")
public class BackupController {
    @Autowired
    private BackupService backupService;

    @Operation(summary = "Crear un nuevo backup")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Backup creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Backup.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al crear backup")
    })
    @PostMapping
    public ResponseEntity<?> crearBackup() {
        try {
            Backup backup = backupService.createBackup();
            return ResponseEntity.status(HttpStatus.CREATED).body(backup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al crear el backup.");
        }
    }

    @Operation(summary = "Restaurar un backup existente por nombre de archivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restauración completada exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al restaurar backup")
    })
    @PostMapping("/restore/{filename}")
    public ResponseEntity<String> restaurarBackup(
            @Parameter(description = "Nombre del archivo de backup a restaurar", required = true) @PathVariable String filename) {
        try {
            backupService.restoreBackup(filename);
            return ResponseEntity.ok("Restauración completada: " + filename);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al restaurar el backup.");
        }
    }

    @Operation(summary = "Eliminar un backup por nombre de archivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Backup eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Backup no encontrado o ya eliminado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{filename}")
    public ResponseEntity<?> eliminarBackup(
            @Parameter(description = "Nombre del archivo de backup a eliminar", required = true) @PathVariable String filename) {
        try {
            boolean eliminado = backupService.deleteBackup(filename);
            if (eliminado) {
                return ResponseEntity.ok("Backup eliminado correctamente: " + filename);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El backup no existe o ya fue eliminado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al eliminar el backup.");
        }
    }

    @Operation(summary = "Obtener un backup por nombre de archivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Backup encontrado y retornado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Backup.class))),
            @ApiResponse(responseCode = "404", description = "Backup no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{filename}")
    public ResponseEntity<Backup> obtenerBackup(
            @Parameter(description = "Nombre del archivo de backup a obtener", required = true) @PathVariable String filename) {
        try {
            Backup backup = backupService.getBackup(filename);
            if (backup != null) {
                return ResponseEntity.ok(backup);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar todos los archivos de backup")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de archivos de backup retornada", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "string")))),
            @ApiResponse(responseCode = "204", description = "No hay archivos de backup disponibles"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<String>> listarBackups() {
        try {
            List<String> backups = backupService.listBackupFiles();
            if (backups.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(backups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
