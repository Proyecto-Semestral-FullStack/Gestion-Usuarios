package com.gestionclientes.GestionClientes.controller;

import com.gestionclientes.GestionClientes.dto.ClienteRequestDTO;
import com.gestionclientes.GestionClientes.dto.ClienteResponseDTO;
import com.gestionclientes.GestionClientes.dto.LoginRequestDTO;
import com.gestionclientes.GestionClientes.dto.LoginResponseDTO;
import com.gestionclientes.GestionClientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO dto){
        return ResponseEntity.status(201).body(clienteService.guardar(dto,null));
    }

    @PostMapping(path = "/{id}/imagen",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClienteResponseDTO> subirImagen(@PathVariable Long id, @RequestPart("archivo") MultipartFile archivo){
        return ResponseEntity.status(201).body(clienteService.asignarImagen(id,archivo));
    }

    @PutMapping(path = "id/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto,MultipartFile archivo){
        ClienteResponseDTO actualizado = clienteService.actualizar(id,dto,null);
        return ResponseEntity.ok(actualizado);
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto){
        return ResponseEntity.ok(clienteService.login(dto));
    }
}
