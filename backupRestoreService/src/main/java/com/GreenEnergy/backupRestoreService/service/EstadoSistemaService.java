package com.GreenEnergy.backupRestoreService.service;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GreenEnergy.backupRestoreService.model.EstadoSistema;
import com.GreenEnergy.backupRestoreService.repository.EstadoSistemaRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class EstadoSistemaService {
@Autowired
private EstadoSistemaRepository estadoSistemaRespository;

public EstadoSistema monitorearSistema() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        Runtime runtime = Runtime.getRuntime();
        

        long memoriaLibre = runtime.freeMemory();
        long memoriaTotal = runtime.totalMemory();
        long memoriaUsada = memoriaTotal - memoriaLibre;

        double cpu = osBean.getSystemLoadAverage();
        double cpuUsada = Math.max(0, Math.min(100, cpu * 10));

        String estadoSistema;
        if (cpuUsada > 80 || memoriaLibre < 200_000_000) {
            estadoSistema = "Alerta: posible fallo";
        } else if (cpuUsada > 60) {
            estadoSistema = "Advertencia: uso elevado";
        } else {
            estadoSistema = "Sistema funcionando correctamente";
        }

        EstadoSistema estado = new EstadoSistema();
        estado.setCpuUsada(cpuUsada);
        estado.setMemoriaLibre(memoriaLibre);
        estado.setMemoriaUsada(memoriaUsada);
        estado.setEstadoSistema(estadoSistema);
        estado.setFecha(LocalDateTime.now());

        return estadoSistemaRespository.save(estado);
    }

    public List<EstadoSistema> findAll() {
        return estadoSistemaRespository.findAll();
    }

    public EstadoSistema findById(Long id) {
        return estadoSistemaRespository.findById(id).orElseThrow(() -> new RuntimeException("Monitoreo no encontrado"));
    }

    public EstadoSistema getLastStatus() {
        return estadoSistemaRespository.findTopByOrderByFechaDesc().orElseThrow(() -> new RuntimeException("No hay monitoreos registrados"));
    }

}
