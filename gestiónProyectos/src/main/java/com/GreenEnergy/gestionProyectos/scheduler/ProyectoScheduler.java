package com.GreenEnergy.gestionProyectos.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.GreenEnergy.gestionProyectos.model.Proyecto;
import com.GreenEnergy.gestionProyectos.repository.ProyectoRepository;

@Configuration
public class ProyectoScheduler {
    private final ProyectoRepository proyectoRepository;

    public ProyectoScheduler(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void finalizarProyectosPasados() {
        LocalDate hoy = LocalDate.now();
        List<Proyecto> proyectosPendientes = proyectoRepository
                .findByEstadoNotAndFechaFinBefore(Proyecto.EstadoProyecto.FINALIZADO, hoy);

        for (Proyecto proyecto : proyectosPendientes) {
            proyecto.setEstado(Proyecto.EstadoProyecto.FINALIZADO);
            proyecto.setFechaFin(hoy);
            proyectoRepository.save(proyecto);
        }
    }
}
