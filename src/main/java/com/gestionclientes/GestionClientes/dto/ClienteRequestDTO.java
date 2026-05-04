package com.gestionclientes.GestionClientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClienteRequestDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "El correo no puede estar en blanco")
    private String correo;

    @NotBlank(message = "El correo no puede estar en blanco")
    private String rol;

    @NotBlank(message = "La contrasena no puede estar vacia")
    private String contrasena;
}
