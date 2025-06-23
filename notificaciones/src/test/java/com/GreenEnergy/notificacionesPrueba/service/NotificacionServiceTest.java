package com.GreenEnergy.notificacionesPrueba.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.GreenEnergy.notificaciones.model.Notificacion;
import com.GreenEnergy.notificaciones.model.Usuario;
import com.GreenEnergy.notificaciones.repository.NotificacionRepository;
import com.GreenEnergy.notificaciones.repository.UsuarioRepository;
import com.GreenEnergy.notificaciones.service.NotificacionService;

@ExtendWith(MockitoExtension.class)
public class NotificacionServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificacionService notificacionService;

    @Test
    void enviarCorreo_deberiaEnviarYGuardarNotificacion() {
        Long usuarioId = 1L;
        String asunto = "Asunto prueba";
        String mensaje = "Mensaje prueba";

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setEmail("usuario@test.com");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        notificacionService.sendEmail(usuarioId, asunto, mensaje);

        verify(usuarioRepository).findById(usuarioId);
        verify(mailSender).send(any(SimpleMailMessage.class));
        verify(notificacionRepository).save(any(Notificacion.class));
    }

    @Test
    void enviarCorreo_deberiaLanzarErrorSiUsuarioNoExiste() {
        Long usuarioId = 99L;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.sendEmail(usuarioId, "asunto", "mensaje"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("usuario no encontrado");

        verify(usuarioRepository).findById(usuarioId);
        verifyNoInteractions(mailSender);
        verifyNoInteractions(notificacionRepository);
    }

    @Test
    void enviarCorreo_deberiaLanzarErrorSiEmailEsVacio() {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setEmail("   ");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> notificacionService.sendEmail(usuarioId, "asunto", "mensaje"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("El usuario no tiene un correo registrado.");

        verify(usuarioRepository).findById(usuarioId);
        verifyNoInteractions(mailSender);
        verifyNoInteractions(notificacionRepository);
    }

    @Test
    void buscarNotificacionesPorUsuario_deberiaRetornarLista() {
        Long usuarioId = 1L;
        List<Notificacion> lista = List.of(new Notificacion());

        when(notificacionRepository.findByUsuarioId(usuarioId)).thenReturn(lista);

        List<Notificacion> resultado = notificacionService.findNotificationsByUsuarioId(usuarioId);

        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);

        verify(notificacionRepository).findByUsuarioId(usuarioId);
    }
}
