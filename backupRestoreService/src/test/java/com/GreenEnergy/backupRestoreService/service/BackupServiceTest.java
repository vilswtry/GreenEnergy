package com.GreenEnergy.backupRestoreService.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.GreenEnergy.backupRestoreService.model.Backup;
import com.GreenEnergy.backupRestoreService.repository.BackupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


public class BackupServiceTest {

    @InjectMocks
    private BackupService backupService;

    @Mock
    private BackupRepository backupRepository;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        setPrivateField("backupDir", "backupDir");
        setPrivateField("mysqlDumpPath", "mysqldump");
        setPrivateField("mysqlRestorePath", "mysql");
        setPrivateField("mysqlUser", "user");
        setPrivateField("mysqlDb", "db");
    }

    private void setPrivateField(String fieldName, String value) throws Exception {
        Field field = BackupService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(backupService, value);
    }

    @Test
    void createBackup_exitoso() throws Exception {
        Backup b = new Backup(null, "backup_20230623.sql", new Date());

        BackupService spyService = spy(backupService);
        doReturn(b).when(spyService).createBackup();

        Backup backup = spyService.createBackup();

        assertEquals("backup_20230623.sql".substring(0, 7), backup.getFilename().substring(0, 7));
        verify(spyService).createBackup();
    }

    @Test
    void createBackup_errorProceso() {
        BackupService spyService = spy(backupService);
        doThrow(new RuntimeException("Error")).when(spyService).createBackup();

        assertThrows(RuntimeException.class, spyService::createBackup);
    }


    @Test
    void restoreBackup_archivoNoExiste() throws Exception {
        setPrivateField("backupDir", "noExiste");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            backupService.restoreBackup("noExiste.sql");
        });
        assertTrue(ex.getMessage().contains("Archivo de respaldo no encontrado"));
    }

    @Test
    void deleteBackup_archivoExiste() throws Exception {
        File temp = File.createTempFile("temp", ".sql");
        temp.deleteOnExit();
        setPrivateField("backupDir", temp.getParent());

        Backup b = new Backup(1, temp.getName(), new Date());
        when(backupRepository.findByFilename(temp.getName())).thenReturn(b);

        boolean resultado = backupService.deleteBackup(temp.getName());

        assertTrue(resultado);
        verify(backupRepository).delete(b);
    }

    @Test
    void deleteBackup_archivoNoExiste() throws Exception {
        setPrivateField("backupDir", "noExiste");
        when(backupRepository.findByFilename("fake.sql")).thenReturn(null);

        boolean resultado = backupService.deleteBackup("fake.sql");
        assertTrue(resultado);
    }

    @Test
    void getBackup_existe() {
        Backup b = new Backup(1, "backup.sql", new Date());
        when(backupRepository.findByFilename("backup.sql")).thenReturn(b);

        Backup resultado = backupService.getBackup("backup.sql");
        assertEquals("backup.sql", resultado.getFilename());
    }

    @Test
    void listBackupFiles() {
        Backup b1 = new Backup(1, "b1.sql", new Date());
        Backup b2 = new Backup(2, "b2.sql", new Date());
        when(backupRepository.findAll()).thenReturn(Arrays.asList(b1, b2));

        List<String> filenames = backupService.listBackupFiles();
        assertEquals(2, filenames.size());
        assertEquals("b1.sql", filenames.get(0));
    }
}
