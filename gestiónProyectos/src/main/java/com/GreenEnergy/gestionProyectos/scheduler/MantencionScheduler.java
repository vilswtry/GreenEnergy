package com.GreenEnergy.gestionProyectos.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.GreenEnergy.gestionProyectos.model.Mantencion;
import com.GreenEnergy.gestionProyectos.repository.MantencionRepository;

@Configuration
public class MantencionScheduler {

    private final MantencionRepository mantencionRepository;

    public MantencionScheduler(MantencionRepository mantencionRepository) {
        this.mantencionRepository = mantencionRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void finalizarMantencionesPasadas() {
        LocalDate hoy = LocalDate.now();
        List<Mantencion> mantencionesPendientes = mantencionRepository
                .findByEstadoNotAndFechaMantencionBefore(Mantencion.EstadoMantencion.FINALIZADO, hoy);

        for (Mantencion mantencion : mantencionesPendientes) {
            mantencion.setEstado(Mantencion.EstadoMantencion.FINALIZADO);
            mantencionRepository.save(mantencion);
        }
    }
}
