package com.GreenEnergy.backupRestoreService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.backupRestoreService.model.Backup;

@Repository
public interface BackupRepository extends JpaRepository<Backup, Integer> {
    
Backup findByFilename(String filename);


}
