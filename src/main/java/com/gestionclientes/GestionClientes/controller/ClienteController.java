package com.gestionclientes.GestionClientes.controller;

import com.gestionclientes.GestionClientes.dto.ClienteRequestDTO;
import com.gestionclientes.GestionClientes.dto.ClienteResponseDTO;
import com.gestionclientes.GestionClientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController{

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obtenerClientes(){
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    @GetMapping("{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@RequestParam(name = "id",required = true) Long id){
        return clienteService.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("{nombre}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@RequestParam(name = "nombre",required = true) String nombre){
        return clienteService.obtenerPorNombre(nombre).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO dto){
        return ResponseEntity.status(201).body(clienteService.guardar(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(@RequestParam(name = "id",required = true) Long id, @Valid @RequestBody ClienteRequestDTO dto){
        return clienteService.actualizar(id,dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
