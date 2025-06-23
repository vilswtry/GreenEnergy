package com.GreenEnergy.gestionUsuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Roles disponibles para los usuarios del sistema")
public enum Rol {
    @Schema(description = "Rol de cliente")
    CLIENTE,
    @Schema(description = "Rol de técnico")
    TECNICO,
    @Schema(description = "Rol de administrador")
    ADMINISTRADOR,
    @Schema(description = "Rol de técnico de soporte")
    TECNICO_SOPORTE
}
