package com.gestionclientes.GestionClientes.dto;

import com.gestionclientes.GestionClientes.model.Rol;
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
    private Rol rol;
    private Long imagenId;
}
