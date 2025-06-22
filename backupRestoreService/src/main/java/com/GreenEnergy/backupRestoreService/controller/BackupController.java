package com.GreenEnergy.backupRestoreService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GreenEnergy.backupRestoreService.model.Backup;
import com.GreenEnergy.backupRestoreService.service.BackupService;

@RestController
@RequestMapping("/backups")
public class BackupController {
    @Autowired
    private BackupService backupService;

    @PostMapping
    public ResponseEntity<?> crearBackup() {
        try {
            Backup backup = backupService.createBackup();
            return ResponseEntity.status(HttpStatus.CREATED).body(backup);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/restore/{filename}")
    public ResponseEntity<String> restaurarBackup(@PathVariable String filename) {
        backupService.restoreBackup(filename);
        return ResponseEntity.ok("Restauración completada: " + filename);
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<?> eliminarBackup(@PathVariable String filename) {
        boolean eliminado = backupService.deleteBackup(filename);
        if (eliminado) {
            return ResponseEntity.ok("Backup eliminado correctamente: " + filename);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El backup no existe o ya fue eliminado.");
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Backup> obtenerBackup(@PathVariable String filename) {
        Backup backup = backupService.getBackup(filename);
        if (backup != null) {
            return ResponseEntity.ok(backup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<String>> listarBackups() {
        List<String> backups = backupService.listBackupFiles();
        if (backups.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(backups);
    }

}
