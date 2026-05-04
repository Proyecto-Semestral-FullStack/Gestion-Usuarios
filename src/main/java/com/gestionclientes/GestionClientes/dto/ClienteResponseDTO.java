package com.gestionclientes.GestionClientes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String rol;
    private String contrasena;
}
