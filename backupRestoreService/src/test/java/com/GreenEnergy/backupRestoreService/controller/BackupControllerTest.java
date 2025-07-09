package com.GreenEnergy.backupRestoreService.controller;

import com.GreenEnergy.backupRestoreService.model.Backup;
import com.GreenEnergy.backupRestoreService.service.BackupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BackupController.class)
public class BackupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BackupService backupService;

    @Test
    public void crearBackup_ok() throws Exception {
        Backup dummy = new Backup(1, "backup_test.sql", new Date());
        when(backupService.createBackup()).thenReturn(dummy);

        mockMvc.perform(post("/backups"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.filename").value("backup_test.sql"));
    }

    @Test
    public void restaurarBackup_ok() throws Exception {
        mockMvc.perform(post("/backups/restore/backup_test.sql"))
                .andExpect(status().isOk())
                .andExpect(content().string("Restauración completada: backup_test.sql"));
    }

    @Test
    public void eliminarBackup_ok() throws Exception {
        when(backupService.deleteBackup("backup_test.sql")).thenReturn(true);

        mockMvc.perform(delete("/backups/backup_test.sql"))
                .andExpect(status().isOk())
                .andExpect(content().string("Backup eliminado correctamente: backup_test.sql"));
    }

    @Test
    public void obtenerBackup_ok() throws Exception {
        Backup dummy = new Backup(1, "backup_test.sql", new Date());
        when(backupService.getBackup("backup_test.sql")).thenReturn(dummy);

        mockMvc.perform(get("/backups/backup_test.sql"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filename").value("backup_test.sql"));
    }

    @Test
    void obtenerBackup_notFound() throws Exception {
        String filename = "inexistente.sql";

        when(backupService.getBackup(filename)).thenReturn(null);

        mockMvc.perform(get("/backups/" + filename))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listarBackups_ok() throws Exception {
        when(backupService.listBackupFiles()).thenReturn(Arrays.asList("b1.sql", "b2.sql"));

        mockMvc.perform(get("/backups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void listarBackups_vacio() throws Exception {
        when(backupService.listBackupFiles()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/backups"))
        .andExpect(status().isNoContent());
    }
}
