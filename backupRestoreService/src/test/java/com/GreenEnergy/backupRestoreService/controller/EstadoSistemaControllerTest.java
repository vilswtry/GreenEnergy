package com.GreenEnergy.backupRestoreService.controller;

import com.GreenEnergy.backupRestoreService.model.EstadoSistema;
import com.GreenEnergy.backupRestoreService.service.EstadoSistemaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadoSistemaController.class)
public class EstadoSistemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadoSistemaService estadoSistemaService;

    @Test
    public void monitorearSistema_ok() throws Exception {
        EstadoSistema dummy = new EstadoSistema(1L, 25.0, 500L, 300L, "Sistema funcionando correctamente",
                LocalDateTime.now());
        when(estadoSistemaService.monitorearSistema()).thenReturn(dummy);

        mockMvc.perform(post("/sistema/monitorear"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoSistema").value("Sistema funcionando correctamente"));
    }

    @Test
    public void listarEstados_ok() throws Exception {
        EstadoSistema dummy = new EstadoSistema(1L, 25.0, 500L, 300L, "Sistema funcionando correctamente",
                LocalDateTime.now());
        when(estadoSistemaService.findAll()).thenReturn(Arrays.asList(dummy));

        mockMvc.perform(get("/sistema/estados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void listarEstados_vacio() throws Exception {
        when(estadoSistemaService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/sistema/estados"))
        .andExpect(status().isNoContent());
    }

    @Test
    public void obtenerPorId_ok() throws Exception {
        EstadoSistema dummy = new EstadoSistema(1L, 25.0, 500L, 300L, "Sistema funcionando correctamente",
                LocalDateTime.now());
        when(estadoSistemaService.findById(1L)).thenReturn(dummy);

        mockMvc.perform(get("/sistema/estados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void obtenerPorId_notFound() throws Exception{
        when(estadoSistemaService.findById(7L)).thenThrow(new RuntimeException("no encontrado"));

        mockMvc.perform(get("/sistema/estados/7"))
        .andExpect(status().isNotFound());

    }


    @Test
    public void obtenerUltimoEstado_ok() throws Exception {
        EstadoSistema dummy = new EstadoSistema(1L, 25.0, 500L, 300L, "Sistema funcionando correctamente",
                LocalDateTime.now());
        when(estadoSistemaService.getLastStatus()).thenReturn(dummy);

        mockMvc.perform(get("/sistema/estados/ultimo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoSistema").value("Sistema funcionando correctamente"));
    }

    @Test
    public void obtenerUltimoEstado_notFound() throws Exception {
        
        when(estadoSistemaService.getLastStatus()).thenReturn(null);
        
        mockMvc.perform(get("/sistema/estados/ultimo"))
        .andExpect(status().isNotFound());
    }
    
}
