package com.GreenEnergy.notificaciones.controller;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.GreenEnergy.notificaciones.model.Notificacion;
import com.GreenEnergy.notificaciones.service.NotificacionService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacionController.class)
public class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    @Test
    void testEnviarEmail() throws Exception {
        mockMvc.perform(post("/notificaciones/enviar")
                .param("usuarioId", "1")
                .param("asunto", "Prueba")
                .param("mensaje", "Este es un mensaje"))
                .andExpect(status().isOk())
                .andExpect(content().string("Correo enviado."));

        verify(notificacionService).sendEmail(1L, "Prueba", "Este es un mensaje");
    }

    @Test
    void testEnviarEmailError() throws Exception {
        doThrow(new RuntimeException("Error en envío")).when(notificacionService)
                .sendEmail(1L, "Error", "Mensaje");

        mockMvc.perform(post("/notificaciones/enviar")
                .param("usuarioId", "1")
                .param("asunto", "Error")
                .param("mensaje", "Mensaje"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Error en envío"));
    }

    @Test
    void testObtenerNotificaciones() throws Exception {
        Notificacion mockNoti = new Notificacion();
        mockNoti.setId(1L);
        when(notificacionService.findNotificationsByUsuarioId(1L))
                .thenReturn(List.of(mockNoti));

        mockMvc.perform(get("/notificaciones/cliente/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerNotificaciones_listaVacia() throws Exception {
        when(notificacionService.findNotificationsByUsuarioId(1L)).thenReturn(List.of());
        
        mockMvc.perform(get("/notificaciones/cliente/1"))
            .andExpect(status().isNoContent());
}

    @Test
    void testNotificarCambioEstado() throws Exception {
        mockMvc.perform(post("/notificaciones/estado")
                .param("usuarioId", "1")
                .param("nombreServicio", "Instalación")
                .param("nuevoEstado", "FINALIZADO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notificación enviada con éxito."));

        verify(notificacionService)
                .enviarNotificacionClienteCambioEstado(1L, "Instalación", "FINALIZADO");
    }

    @Test
    void testNotificarCambioEstadoError() throws Exception {
        doThrow(new RuntimeException("Error de estado")).when(notificacionService)
                .enviarNotificacionClienteCambioEstado(anyLong(), anyString(), anyString());

        mockMvc.perform(post("/notificaciones/estado")
                .param("usuarioId", "1")
                .param("nombreServicio", "Mantención")
                .param("nuevoEstado", "ERROR"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al enviar notificación: Error de estado"));
    }
}
