package com.GreenEnergy.gestionProyectos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import com.GreenEnergy.gestionProyectos.DTO.MantencionRequest;
import com.GreenEnergy.gestionProyectos.DTO.ProjectRequest;
import com.GreenEnergy.gestionProyectos.DTO.ProjectUpdateRequest;
import com.GreenEnergy.gestionProyectos.model.*;
import com.GreenEnergy.gestionProyectos.repository.MantencionRepository;
import com.GreenEnergy.gestionProyectos.repository.ProyectoRepository;
import com.GreenEnergy.gestionProyectos.webclient.CoordinacionClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProyectoMantencionServiceTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private MantencionRepository mantencionRepository;

    @Mock
    private CoordinacionClient coordinacionClient;

    @InjectMocks
    private ProyectoMantencionService service;

    private Proyecto proyecto;
    private Mantencion mantencion;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        proyecto = new Proyecto(1L, "Proyecto A", 10, LocalDate.now(), LocalDate.now().plusDays(10),
                Proyecto.EstadoProyecto.CREADO, false);

        mantencion = new Mantencion(1L, "Mantencion A", LocalDate.now().plusDays(5), 5,
                Mantencion.EstadoMantencion.CREADO, false);
    }

    @Test
    void crearProyecto_existenteConRecursosAsignados() {
        ProjectRequest req = new ProjectRequest(null, "Proyecto A", 10, LocalDate.now(), LocalDate.now().plusDays(10));
        when(proyectoRepository.findByNombreAndFechaInicioAndFechaFin(anyString(), any(), any()))
                .thenReturn(Optional.of(proyecto));
        proyecto.setRecursosAsignados(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.crearProyecto(req));
        assertTrue(ex.getMessage().contains("Ya existe un proyecto igual con recursos asignados."));
    }

    @Test
    void crearProyecto_existenteSinRecursosAsignados_asignacionExitosa() {
        ProjectRequest req = new ProjectRequest(null, "Proyecto A", 10, LocalDate.now(), LocalDate.now().plusDays(10));
        proyecto.setRecursosAsignados(false);
        proyecto.setEstado(Proyecto.EstadoProyecto.CREADO);
        when(proyectoRepository.findByNombreAndFechaInicioAndFechaFin(anyString(), any(), any()))
                .thenReturn(Optional.of(proyecto));
        doNothing().when(coordinacionClient).asignarRecursos(any());
        when(proyectoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Proyecto result = service.crearProyecto(req);

        assertTrue(result.isRecursosAsignados());
        assertEquals(Proyecto.EstadoProyecto.EN_PROGRESO, result.getEstado());
    }

    @Test
    void crearProyecto_existenteSinRecursosAsignados_errorAsignacion() {
        ProjectRequest req = new ProjectRequest(null, "Proyecto A", 10, LocalDate.now(), LocalDate.now().plusDays(10));
        proyecto.setRecursosAsignados(false);
        proyecto.setEstado(Proyecto.EstadoProyecto.CREADO);
        when(proyectoRepository.findByNombreAndFechaInicioAndFechaFin(anyString(), any(), any()))
                .thenReturn(Optional.of(proyecto));
        doThrow(new RuntimeException("Falló asignación")).when(coordinacionClient).asignarRecursos(any());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.crearProyecto(req));
        assertTrue(ex.getMessage().contains("Reintento fallido en asignación"));
        assertEquals(Proyecto.EstadoProyecto.CREADO, proyecto.getEstado());
    }

    @Test
    void crearProyecto_nuevoProyecto_asignacionExitosa() {
        ProjectRequest req = new ProjectRequest(null, "Proyecto Nuevo", 15, LocalDate.now(),
                LocalDate.now().plusDays(15));
        when(proyectoRepository.findByNombreAndFechaInicioAndFechaFin(anyString(), any(), any()))
                .thenReturn(Optional.empty());
        when(proyectoRepository.save(any())).thenAnswer(i -> {
            Proyecto p = i.getArgument(0);
            p.setId(2L);
            return p;
        });
        doNothing().when(coordinacionClient).asignarRecursos(any());

        Proyecto result = service.crearProyecto(req);
        assertNotNull(result.getId());
        assertTrue(result.isRecursosAsignados());
        assertEquals(Proyecto.EstadoProyecto.EN_PROGRESO, result.getEstado());
    }

    @Test
    void crearProyecto_nuevoProyecto_errorAsignacion() {
        ProjectRequest req = new ProjectRequest(null, "Proyecto Nuevo", 15, LocalDate.now(),
                LocalDate.now().plusDays(15));
        when(proyectoRepository.findByNombreAndFechaInicioAndFechaFin(anyString(), any(), any()))
                .thenReturn(Optional.empty());
        when(proyectoRepository.save(any())).thenAnswer(i -> {
            Proyecto p = i.getArgument(0);
            p.setId(2L);
            return p;
        });
        doThrow(new RuntimeException("Falló asignación")).when(coordinacionClient).asignarRecursos(any());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.crearProyecto(req));
        assertTrue(ex.getMessage().contains("Error al asignar recursos"));
    }

    @Test
    void crearMantencion_existenteConRecursosAsignados() {
        MantencionRequest req = new MantencionRequest(null, "Mantencion A", LocalDate.now().plusDays(5), 5);
        when(mantencionRepository.findByNombreAndFechaMantencion(anyString(), any()))
                .thenReturn(Optional.of(mantencion));
        mantencion.setRecursosAsignados(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.crearMantencion(req));
        assertTrue(
                ex.getMessage().contains("Ya existe una mantención con ese nombre y fecha, y con recursos asignados."));
    }

    @Test
    void crearMantencion_existenteSinRecursosAsignados_asignacionExitosa() {
        MantencionRequest req = new MantencionRequest(null, "Mantencion A", LocalDate.now().plusDays(5), 5);
        mantencion.setRecursosAsignados(false);
        mantencion.setEstado(Mantencion.EstadoMantencion.CREADO);
        when(mantencionRepository.findByNombreAndFechaMantencion(anyString(), any()))
                .thenReturn(Optional.of(mantencion));
        doNothing().when(coordinacionClient).asignarRecursosMantencion(any());
        when(mantencionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Mantencion result = service.crearMantencion(req);

        assertTrue(result.isRecursosAsignados());
        assertEquals(Mantencion.EstadoMantencion.EN_PROGRESO, result.getEstado());
    }

    @Test
    void crearMantencion_existenteSinRecursosAsignados_errorAsignacion() {
        MantencionRequest req = new MantencionRequest(null, "Mantencion A", LocalDate.now().plusDays(5), 5);
        mantencion.setRecursosAsignados(false);
        mantencion.setEstado(Mantencion.EstadoMantencion.CREADO);
        when(mantencionRepository.findByNombreAndFechaMantencion(anyString(), any()))
                .thenReturn(Optional.of(mantencion));
        doThrow(new RuntimeException("Falló asignación")).when(coordinacionClient).asignarRecursosMantencion(any());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.crearMantencion(req));
        assertTrue(ex.getMessage().contains("Reintento fallido en asignación"));
        assertEquals(Mantencion.EstadoMantencion.CREADO, mantencion.getEstado());
    }

    @Test
    void crearMantencion_nuevaMantencion_asignacionExitosa() {
        MantencionRequest req = new MantencionRequest(null, "Mantencion Nueva", LocalDate.now().plusDays(7), 7);
        when(mantencionRepository.findByNombreAndFechaMantencion(anyString(), any()))
                .thenReturn(Optional.empty());
        when(mantencionRepository.save(any())).thenAnswer(i -> {
            Mantencion m = i.getArgument(0);
            m.setId(2L);
            return m;
        });
        doNothing().when(coordinacionClient).asignarRecursosMantencion(any());

        Mantencion result = service.crearMantencion(req);
        assertNotNull(result.getId());
        assertTrue(result.isRecursosAsignados());
        assertEquals(Mantencion.EstadoMantencion.EN_PROGRESO, result.getEstado());
    }

    @Test
    void crearMantencion_nuevaMantencion_errorAsignacion() {
        MantencionRequest req = new MantencionRequest(null, "Mantencion Nueva", LocalDate.now().plusDays(7), 7);
        when(mantencionRepository.findByNombreAndFechaMantencion(anyString(), any()))
                .thenReturn(Optional.empty());
        when(mantencionRepository.save(any())).thenAnswer(i -> {
            Mantencion m = i.getArgument(0);
            m.setId(2L);
            return m;
        });
        doThrow(new RuntimeException("Falló asignación")).when(coordinacionClient).asignarRecursosMantencion(any());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.crearMantencion(req));
        assertTrue(ex.getMessage().contains("Error al asignar recursos"));
    }

    @Test
    void listarProyectos() {
        when(proyectoRepository.findAll()).thenReturn(List.of(proyecto));
        List<Proyecto> result = service.listarProyectos();
        assertFalse(result.isEmpty());
    }

    @Test
    void listarMantenciones() {
        when(mantencionRepository.findAll()).thenReturn(List.of(mantencion));
        List<Mantencion> result = service.listarMantenciones();
        assertFalse(result.isEmpty());
    }

    @Test
    void obtenerProyectoPorId_existe() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        Optional<Proyecto> opt = service.obtenerProyectoPorId(1L);
        assertTrue(opt.isPresent());
        assertEquals(proyecto, opt.get());
    }

    @Test
    void obtenerProyectoPorId_noExiste() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Proyecto> opt = service.obtenerProyectoPorId(1L);
        assertFalse(opt.isPresent());
    }

    @Test
    void obtenerMantencionPorId_existe() {
        when(mantencionRepository.findById(1L)).thenReturn(Optional.of(mantencion));
        Optional<Mantencion> opt = service.obtenerMantencionPorId(1L);
        assertTrue(opt.isPresent());
        assertEquals(mantencion, opt.get());
    }

    @Test
    void obtenerMantencionPorId_noExiste() {
        when(mantencionRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Mantencion> opt = service.obtenerMantencionPorId(1L);
        assertFalse(opt.isPresent());
    }

    @Test
    void actualizarProyecto_exitoso() {
        ProjectUpdateRequest update = new ProjectUpdateRequest("Nuevo Proyecto", LocalDate.now(),
                LocalDate.now().plusDays(5));
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(proyectoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Proyecto actualizado = service.actualizarProyecto(1L, update);
        assertEquals("Nuevo Proyecto", actualizado.getNombre());
    }

    @Test
    void actualizarProyecto_noEncontrado() {
        ProjectUpdateRequest update = new ProjectUpdateRequest("Nuevo Proyecto", LocalDate.now(),
                LocalDate.now().plusDays(5));
        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.actualizarProyecto(1L, update));
        assertTrue(ex.getMessage().contains("no encontrado") || ex.getMessage().contains("Proyecto no encontrado"));
    }

    @Test
    void cancelarProyecto_exitoso() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        doNothing().when(coordinacionClient).devolverMaterialesProyecto(1L);
        when(proyectoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Proyecto cancelado = service.cancelarProyecto(1L);
        assertEquals(Proyecto.EstadoProyecto.CANCELADO, cancelado.getEstado());
        assertFalse(cancelado.isRecursosAsignados());
    }

    @Test
    void cancelarProyecto_errorDevolucion() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        doThrow(new RuntimeException("Error devolución")).when(coordinacionClient).devolverMaterialesProyecto(1L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.cancelarProyecto(1L));
        assertTrue(ex.getMessage().contains("Error al devolver materiales"));
    }

    @Test
    void cancelarProyecto_noEncontrado() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.cancelarProyecto(1L));
        assertTrue(ex.getMessage().contains("no encontrado") || ex.getMessage().contains("Proyecto no encontrado"));
    }

    @Test
    void cancelarProyecto_yaCancelado() {
        proyecto.setEstado(Proyecto.EstadoProyecto.CANCELADO);
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.cancelarProyecto(1L));
        assertTrue(ex.getMessage().contains("ya está cancelado"));
    }

    @Test
    void cancelarMantencion_exitoso() {
        when(mantencionRepository.findById(1L)).thenReturn(Optional.of(mantencion));
        doNothing().when(coordinacionClient).devolverMaterialesMantencion(1L);
        when(mantencionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Mantencion cancelado = service.cancelarMantencion(1L);
        assertEquals(Mantencion.EstadoMantencion.CANCELADO, cancelado.getEstado());
        assertFalse(cancelado.isRecursosAsignados());
    }

    @Test
    void cancelarMantencion_errorDevolucion() {
        when(mantencionRepository.findById(1L)).thenReturn(Optional.of(mantencion));
        doThrow(new RuntimeException("Error devolución")).when(coordinacionClient).devolverMaterialesMantencion(1L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.cancelarMantencion(1L));
        assertTrue(ex.getMessage().contains("Error al devolver materiales"));
    }

    @Test
    void cancelarMantencion_yaCancelada() {
        mantencion.setEstado(Mantencion.EstadoMantencion.CANCELADO);
        when(mantencionRepository.findById(1L)).thenReturn(Optional.of(mantencion));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.cancelarMantencion(1L));
        assertTrue(ex.getMessage().contains("ya está cancelada"));
    }

    @Test
    void proyectosEnCurso() {
        when(proyectoRepository.findByEstadoInAndFechaFinAfter(anyList(), any()))
                .thenReturn(List.of(proyecto));
        List<Proyecto> enCurso = service.proyectosEnCurso();
        assertFalse(enCurso.isEmpty());
    }

    @Test
    void finalizarProyectoAnticipadamente_exitoso() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(proyectoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Proyecto finalizado = service.finalizarProyectoAnticipadamente(1L);
        assertEquals(Proyecto.EstadoProyecto.FINALIZADO, finalizado.getEstado());
        assertEquals(LocalDate.now(), finalizado.getFechaFin());
    }

    @Test
    void finalizarProyectoAnticipadamente_errorYaFinalizado() {
        proyecto.setEstado(Proyecto.EstadoProyecto.FINALIZADO);
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.finalizarProyectoAnticipadamente(1L));
        assertTrue(ex.getMessage().contains("ya está finalizado"));
    }

    @Test
    void finalizarProyectoAnticipadamente_noEncontrado() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.finalizarProyectoAnticipadamente(1L));
        assertTrue(ex.getMessage().contains("no encontrado") || ex.getMessage().contains("Proyecto no encontrado"));
    }
}
