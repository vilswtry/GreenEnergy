package com.GreenEnergy.gestionProyectos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GreenEnergy.gestionProyectos.DTO.MantencionRequest;
import com.GreenEnergy.gestionProyectos.DTO.ProjectRequest;
import com.GreenEnergy.gestionProyectos.DTO.ProjectUpdateRequest;
import com.GreenEnergy.gestionProyectos.model.Mantencion;
import com.GreenEnergy.gestionProyectos.model.Mantencion.EstadoMantencion;
import com.GreenEnergy.gestionProyectos.model.Proyecto;
import com.GreenEnergy.gestionProyectos.model.Proyecto.EstadoProyecto;
import com.GreenEnergy.gestionProyectos.model.Rol;
import com.GreenEnergy.gestionProyectos.model.Usuario;
import com.GreenEnergy.gestionProyectos.repository.MantencionRepository;
import com.GreenEnergy.gestionProyectos.repository.ProyectoRepository;
import com.GreenEnergy.gestionProyectos.repository.UsuarioRepository;
import com.GreenEnergy.gestionProyectos.webclient.CoordinacionClient;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ProyectoMantencionService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private MantencionRepository mantencionRepository;

    @Autowired
    private CoordinacionClient coordinacionClient;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private void validarCliente(Long clienteId) {
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + clienteId + " no existe."));

        if (cliente.getRol() != Rol.CLIENTE) {
            throw new RuntimeException("El ID proporcionado no corresponde a un cliente válido.");
        }
    }

    public Proyecto crearProyecto(ProjectRequest request) {

        validarCliente(request.getClienteId());

        Optional<Proyecto> existente = proyectoRepository.findByNombreAndFechaInicioAndFechaFin(
                request.getNombre(), request.getFechaInicio(), request.getFechaFin());

        if (existente.isPresent()) {
            Proyecto proyectoExistente = existente.get();

            if (proyectoExistente.isRecursosAsignados()) {
                throw new RuntimeException("Ya existe un proyecto igual con recursos asignados.");
            }

            try {
                request.setProyectoId(proyectoExistente.getId());
                coordinacionClient.asignarRecursos(request);
                proyectoExistente.setRecursosAsignados(true);
                proyectoExistente.setEstado(EstadoProyecto.EN_PROGRESO);
            } catch (RuntimeException e) {
                proyectoExistente.setEstado(EstadoProyecto.CREADO);
                throw new RuntimeException("Reintento fallido en asignación de recursos: " + e.getMessage());
            }

            return proyectoRepository.save(proyectoExistente);
        }

        Proyecto nuevoProyecto = new Proyecto();
        nuevoProyecto.setNombre(request.getNombre());
        nuevoProyecto.setCantidadPaneles(request.getCantidadPaneles());
        nuevoProyecto.setFechaInicio(request.getFechaInicio());
        nuevoProyecto.setFechaFin(request.getFechaFin());
        nuevoProyecto.setEstado(EstadoProyecto.CREADO);
        nuevoProyecto.setRecursosAsignados(false);
        nuevoProyecto.setClienteId(request.getClienteId());

        nuevoProyecto = proyectoRepository.save(nuevoProyecto);

        try {
            request.setProyectoId(nuevoProyecto.getId());
            coordinacionClient.asignarRecursos(request);
            nuevoProyecto.setRecursosAsignados(true);
            nuevoProyecto.setEstado(EstadoProyecto.EN_PROGRESO);
        } catch (RuntimeException e) {
            nuevoProyecto.setEstado(EstadoProyecto.CREADO);
            throw new RuntimeException("Error al asignar recursos al nuevo proyecto: " + e.getMessage());
        }

        return proyectoRepository.save(nuevoProyecto);
    }

    public Mantencion crearMantencion(MantencionRequest request) {

        validarCliente(request.getClienteId());

        Optional<Mantencion> existente = mantencionRepository.findByNombreAndFechaMantencion(request.getNombre(),
                request.getFechaMantencion());

        if (existente.isPresent()) {
            Mantencion mantencionExistente = existente.get();

            if (mantencionExistente.isRecursosAsignados()) {
                throw new RuntimeException(
                        "Ya existe una mantención con ese nombre y fecha, y con recursos asignados.");
            }

            try {
                request.setMantencionId(mantencionExistente.getId());
                coordinacionClient.asignarRecursosMantencion(request);
                mantencionExistente.setRecursosAsignados(true);
                mantencionExistente.setEstado(EstadoMantencion.EN_PROGRESO);
            } catch (RuntimeException e) {
                mantencionExistente.setEstado(EstadoMantencion.CREADO);
                throw new RuntimeException("Reintento fallido en asignación de recursos: " + e.getMessage());
            }

            return mantencionRepository.save(mantencionExistente);
        }

        Mantencion nuevaMantencion = new Mantencion();
        nuevaMantencion.setNombre(request.getNombre());
        nuevaMantencion.setFechaMantencion(request.getFechaMantencion());
        nuevaMantencion.setCantidadPaneles(request.getCantidadPaneles());
        nuevaMantencion.setEstado(EstadoMantencion.CREADO);
        nuevaMantencion.setRecursosAsignados(false);
        nuevaMantencion.setClienteId(request.getClienteId());

        nuevaMantencion = mantencionRepository.save(nuevaMantencion);

        try {
            request.setMantencionId(nuevaMantencion.getId());
            coordinacionClient.asignarRecursosMantencion(request);
            nuevaMantencion.setRecursosAsignados(true);
            nuevaMantencion.setEstado(EstadoMantencion.EN_PROGRESO);
        } catch (RuntimeException e) {
            nuevaMantencion.setEstado(EstadoMantencion.CREADO);
            throw new RuntimeException("Error al asignar recursos a la mantención: " + e.getMessage());
        }

        return mantencionRepository.save(nuevaMantencion);
    }

    public List<Proyecto> listarProyectos() {
        return proyectoRepository.findAll();
    }

    public List<Mantencion> listarMantenciones() {
        return mantencionRepository.findAll();
    }

    public Optional<Proyecto> obtenerProyectoPorId(Long id) {
        return proyectoRepository.findById(id);
    }

    public Optional<Mantencion> obtenerMantencionPorId(Long id) {
        return mantencionRepository.findById(id);
    }

    public Proyecto actualizarProyecto(Long id, ProjectUpdateRequest request) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            proyecto.setNombre(request.getNombre());
        }

        if (request.getFechaInicio() != null) {
            proyecto.setFechaInicio(request.getFechaInicio());
        }

        if (request.getFechaFin() != null) {
            proyecto.setFechaFin(request.getFechaFin());
        }

        return proyectoRepository.save(proyecto);
    }

    public Proyecto cancelarProyecto(Long proyectoId) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (proyecto.getEstado() == Proyecto.EstadoProyecto.CANCELADO) {
            throw new RuntimeException("Proyecto ya está cancelado");
        }

        try {
            coordinacionClient.devolverMaterialesProyecto(proyectoId);
        } catch (Exception e) {
            throw new RuntimeException("Error al devolver materiales en Coordinación: " + e.getMessage());
        }

        proyecto.setEstado(Proyecto.EstadoProyecto.CANCELADO);
        proyecto.setRecursosAsignados(false);

        return proyectoRepository.save(proyecto);
    }

    public Mantencion cancelarMantencion(Long mantencionId) {
        Mantencion mantencion = mantencionRepository.findById(mantencionId)
                .orElseThrow(() -> new RuntimeException("Mantención no encontrada"));

        if (mantencion.getEstado() == Mantencion.EstadoMantencion.CANCELADO) {
            throw new RuntimeException("Mantención ya está cancelada");
        }

        try {
            coordinacionClient.devolverMaterialesMantencion(mantencionId);
        } catch (Exception e) {
            throw new RuntimeException("Error al devolver materiales en Coordinación: " + e.getMessage());
        }

        mantencion.setEstado(Mantencion.EstadoMantencion.CANCELADO);
        mantencion.setRecursosAsignados(false);

        return mantencionRepository.save(mantencion);
    }

    public List<Proyecto> proyectosEnCurso() {
        LocalDate hoy = LocalDate.now();
        return proyectoRepository.findByEstadoInAndFechaFinAfter(
                List.of(Proyecto.EstadoProyecto.CREADO, Proyecto.EstadoProyecto.EN_PROGRESO), hoy);
    }

    public Proyecto finalizarProyectoAnticipadamente(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (proyecto.getEstado() == Proyecto.EstadoProyecto.FINALIZADO) {
            throw new RuntimeException("El proyecto ya está finalizado.");
        }

        proyecto.setEstado(Proyecto.EstadoProyecto.FINALIZADO);
        proyecto.setFechaFin(LocalDate.now());

        return proyectoRepository.save(proyecto);
    }

}
