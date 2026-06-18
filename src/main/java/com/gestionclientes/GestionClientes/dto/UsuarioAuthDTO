package com.gestionclientes.GestionClientes.dto;

import com.gestionclientes.GestionClientes.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Esto es para el tema de seguridad con la contraseña. No se debe poner en el DTO de response porque queda expuesta la contrasena y es mala practica
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioAuthDTO {
    private Long id;
    private String correo;
    private String contrasena; // ← solo para uso interno de ms-auth
    private Rol rol;

    /**
     * DTO exclusivo para autenticación interna entre microservicios.
     *
     * Este DTO es utilizado ÚNICAMENTE por ms-auth a través de Feign
     * para verificar las credenciales del usuario durante el login.
     *
     * A diferencia de ClienteResponseDTO (que es público y no expone
     * la contraseña), este DTO incluye la contraseña hasheada porque
     * ms-auth la necesita para validar con BCrypt.
     *
     * IMPORTANTE: No eliminar ni agregar este DTO a endpoints públicos.
     * Su uso debe quedar restringido al endpoint /interno/auth/{correo}.
     */
}
