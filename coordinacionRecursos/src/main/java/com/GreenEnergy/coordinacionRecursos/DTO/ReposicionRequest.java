package com.GreenEnergy.coordinacionRecursos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReposicionRequest {
private String codigoMaterial;
private int cantidad;
}
