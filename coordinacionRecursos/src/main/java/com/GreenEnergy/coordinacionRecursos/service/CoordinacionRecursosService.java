package com.GreenEnergy.coordinacionRecursos.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.GreenEnergy.coordinacionRecursos.DTO.MantencionRequest;
import com.GreenEnergy.coordinacionRecursos.DTO.ProjectRequest;
import com.GreenEnergy.coordinacionRecursos.model.Material;
import com.GreenEnergy.coordinacionRecursos.model.MaterialAsignadoMantencion;
import com.GreenEnergy.coordinacionRecursos.model.MaterialAsignadoProyecto;
import com.GreenEnergy.coordinacionRecursos.model.Tecnico;
import com.GreenEnergy.coordinacionRecursos.repository.MaterialAsignadoMantencionRepository;
import com.GreenEnergy.coordinacionRecursos.repository.MaterialAsignadoProyectoRepository;
import com.GreenEnergy.coordinacionRecursos.repository.MaterialRepository;
import com.GreenEnergy.coordinacionRecursos.repository.TecnicoRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class CoordinacionRecursosService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private MaterialAsignadoProyectoRepository maProyectoRepository;

    @Autowired
    private MaterialAsignadoMantencionRepository maMantencionRepository;

    public void asignarRecursosProyecto(ProjectRequest request) {
        Long proyectoId = request.getProyectoId();
        int cantPaneles = request.getCantidadPaneles();
        LocalDate fechaIni = request.getFechaInicio();
        LocalDate fechaFin = request.getFechaFin();

        Map<String, Integer> materialesRequeridos = calcularMateriales(cantPaneles);

        for (Map.Entry<String, Integer> materialReq : materialesRequeridos.entrySet()) {
            Material material = materialRepository.findByCodigoMaterial(materialReq.getKey())
                    .orElseThrow(() -> new RuntimeException("Material no encontrado: " + materialReq.getKey()));
            if (material.getStock() < materialReq.getValue()) {
                throw new RuntimeException("Stock insuficiente de: " + material.getNombreMaterial());
            }
            material.setStock(material.getStock() - materialReq.getValue());
            materialRepository.save(material);

            MaterialAsignadoProyecto asignacion = new MaterialAsignadoProyecto();
            asignacion.setProyectoId(proyectoId);
            asignacion.setCodigoMaterial(material.getCodigoMaterial());
            asignacion.setCantAsignado(materialReq.getValue());

            maProyectoRepository.save(asignacion);
        }

        asignarTecnicos(fechaIni, fechaFin);
    }

    private Map<String, Integer> calcularMateriales(int cantPaneles) {
        Map<String, Integer> materiales = new HashMap<>();
        materiales.put("PS", cantPaneles);
        materiales.put("INV", cantPaneles / 5);
        materiales.put("CBDC", cantPaneles * 2);
        materiales.put("RIA", cantPaneles);
        materiales.put("ABR", cantPaneles * 2);
        materiales.put("GCH", cantPaneles);
        materiales.put("TRN", cantPaneles * 4);
        materiales.put("PTS", cantPaneles * 2);
        materiales.put("KPE", 1);
        materiales.put("CBAC", cantPaneles * 2);
        materiales.put("TAC", cantPaneles / 5);
        materiales.put("CBT", cantPaneles);
        materiales.put("VCT", 1);
        materiales.put("CAE", cantPaneles / 5);
        materiales.put("MC4", cantPaneles * 2);
        materiales.put("CNT", cantPaneles);
        materiales.put("ETQ", 1);
        materiales.put("EPP", 2);
        materiales.put("MULT", 1);
        materiales.put("TST", 1);
        return materiales;
    }

    private Map<String, Integer> calcularMaterialesMantencion(int cantPaneles) {
        Map<String, Integer> materiales = new HashMap<>();
        materiales.put("CEP", 1);
        materiales.put("PAN", 1);
        materiales.put("AGP", 1);
        materiales.put("ETQ", 1);
        materiales.put("EPP", 2);
        materiales.put("MULT", 1);
        materiales.put("TST", 1);

        return materiales;
    }

    private void asignarTecnicos(LocalDate fechaInicio, LocalDate fechaFin) {
        List<String> especialidades = List.of("electricista", "instalador fotovoltaico",
                "instalador de estructura", "ayudante tecnico");

        List<LocalDate> fechas = fechaInicio.datesUntil(fechaFin.plusDays(1)).toList();

        for (String especialidad : especialidades) {
            for (LocalDate fecha : fechas) {
                List<Tecnico> disponibles = tecnicoRepository.findByEspecialidadAndFechaDisponible(especialidad, fecha);
                if (disponibles.isEmpty()) {
                    throw new RuntimeException("No hay suficientes técnicos disponibles de la especialidad "
                            + especialidad + " para la fecha " + fecha);
                }
                Tecnico seleccionado = disponibles.get(0);
                seleccionado.getFechasOcupadas().add(fecha);
                tecnicoRepository.save(seleccionado);
            }
        }
    }

    public void asignarRecursosMantencion(MantencionRequest request) {
        Long mantencionId = request.getMantencionId();
        LocalDate fecha = request.getFechaMantencion();
        int paneles = request.getCantidadPaneles();

        if (fecha == null || paneles <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de mantención o la cantidad de paneles por limpiar no es válida.");
        }

        Map<String, Integer> materialesRequeridos = calcularMaterialesMantencion(paneles);

        for (Map.Entry<String, Integer> materialReq : materialesRequeridos.entrySet()) {
            Material material = materialRepository.findByCodigoMaterial(materialReq.getKey())
                    .orElseThrow(() -> new RuntimeException("Material no encontrado: " + materialReq.getKey()));
            if (material.getStock() < materialReq.getValue()) {
                throw new RuntimeException("Stock insuficiente de: " + material.getNombreMaterial());
            }
            material.setStock(material.getStock() - materialReq.getValue());
            materialRepository.save(material);

            MaterialAsignadoMantencion asignacion = new MaterialAsignadoMantencion();
            asignacion.setMantencionId(mantencionId);
            asignacion.setCodigoMaterial(material.getCodigoMaterial());
            asignacion.setCantAsignado(materialReq.getValue());

            maMantencionRepository.save(asignacion);
        }

        List<Tecnico> electricistasDisponibles = tecnicoRepository.findByEspecialidadAndFechaDisponible("electricista",
                fecha);
        if (electricistasDisponibles.isEmpty()) {
            throw new RuntimeException("No hay electricistas disponibles para la fecha indicada.");
        }
        Tecnico electricista = electricistasDisponibles.get(0);
        electricista.getFechasOcupadas().add(fecha);
        tecnicoRepository.save(electricista);

        int tecnicosLimpiezaNecesarios = (int) Math.ceil(paneles / 10.0);

        List<Tecnico> limpiezaDisponibles = tecnicoRepository
                .findByEspecialidadAndFechaDisponible("limpieza de paneles", fecha);
        if (limpiezaDisponibles.size() < tecnicosLimpiezaNecesarios) {
            throw new RuntimeException("No hay suficientes técnicos de limpieza disponibles para la fecha indicada.");
        }
        for (int i = 0; i < tecnicosLimpiezaNecesarios; i++) {
            Tecnico t = limpiezaDisponibles.get(i);
            t.getFechasOcupadas().add(fecha);
            tecnicoRepository.save(t);
        }
    }

    public void devolverMaterialesProyecto(Long proyectoId) {
        List<MaterialAsignadoProyecto> asignados = maProyectoRepository.findByProyectoId(proyectoId);
        if (asignados.isEmpty()) {
            throw new RuntimeException("No se encontraron materiales asignados para el proyecto con ID: " + proyectoId);
        }

        for (MaterialAsignadoProyecto asignado : asignados) {
            Material material = materialRepository.findByCodigoMaterial(asignado.getCodigoMaterial())
                    .orElseThrow(() -> new RuntimeException("Material no encontrado: " + asignado.getCodigoMaterial()));

            material.setStock(material.getStock() + asignado.getCantAsignado());
            materialRepository.save(material);
        }
    }

    public void devolverMaterialesMantencion(Long mantencionId) {
        List<MaterialAsignadoMantencion> asignados = maMantencionRepository
                .findByMantencionId(mantencionId);
        if (asignados.isEmpty()) {
            throw new RuntimeException(
                    "No se encontraron materiales asignados para la mantención con ID: " + mantencionId);
        }

        for (MaterialAsignadoMantencion asignado : asignados) {
            Material material = materialRepository.findByCodigoMaterial(asignado.getCodigoMaterial())
                    .orElseThrow(() -> new RuntimeException("Material no encontrado: " + asignado.getCodigoMaterial()));

            material.setStock(material.getStock() + asignado.getCantAsignado());
            materialRepository.save(material);
        }
    }

    public List<Material> listarMateriales() {
        return materialRepository.findAll();
    }

    public List<Material> listarMaterialesFaltantes() {
        List<Material> materiales = materialRepository.findAll();

        return materiales.stream().filter(material -> {
            String codigo = material.getCodigoMaterial();
            int stock = material.getStock();

            return switch (codigo) {
                case "PS" -> stock < 31;
                case "INV" -> stock < 4;
                case "CBDC" -> stock < 121;
                case "RIA" -> stock < 22;
                case "ABR" -> stock < 136;
                case "GCH" -> stock < 91;
                case "TRN" -> stock < 136;
                case "PTS" -> stock < 136;
                case "KPE" -> stock < 4;
                case "CBAC" -> stock < 91;
                case "TAC" -> stock < 4;
                case "CBT" -> stock < 31;
                case "VCT" -> stock < 4;
                case "CAE" -> stock < 6;
                case "MC4" -> stock < 91;
                case "CNT" -> stock < 91;
                case "ETQ" -> stock < 4;
                case "EPP" -> stock < 4;
                case "MULT" -> stock < 4;
                case "CEP" -> stock < 10;
                case "PAN" -> stock < 10;
                case "AGP" -> stock < 61;
                case "TST" -> stock < 4;

                default -> false;
            };
        }).toList();
    }

    public Material reponerMaterial(String codigoMaterial, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reponer debe ser mayor a cero.");
        }

        Material material;
        try {
            material = buscarMaterialPorCodigo(codigoMaterial);
        } catch (RuntimeException e) {
            throw new RuntimeException("Código de material inválido: " + codigoMaterial);
        }

        int nuevoStock = material.getStock() + cantidad;
        material.setStock(nuevoStock);

        return materialRepository.save(material);
    }

    public Material buscarMaterialPorCodigo(String codigoMaterial) {
        return materialRepository.findByCodigoMaterial(codigoMaterial)
                .orElseThrow(
                        () -> new RuntimeException("Código de material inválido: " + codigoMaterial));
    }

}
