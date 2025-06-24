package com.GreenEnergy.generacionReportes.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GreenEnergy.generacionReportes.model.Mantencion;
import com.GreenEnergy.generacionReportes.model.Proyecto;
import com.GreenEnergy.generacionReportes.model.Reporte;
import com.GreenEnergy.generacionReportes.repository.MantencionRepository;
import com.GreenEnergy.generacionReportes.repository.ProyectoRepository;
import com.GreenEnergy.generacionReportes.repository.ReporteRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private MantencionRepository mantencionRepository;

    public Reporte crearReporte(Long proyectoId, String tipo, String problemas, String retroalimentacion,
            String eficiencia) {

        Reporte reporte = new Reporte();
        reporte.setIdProyectoMantencion(proyectoId);
        reporte.setTipoProyecto(tipo.toUpperCase());

        if (tipo.equalsIgnoreCase("PROYECTO")) {
            Proyecto proyecto = proyectoRepository.findById(proyectoId)
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

            if (proyecto.getEstado() != Proyecto.EstadoProyecto.FINALIZADO) {
                throw new RuntimeException("El proyecto no está finalizado");
            }

            long duracion = Reporte.calcularDuracionEnDias(proyecto.getFechaInicio(), proyecto.getFechaFin());
            reporte.setDuracionDias(duracion);

        } else if (tipo.equalsIgnoreCase("MANTENCION")) {
            Mantencion mantencion = mantencionRepository.findById(proyectoId)
                    .orElseThrow(() -> new RuntimeException("Mantención no encontrada"));

            if (mantencion.getEstado() != Mantencion.EstadoMantencion.FINALIZADO) {
                throw new RuntimeException("La mantención no está finalizada");
            }

            reporte.setDuracionDias(1L);

        } else {
            throw new RuntimeException("Tipo inválido: debe ser 'PROYECTO' o 'MANTENCION'");
        }

        reporte.setProblemasReportados(problemas);
        reporte.setRetroalimentacionCliente(retroalimentacion);
        reporte.setEficiencia(eficiencia);

        return reporteRepository.save(reporte);
    }

    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }

    public List<Reporte> buscarReportesPorFechaCreacion(LocalDate fechaCreacion) {
        return reporteRepository.findByFechaCreacion(fechaCreacion);
    }

    public List<Reporte> buscarReportesPorProyectoId(Long proyectoId) {
        return reporteRepository.findByIdProyectoMantencion(proyectoId);
    }
}
