package com.GreenEnergy.generacionReportes.controller;

import com.GreenEnergy.generacionReportes.model.Reporte;
import com.GreenEnergy.generacionReportes.service.ReporteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/reportes")
@Tag(name = "Reportes", description = "API para gestionar reportes de proyectos y mantenciones")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Operation(summary = "Crear un nuevo reporte", description = "Crea un reporte basado en un proyecto o mantención finalizada")
    @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reporte.class)))
    @ApiResponse(responseCode = "400", description = "Error en la creación del reporte")
    @PostMapping
    public ResponseEntity<?> crearReporte(
            @Parameter(description = "ID del proyecto o mantención", required = true) @RequestParam Long idProyectoMantencion,
            @Parameter(description = "Tipo de proyecto: PROYECTO o MANTENCION", required = true, example = "PROYECTO") @RequestParam String tipo,
            @Parameter(description = "Problemas reportados", required = false) @RequestParam(required = false) String problemas,
            @Parameter(description = "Retroalimentación del cliente", required = false) @RequestParam(required = false) String retroalimentacion,
            @Parameter(description = "Evaluación de eficiencia", required = false) @RequestParam(required = false) String eficiencia) {

        try {
            Reporte nuevo = reporteService.crearReporte(
                    idProyectoMantencion,
                    tipo,
                    problemas,
                    retroalimentacion,
                    eficiencia);
            return ResponseEntity.status(201).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Listar todos los reportes", description = "Obtiene la lista completa de reportes")
    @ApiResponse(responseCode = "200", description = "Lista de reportes obtenida")
    @GetMapping
    public ResponseEntity<List<Reporte>> listarTodos() {
        return ResponseEntity.ok(reporteService.listarReportes());
    }

    @Operation(summary = "Buscar reportes por fecha de creación", description = "Obtiene reportes filtrando por fecha de creación en formato YYYY-MM-DD")
    @ApiResponse(responseCode = "200", description = "Reportes encontrados")
    @ApiResponse(responseCode = "400", description = "Formato de fecha inválido")
    @ApiResponse(responseCode = "404", description = "No se encontraron reportes para la fecha indicada")
    @GetMapping("/fecha")
    public ResponseEntity<?> buscarPorFecha(
            @Parameter(description = "Fecha de creación del reporte en formato YYYY-MM-DD", example = "2025-07-01") @RequestParam String fecha) {

        try {
            LocalDate fechaCreacion = LocalDate.parse(fecha);
            List<Reporte> reportes = reporteService.buscarReportesPorFechaCreacion(fechaCreacion);

            if (reportes.isEmpty()) {
                return ResponseEntity.status(404).body("No se encontraron reportes para la fecha: " + fecha);
            }

            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Formato de fecha inválido. Usa YYYY-MM-DD.");
        }
    }

    @Operation(summary = "Buscar reportes por ID de proyecto o mantención", description = "Obtiene reportes filtrando por ID de proyecto o mantención")
    @ApiResponse(responseCode = "200", description = "Reportes encontrados")
    @ApiResponse(responseCode = "404", description = "No se encontraron reportes para el ID indicado")
    @GetMapping("/proyecto/{id}")
    public ResponseEntity<?> buscarPorProyectoId(
            @Parameter(description = "ID del proyecto o mantención", required = true) @PathVariable Long id) {

        List<Reporte> reportes = reporteService.buscarReportesPorProyectoId(id);
        if (reportes.isEmpty()) {
            return ResponseEntity.status(404).body("No se encontraron reportes para el ID: " + id);
        }
        return ResponseEntity.ok(reportes);
    }
}
