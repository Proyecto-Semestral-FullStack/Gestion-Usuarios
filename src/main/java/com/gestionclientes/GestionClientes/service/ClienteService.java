package com.gestionclientes.GestionClientes.service;

import com.gestionclientes.GestionClientes.dto.ClienteRequestDTO;
import com.gestionclientes.GestionClientes.dto.ClienteResponseDTO;
import com.gestionclientes.GestionClientes.dto.LoginRequestDTO;
import com.gestionclientes.GestionClientes.dto.LoginResponseDTO;
import com.gestionclientes.GestionClientes.model.Cliente;
import com.gestionclientes.GestionClientes.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService{
    private final ClienteRepository clienteRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public ClienteResponseDTO mapToDto(Cliente cliente){
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getCorreo(),
                cliente.getRol(),
                cliente.getActivo(),
                cliente.getImagenId(),
                cliente.getFechaCreacion(),
                cliente.getFechaActualizacion()
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

    public Optional<ClienteResponseDTO> obtenerPorCorreo(String correo){
        return clienteRepository.findByCorreo(correo).map(this::mapToDto);
    }

    public Optional<ClienteResponseDTO> actualizar(Long id, ClienteRequestDTO dto) {
        return clienteRepository.findById(id).map(existe -> {
            Cliente cliente = clienteRepository
                    .findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Categoría NO encontrada con id: " + dto.getId()));
            existe.setNombre(dto.getNombre());
            existe.setApellido(dto.getApellido());
            existe.setCorreo(dto.getCorreo());
            existe.setContrasena(passwordEncoder.encode(dto.getContrasena()));
            existe.setRol(dto.getRol());
            existe.setActivo(dto.getActivo());
            existe.setImagenId(dto.getImagenId());
            return mapToDto(clienteRepository.save(existe));
        });
    }

    public ClienteResponseDTO guardar(ClienteRequestDTO dto){
        if(clienteRepository.findByCorreo(dto.getCorreo()).isPresent()){
            throw new RuntimeException("El usuario ya está registrado");
        }
        Cliente cliente = new Cliente(
                null,
                dto.getNombre(),
                dto.getApellido(),
                dto.getCorreo(),
                passwordEncoder.encode(dto.getContrasena()),
                dto.getRol(),
                dto.getActivo(),
                dto.getImagenId(),
                null,
                null
        );
        return mapToDto(clienteRepository.save(cliente));
    }

    /**public LoginResponseDTO login(LoginRequestDTO dto){
        Cliente cliente = clienteRepository.findByCorreo(dto.getCorreo()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if(!passwordEncoder.matches(dto.getContrasena(), cliente.getContrasena())){
            throw new RuntimeException("Contraseña incorrecta");
        }
        return new LoginResponseDTO("Inicio de sesión exitoso");
    }  **/

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Cliente cliente = clienteRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(dto.getContrasena(), cliente.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        String token = jwtService.generarToken(cliente.getId(), cliente.getCorreo(), cliente.getRol().toString());
        return new LoginResponseDTO(token, "Inicio de sesión exitoso");
    }

    public void eliminarPorId(Long id) {
        clienteRepository.deleteById(id);
    }

    public void eliminarPorCorreo(String correo){
        clienteRepository.deleteClienteByCorreo(correo);
    }

}
