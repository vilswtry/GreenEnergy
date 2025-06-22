package com.GreenEnergy.notificacionesPrueba.service;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.GreenEnergy.notificacionesPrueba.model.Cliente;
import com.GreenEnergy.notificacionesPrueba.model.Notificacion;
import com.GreenEnergy.notificacionesPrueba.repository.ClienteRepository;
import com.GreenEnergy.notificacionesPrueba.repository.NotificacionRepository;




@Service
public class NotificacionService {
    
    private final ClienteRepository clienteRepository;
    private final NotificacionRepository notificacionRepository;
    private final JavaMailSender mailSender = null;

    public void sendEmail(Long clienteId, String asunto, String mensaje) {
    Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

    if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
        throw new RuntimeException("El cliente no tiene un correo registrado.");
    }

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(cliente.getEmail());
    mailMessage.setSubject(asunto);
    mailMessage.setText(mensaje);
    mailMessage.setFrom("GreenEnergy.atencion@gmail.com");

    mailSender.send(mailMessage);

    Notificacion notificacion = new Notificacion();
    notificacion.setCliente(cliente);
    notificacion.setAsunto(asunto);
    notificacion.setMensaje(mensaje);

    notificacionRepository.save(notificacion);
}

    public List<Notificacion> findNotificationsByClientId(Long clienteId) {
        return notificacionRepository.findByClienteId(clienteId);
    }


    public void enviarNotificacionCambioEstado(Long clienteId, String nombreServicio, String nuevoEstado) {
    String asunto = "Actualización de estado de su servicio";
    String mensaje = String.format(
        "Estimado cliente,\n\nSu servicio \"%s\" ha cambiado de estado a: %s.\n\nGracias por confiar en nosotros.\nEquipo Green Energy.",
        nombreServicio, nuevoEstado);

    sendEmail(clienteId, asunto, mensaje);
}

}
