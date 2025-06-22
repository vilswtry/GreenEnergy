package com.GreenEnergy.atencionClientes.controller;

import com.GreenEnergy.atencionClientes.model.Solicitud;
import com.GreenEnergy.atencionClientes.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService service;

    @PostMapping
    public ResponseEntity<Solicitud> crear(@RequestBody Solicitud s) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(s));
    }

    @GetMapping
    public ResponseEntity<List<Solicitud>> verTodas() {
        List<Solicitud> lista = service.obtenerTodas();
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(lista);
    }
}
