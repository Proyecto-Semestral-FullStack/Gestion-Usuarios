package com.gestionclientes.GestionClientes.service;

import com.gestionclientes.GestionClientes.dto.ClienteRequestDTO;
import com.gestionclientes.GestionClientes.dto.ClienteResponseDTO;
import com.gestionclientes.GestionClientes.model.Cliente;
import com.gestionclientes.GestionClientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ClienteService{
    private final ClienteRepository clienteRepository;

    public ClienteResponseDTO mapToDto(Cliente cliente){
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getCorreo(),
                cliente.getRol(),
                "*".repeat(cliente.getContrasena().length())
        );
    }

    public List<ClienteResponseDTO> obtenerTodos(){
        return clienteRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Optional<ClienteResponseDTO> obtenerPorId(Long id){
        return clienteRepository.findById(id).map(this::mapToDto);
    }

    public Optional<ClienteResponseDTO> obtenerPorNombre(String nombre){
        return clienteRepository.findByNombreContainingIgnoreCase(nombre).map(this::mapToDto);
    }

    public ClienteResponseDTO guardar(ClienteRequestDTO dto){
        Cliente cliente = new Cliente(
                null,
                dto.getNombre(),
                dto.getCorreo(),
                dto.getRol(),
                dto.getContrasena()
        );
        return mapToDto(clienteRepository.save(cliente));
    }

    public Optional<ClienteResponseDTO> actualizar(Long id, ClienteRequestDTO dto) {
        return clienteRepository.findById(id).map(existe -> {
            Cliente cliente = clienteRepository
                    .findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Categoría NO encontrada con id: " + dto.getId()));
            existe.setNombre(dto.getNombre());
            existe.setCorreo(dto.getCorreo());
            existe.setContrasena(dto.getContrasena());
            return mapToDto(clienteRepository.save(existe));
        });
    }

    public void eliminarPorId(Long id) {
        clienteRepository.deleteById(id);
    }

    public void eliminarPorCorreo(String correo){
        clienteRepository.deleteClienteByCorreo(correo);
    }

}

