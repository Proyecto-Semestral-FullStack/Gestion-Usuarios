package com.gestionclientes.GestionClientes.controller;

import com.gestionclientes.GestionClientes.dto.ClienteRequestDTO;
import com.gestionclientes.GestionClientes.dto.ClienteResponseDTO;
import com.gestionclientes.GestionClientes.dto.UsuarioAuthDTO;
import com.gestionclientes.GestionClientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class ClienteController{

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obtenerClientes(){
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    @GetMapping("id/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id){
        return clienteService.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("nombre/{nombre}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorNombre(@PathVariable String nombre){
        return clienteService.obtenerPorNombre(nombre).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("correo/{correo}")  // Para que ms-auth lo use al loguearse ya que el cliente pide iniciar sesion con correo en UsuarioClient
    public ResponseEntity<ClienteResponseDTO> obtenerPorCorreo(@PathVariable String correo){
        return clienteService.obtenerPorCorreo(correo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint interno exclusivo para autenticación entre microservicios.
     *
     * Este endpoint es llamado ÚNICAMENTE por ms-auth a través de FeignClient
     * durante el proceso de login. Retorna un UsuarioAuthDTO que incluye la
     * contraseña hasheada necesaria para que ms-auth pueda validarla con BCrypt.
     *
     * Por qué existe este endpoint separado:
     * - El endpoint público /correo/{correo} retorna ClienteResponseDTO que
     *   NO incluye la contraseña por seguridad.
     * - Se necesita un canal interno que sí la incluya, pero solo accesible
     *   entre microservicios, nunca desde el exterior.
     *
     * Seguridad:
     * - El gateway bloquea cualquier petición externa a /interno/** con 401
     * - Solo Feign (comunicación interna) puede consumir este endpoint
     *
     * IMPORTANTE: No eliminar este endpoint, su eliminación rompe el login.
     * No agregar esta ruta a endpoints públicos del gateway.
     *
     * @param correo Correo del usuario que intenta autenticarse
     * @return UsuarioAuthDTO con datos mínimos necesarios para autenticación
     */
    //Para ms-usuarios y ms-auth se comuniquen de forma interna. Ya que la contraseña incluso en hash no se expone
    @GetMapping("/interno/auth/{correo}")
    public ResponseEntity<UsuarioAuthDTO> obtenerParaAuth(@PathVariable String correo) {
        return clienteService.obtenerEntidadPorCorreo(correo)
                .map(c -> ResponseEntity.ok(new UsuarioAuthDTO(
                        c.getId(),
                        c.getCorreo(),
                        c.getContrasena(),
                        c.getRol()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO dto){
        return ResponseEntity.status(201).body(clienteService.guardar(dto));
    }

    @PutMapping("id/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto){
        return clienteService.actualizar(id,dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<Void> borrarPorId(@PathVariable Long id){
        if(clienteService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        clienteService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("correo/{correo}")
    public ResponseEntity<Void> borrarPorCorreo(@PathVariable String correo){
        if(clienteService.obtenerPorCorreo(correo).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        clienteService.eliminarPorCorreo(correo);
        return ResponseEntity.noContent().build();
    }

}
