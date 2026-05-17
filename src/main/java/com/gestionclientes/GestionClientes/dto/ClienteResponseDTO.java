package com.gestionclientes.GestionClientes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private Long rol;
    private Boolean activo;
    private Long imagenId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
