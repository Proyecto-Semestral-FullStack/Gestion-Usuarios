package com.gestionclientes.GestionClientes.controller;

import com.gestionclientes.GestionClientes.dto.ClienteRequestDTO;
import com.gestionclientes.GestionClientes.dto.ClienteResponseDTO;
import com.gestionclientes.GestionClientes.dto.LoginRequestDTO;
import com.gestionclientes.GestionClientes.dto.LoginResponseDTO;
import com.gestionclientes.GestionClientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto){
        return ResponseEntity.ok(clienteService.login(dto));
    }
}
