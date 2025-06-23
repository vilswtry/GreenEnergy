package com.GreenEnergy.backupRestoreService.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.GreenEnergy.backupRestoreService.model.Backup;
import com.GreenEnergy.backupRestoreService.repository.BackupRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class BackupService {

    @Autowired
    private BackupRepository backupRepository;

    @Value("${backup.dir}")
    private String backupDir;

    @Value("${mysql.path}")
    private String mysqlDumpPath;

    @Value("${mysql.restore.path}")
    private String mysqlRestorePath;

    @Value("${mysql.user}")
    private String mysqlUser;

    @Value("${mysql.db}")
    private String mysqlDb;

    public Backup createBackup() {
        try {

            new File(backupDir).mkdirs();

            String fechaCreacion = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "backup_" + fechaCreacion + ".sql";

            File backupFile = new File(backupDir, filename);

            ProcessBuilder pb = new ProcessBuilder(
                    mysqlDumpPath, "-u", mysqlUser, mysqlDb);
            pb.redirectOutput(backupFile);

            Process process = pb.start();

            int resultadoProceso = process.waitFor();
            if (resultadoProceso != 0) {
                throw new RuntimeException("Error al crear el respaldo, código de salida: " + resultadoProceso);
            }

            Backup backup = new Backup(null, filename, new Date());
            return backupRepository.save(backup);

        } catch (Exception e) {
            throw new RuntimeException("Error durante la creación del respaldo", e);
        }
    }

    public void restoreBackup(String filename) {
        File restoreFile = new File(backupDir, filename);
        if (!restoreFile.exists()) {
            throw new RuntimeException("Archivo de respaldo no encontrado: " + filename);
        }
        try {
            ProcessBuilder pb = new ProcessBuilder(mysqlRestorePath, "-u", mysqlUser, mysqlDb);
            pb.redirectInput(restoreFile);
            Process proceso = pb.start();

            int resultadoProceso = proceso.waitFor();
            if (resultadoProceso != 0) {
                throw new RuntimeException("Error al restaurar el respaldo, código de salida: " + resultadoProceso);
            }

            System.out.println("Base de datos restaurada exitosamente. Archivo fuente: " + filename);

        } catch (Exception e) {
            throw new RuntimeException("Error durante la restauración del respaldo", e);
        }
    }

    public boolean deleteBackup(String filename) {
        File backupFile = new File(backupDir, filename);

        Backup backup = backupRepository.findByFilename(filename);
        if (backup != null) {
            backupRepository.delete(backup);
        }
        if (backupFile.exists()) {
            return backupFile.delete();
        }
        return true;
    }

    public Backup getBackup(String filename) {
        return backupRepository.findByFilename(filename);
    }

    public List<String> listBackupFiles() {
        return backupRepository.findAll().stream().map(Backup::getFilename).collect(Collectors.toList());
    }

}
