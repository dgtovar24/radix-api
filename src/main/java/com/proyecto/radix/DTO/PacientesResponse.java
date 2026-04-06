package com.proyecto.radix.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PacientesResponse {
    private Integer id;
    private String nombreCompleto;
    private Boolean activo;
}
