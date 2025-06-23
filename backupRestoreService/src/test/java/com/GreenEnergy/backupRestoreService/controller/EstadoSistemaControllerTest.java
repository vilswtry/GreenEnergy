package com.GreenEnergy.backupRestoreService.controller;

import com.GreenEnergy.backupRestoreService.model.EstadoSistema;
import com.GreenEnergy.backupRestoreService.service.EstadoSistemaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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
    public void obtenerPorId_ok() throws Exception {
        EstadoSistema dummy = new EstadoSistema(1L, 25.0, 500L, 300L, "Sistema funcionando correctamente",
                LocalDateTime.now());
        when(estadoSistemaService.findById(1L)).thenReturn(dummy);

        mockMvc.perform(get("/sistema/estados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
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
}
