package com.gestionclientes.GestionClientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClienteRequestDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    private String apellido;

    @NotBlank(message = "El correo no puede estar en blanco")
    private String correo;

    @NotBlank(message = "La contrasena no puede estar vacia")
    private String contrasena;

    @NotNull(message = "El rol no puede estar nulo")
    private Long rol;

    @NotNull(message = "La actividad no puede estar nula")
    private Boolean activo;

    private Long imagenId;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;
}
