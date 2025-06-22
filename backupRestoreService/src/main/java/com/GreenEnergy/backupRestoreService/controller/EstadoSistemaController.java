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

@RestController
@RequestMapping("/sistema")
public class EstadoSistemaController {
    @Autowired
    private EstadoSistemaService estadoSistemaService;

    @PostMapping("/monitorear")
    public ResponseEntity<EstadoSistema> monitorearSistema() {
        EstadoSistema estado = estadoSistemaService.monitorearSistema();
        return ResponseEntity.ok(estado);
    }

    @GetMapping("/estados")
    public ResponseEntity<List<EstadoSistema>> listarEstados() {
        List<EstadoSistema> estados = estadoSistemaService.findAll();
        if (estados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estados);
    }

    @GetMapping("/estados/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            EstadoSistema estado = estadoSistemaService.findById(id);
            return ResponseEntity.ok(estado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

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
