package com.GreenEnergy.notificacionesPrueba.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.GreenEnergy.notificacionesPrueba.model.Notificacion;
import com.GreenEnergy.notificacionesPrueba.service.NotificacionService;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarEmail(
            @RequestParam Long clienteId,
            @RequestParam String asunto,
            @RequestParam String mensaje) {
        try {
            notificacionService.sendEmail(clienteId, asunto, mensaje);
            return ResponseEntity.ok("Correo enviado.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Notificacion>> obtenerNotificaciones(@PathVariable Long clienteId) {
        return ResponseEntity.ok(notificacionService.findNotificationsByClientId(clienteId));
    }

    @PostMapping("/estado")
    public ResponseEntity<String> notificarCambioEstado(@RequestParam Long clienteId,
            @RequestParam String nombreServicio,
            @RequestParam String nuevoEstado) {
        try {
            notificacionService.enviarNotificacionCambioEstado(clienteId, nombreServicio, nuevoEstado);
            return ResponseEntity.ok("Notificación enviada con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al enviar notificación: " + e.getMessage());
        }
    }
}
