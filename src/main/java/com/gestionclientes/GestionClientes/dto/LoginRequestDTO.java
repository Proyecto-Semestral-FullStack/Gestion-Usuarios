package com.gestionclientes.GestionClientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO{

    @NotBlank(message = "El correo no puede estar en blanco")
    private String correo;

    @NotBlank(message = "La contrasena no puede estar en blanco")
    private String contrasena;
}
