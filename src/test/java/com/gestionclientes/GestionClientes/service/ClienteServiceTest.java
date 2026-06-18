package com.gestionclientes.GestionClientes.service;

import com.gestionclientes.GestionClientes.dto.ClienteRequestDTO;
import com.gestionclientes.GestionClientes.dto.ClienteResponseDTO;
import com.gestionclientes.GestionClientes.exception.UsuarioYaExisteException;
import com.gestionclientes.GestionClientes.model.Cliente;
import com.gestionclientes.GestionClientes.model.Rol;
import com.gestionclientes.GestionClientes.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void testObtenerTodosLosUsuarios() {
        Cliente cliente1 = new Cliente(1L, "Juan", "Perez", "juan@test.com", "pass", Rol.COMPRADOR, null);
        Cliente cliente2 = new Cliente(2L, "Ana", "Gomez", "ana@test.com", "pass", Rol.COMPRADOR, null);
        List<Cliente> listaClientes = List.of(cliente1, cliente2);

        when(clienteRepository.findAll()).thenReturn(listaClientes);
        List<ClienteResponseDTO> resultado = clienteService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        assertEquals("Ana", resultado.get(1).getNombre());

        verify(clienteRepository, times(1)).findAll();
    }
    @Test
    void testObtenerPorId(){
        Long clienteId=1L;
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setCorreo("juan@test.com");
        cliente.setContrasena("password123");
        cliente.setRol(Rol.COMPRADOR);
        cliente.setImagenId(null);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        Optional<ClienteResponseDTO> resultado = clienteService.obtenerPorId(clienteId);

        assertTrue(resultado.isPresent());
        assertEquals("Juan",resultado.get().getNombre());
        assertEquals("juan@test.com",resultado.get().getCorreo());

        verify(clienteRepository, times(1)).findById(clienteId);
    }

    @Test
    void testGuardarUsuario(){
        ClienteRequestDTO dto =
                new ClienteRequestDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setCorreo("juan@test.com");
        dto.setContrasena("password123");
        dto.setRol(Rol.COMPRADOR);
        dto.setImagenId(null);

        when(clienteRepository.findByCorreo(dto.getCorreo())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getContrasena())).thenReturn("HACE HASH");
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocacion ->{
            Cliente clienteGuardado = invocacion.getArgument(0);
            clienteGuardado.setId(10L);
            return clienteGuardado;
        });
        ClienteResponseDTO resultado = clienteService.guardar(dto);
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("Juan",resultado.getNombre());
        assertEquals("juan@test.com",resultado.getCorreo());

        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(passwordEncoder, times(1)).encode(dto.getContrasena());
    }

    @Test
    void testLanzarExcepcionUsuarioExistente(){
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setCorreo("comprador@test.com");
        Cliente cliente = new Cliente();

        when(clienteRepository.findByCorreo(dto.getCorreo())).thenReturn(Optional.of(cliente));
        assertThrows(UsuarioYaExisteException.class,() -> clienteService.guardar(dto));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testActualizarUsuario(){
        Long clienteId = 1L;
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setCorreo("juan@test.com");
        dto.setContrasena("password123");
        dto.setRol(Rol.COMPRADOR);
        dto.setImagenId(null);

        Cliente clienteExistente = new Cliente(clienteId, "Juan", "Perez", "juan@test.com", "claveVieja", Rol.COMPRADOR, null);
        Cliente clienteActualizado = new Cliente(clienteId, "Luis", "Perez", "luis@test.com", "hashNuevaClave", Rol.COMPRADOR, null);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));
        when(passwordEncoder.encode("password123")).thenReturn("hashNuevaClave");
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);

        Optional<ClienteResponseDTO> resultado = clienteService.actualizar(clienteId, dto);

        assertTrue(resultado.isPresent());
        assertEquals("Luis", resultado.get().getNombre());
        assertEquals("luis@test.com", resultado.get().getCorreo());

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testEliminarUsuarioPorId(){
        Long clienteId = 1L;
        clienteService.eliminarPorId(clienteId);
        verify(clienteRepository, times(1)).deleteById(clienteId);
    }
}