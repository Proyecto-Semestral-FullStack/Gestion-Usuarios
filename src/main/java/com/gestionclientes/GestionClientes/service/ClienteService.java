package com.gestionclientes.GestionClientes.service;

import com.gestionclientes.GestionClientes.dto.*;
import com.gestionclientes.GestionClientes.exception.CredencialesInvalidasException;
import com.gestionclientes.GestionClientes.exception.UsuarioNoEncontradoException;
import com.gestionclientes.GestionClientes.exception.UsuarioYaExisteException;
import com.gestionclientes.GestionClientes.model.Cliente;
import com.gestionclientes.GestionClientes.repository.ClienteRepository;
import com.gestionclientes.GestionClientes.webclient.StorageClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService{
    private final ClienteRepository clienteRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final StorageClient storageClient;
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

    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto,MultipartFile archivo) {
        Cliente cliente = clienteRepository.findById(dto.getId())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setCorreo(dto.getCorreo());
        cliente.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        cliente.setRol(dto.getRol());
        cliente.setActivo(dto.getActivo());
        cliente.setImagenId(dto.getImagenId());
        if(archivo != null && !archivo.isEmpty()){
            ArchivoResponseDTO imagen = storageClient.uploadFile(archivo);
            cliente.setImagenId(imagen.getId());
        }
        return mapToDto(clienteRepository.save(cliente));
    }

    public ClienteResponseDTO guardar(ClienteRequestDTO dto, MultipartFile archivo){
        if(clienteRepository.findByCorreo(dto.getCorreo()).isPresent()){
            throw new UsuarioYaExisteException("El usuario ya está registrado");
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
        if(archivo != null && !archivo.isEmpty()){
            ArchivoResponseDTO imagen = storageClient.uploadFile(archivo);
            cliente.setImagenId(imagen.getId());
        }
        return mapToDto(clienteRepository.save(cliente));
    }

    public ClienteResponseDTO asignarImagen(Long id, MultipartFile archivo){
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        ArchivoResponseDTO imagen = storageClient.uploadFile(archivo);
        cliente.setImagenId(imagen.getId());
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
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        if (!passwordEncoder.matches(dto.getContrasena(), cliente.getContrasena())) {
            throw new CredencialesInvalidasException("Contraseña incorrecta");
        }
        String token = jwtService.generarToken(cliente.getId(), cliente.getCorreo(), cliente.getRol().toString());
        return new LoginResponseDTO(token, "Inicio de sesión exitoso");
    }

    public void eliminarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
        //clienteRepository.deleteById(id); desactivar usuario o eliminar???
    }

    public void eliminarPorCorreo(String correo){
        Cliente cliente = clienteRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
        //clienteRepository.deleteClienteByCorreo(correo); desactivar usuario o eliminar???
    }

}
