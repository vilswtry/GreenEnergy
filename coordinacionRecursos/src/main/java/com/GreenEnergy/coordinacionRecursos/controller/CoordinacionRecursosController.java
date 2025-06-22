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

@RestController
@RequestMapping("/api/coordinacion")
public class CoordinacionRecursosController {

    @Autowired
    private CoordinacionRecursosService coordinacionService;

    @PostMapping("/asignar")
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
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Error en la asignación. " + e.getMessage());
        }
    }

    @PostMapping("/asignar-mantencion")
    public ResponseEntity<String> asignarMantencion(@RequestBody MantencionRequest request) {
        try {
            coordinacionService.asignarRecursosMantencion(request);
            return ResponseEntity.ok("Recursos de mantención asignados correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error en la asignación: " + e.getMessage());
        }
    }

    @PostMapping("/devolver/{proyectoId}")
    public ResponseEntity<?> devolverMaterialesProyecto(@PathVariable Long proyectoId) {
        try {
            coordinacionService.devolverMaterialesProyecto(proyectoId);
            return ResponseEntity.ok("Materiales devueltos correctamente para proyecto ID: " + proyectoId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al devolver materiales del proyecto: " + e.getMessage());
        }
    }

    @PostMapping("/devolver-mantencion/{mantencionId}")
    public ResponseEntity<?> devolverMaterialesMantencion(@PathVariable Long mantencionId) {
        try {
            coordinacionService.devolverMaterialesMantencion(mantencionId);
            return ResponseEntity.ok("Materiales devueltos correctamente para mantención ID: " + mantencionId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al devolver materiales de la mantención: " + e.getMessage());
        }
    }

    @GetMapping("/materiales")
    public ResponseEntity<List<Material>> listarMateriales() {
        return ResponseEntity.ok(coordinacionService.listarMateriales());
    }

    @GetMapping("/materiales/faltantes")
    public ResponseEntity<?> obtenerMaterialesFaltantes() {
        List<Material> faltantes = coordinacionService.listarMaterialesFaltantes();
        if (faltantes.isEmpty()) {
            return ResponseEntity.ok("No hace falta reponer ningún material.");
        } else {
            return ResponseEntity.ok(faltantes);
        }
    }

    @PostMapping("/materiales/reponer")
    public ResponseEntity<?> reponerMaterial(@RequestBody ReposicionRequest request) {
        try {
            Material material = coordinacionService.reponerMaterial(request.getCodigoMaterial(), request.getCantidad());
            return ResponseEntity.ok("Material repuesto correctamente. Nuevo stock: " + material.getStock());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al reponer material: " + e.getMessage());
        }
    }

    @GetMapping("/materiales/{codigoMaterial}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigoMaterial) {
        try {
            Material material = coordinacionService.buscarMaterialPorCodigo(codigoMaterial);
            return ResponseEntity.ok(material);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}