package com.GreenEnergy.backupRestoreService.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import com.GreenEnergy.backupRestoreService.model.EstadoSistema;
import com.GreenEnergy.backupRestoreService.repository.EstadoSistemaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

public class EstadoSistemaServiceTest {

    @InjectMocks
    private EstadoSistemaService estadoSistemaService;

    @Mock
    private EstadoSistemaRepository estadoSistemaRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(estadoSistemaService, "estadoSistemaRespository", estadoSistemaRepository);
    }

    @Test
    void monitorearSistema_guardarYRetornar() {
        EstadoSistema estado = new EstadoSistema();
        estado.setCpuUsada(30.0);
        estado.setMemoriaLibre(500L);
        estado.setMemoriaUsada(1000L);
        estado.setEstadoSistema("Sistema funcionando correctamente");
        estado.setFecha(LocalDateTime.now());

        when(estadoSistemaRepository.save(any())).thenReturn(estado);

        EstadoSistema result = estadoSistemaService.monitorearSistema();

        assertEquals("Sistema funcionando correctamente", result.getEstadoSistema());
        verify(estadoSistemaRepository).save(any());
    }

    @Test
    void findAll_retornaLista() {
        EstadoSistema e1 = new EstadoSistema(1L, 10.0, 500L, 1500L, "OK", LocalDateTime.now());
        when(estadoSistemaRepository.findAll()).thenReturn(Arrays.asList(e1));

        var result = estadoSistemaService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void findById_existe() {
        EstadoSistema e = new EstadoSistema(1L, 10.0, 500L, 1500L, "OK", LocalDateTime.now());
        when(estadoSistemaRepository.findById(1L)).thenReturn(Optional.of(e));

        EstadoSistema result = estadoSistemaService.findById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_noExiste() {
        when(estadoSistemaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> estadoSistemaService.findById(1L));
        assertTrue(ex.getMessage().contains("Monitoreo no encontrado"));
    }

    @Test
    void getLastStatus_existe() {
        EstadoSistema e = new EstadoSistema(1L, 10.0, 500L, 1500L, "OK", LocalDateTime.now());
        when(estadoSistemaRepository.findTopByOrderByFechaDesc()).thenReturn(Optional.of(e));

        EstadoSistema result = estadoSistemaService.getLastStatus();
        assertEquals(1L, result.getId());
    }

    @Test
    void getLastStatus_noExiste() {
        when(estadoSistemaRepository.findTopByOrderByFechaDesc()).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> estadoSistemaService.getLastStatus());
        assertTrue(ex.getMessage().contains("No hay monitoreos registrados"));
    }
}
