package com.GreenEnergy.notificacionesPrueba.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.GreenEnergy.notificacionesPrueba.model.Usuario;
import com.GreenEnergy.notificacionesPrueba.model.Notificacion;
import com.GreenEnergy.notificacionesPrueba.repository.UsuarioRepository;
import com.GreenEnergy.notificacionesPrueba.repository.NotificacionRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class NotificacionService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(Long usuarioId, String asunto, String mensaje) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("usuario no encontrado"));

        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new RuntimeException("El usuario no tiene un correo registrado.");
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(usuario.getEmail());
        mailMessage.setSubject(asunto);
        mailMessage.setText(mensaje);
        mailMessage.setFrom("GreenEnergy.atencion@gmail.com");

        mailSender.send(mailMessage);

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setAsunto(asunto);
        notificacion.setMensaje(mensaje);

        notificacionRepository.save(notificacion);
    }

    public List<Notificacion> findNotificationsByUsuarioId(Long usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    public void enviarNotificacionClienteCambioEstado(Long usuarioId, String nombreServicio, String nuevoEstado) {
        String asunto = "Actualización de estado de su servicio";
        String mensaje = String.format(
                "Estimado cliente,\n\nSu servicio \"%s\" ha cambiado de estado a: %s.\n\nGracias por confiar en nosotros.\nEquipo Green Energy.",
                nombreServicio, nuevoEstado);

        sendEmail(usuarioId, asunto, mensaje);
    }

}
