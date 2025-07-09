package com.GreenEnergy.generacionReportes.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.temporal.ChronoUnit;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "reportes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Reporte", description = "Entidad que representa un reporte de proyecto o mantención finalizada.")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del reporte", example = "1")
    private Long id;

    @Column(name = "id_cliente", nullable = false)
    @Schema(description = "ID del cliente asociado al proyecto o mantención", example = "42")
    private Long clienteId;

    @Column(name = "id_proyecto_mantencion", nullable = false)
    @Schema(description = "ID del proyecto o mantención asociado al reporte", example = "15")
    private Long idProyectoMantencion;

    @Column(name = "tipo_proyecto", nullable = false)
    @Schema(description = "Tipo de proyecto: PROYECTO o MANTENCION", example = "PROYECTO")
    private String tipoProyecto;

    @Column(name = "duracion_dias")
    @Schema(description = "Duración del proyecto o mantención en días", example = "30")
    private Long duracionDias;

    @Column(name = "problemas_reportados", columnDefinition = "TEXT")
    @Schema(description = "Descripción de los problemas reportados durante el proyecto o mantención", example = "Problemas eléctricos leves")
    private String problemasReportados;

    @Column(name = "retroalimentacion_cliente", columnDefinition = "TEXT")
    @Schema(description = "Retroalimentación proporcionada por el cliente", example = "Muy buen servicio, puntual y eficiente")
    private String retroalimentacionCliente;

    @Column(name = "eficiencia")
    @Schema(description = "Evaluación de la eficiencia del proyecto o mantención", example = "Alta")
    private String eficiencia;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate fechaCreacion;

    public static long calcularDuracionEnDias(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null)
            return 0;
        return ChronoUnit.DAYS.between(inicio, fin) + 1;
    }

    
}
