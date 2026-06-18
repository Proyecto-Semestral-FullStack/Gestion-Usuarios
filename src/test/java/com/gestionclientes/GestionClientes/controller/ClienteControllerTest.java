package com.gestionclientes.GestionClientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionclientes.GestionClientes.dto.ClienteRequestDTO;
import com.gestionclientes.GestionClientes.dto.ClienteResponseDTO;
import com.gestionclientes.GestionClientes.model.Rol;
import com.gestionclientes.GestionClientes.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Nota para Spring Boot 3.4+
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    @Test
    void testObtenerTodosLosUsuarios(){
        ClienteResponseDTO dto1 = new ClienteResponseDTO(1L, "Juan", "Perez", "juan@test.com", Rol.COMPRADOR, null);
        ClienteResponseDTO dto2 = new ClienteResponseDTO(2L, "Ana", "Gomez", "ana@test.com", Rol.COMPRADOR, null);
        List<ClienteResponseDTO> listaResponse = List.of(dto1, dto2);

        when(clienteService.obtenerTodos()).thenReturn(listaResponse);

        try {
            mockMvc.perform(get("/api/usuarios"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].nombre").value("Juan"))

                    .andExpect(jsonPath("$[1].id").value(2L))
                    .andExpect(jsonPath("$[1].nombre").value("Ana"));

        } catch (Exception e) {
            fail("La petición MockMvc de obtener todos falló debido a: " + e.getMessage(), e);
        }
    }

    @Test
    void testObtenerPorId(){
        Long id = 1L;
        ClienteResponseDTO response = new ClienteResponseDTO(id, "Juan", "Perez", "juan@test.com", Rol.COMPRADOR, null);
        when(clienteService.obtenerPorId(id)).thenReturn(Optional.of(response));
        try{
            mockMvc.perform(get("/api/usuarios/id/{id}",id))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.nombre").value("Juan"))
                    .andExpect(jsonPath("$.correo").value("juan@test.com"));
        }catch(Exception e){
            fail("La petición MockMvc falló debido a: " + e.getMessage(), e);
        }
    }

    @Test
    void testGuardarUsuario(){
        ClienteRequestDTO request = new ClienteRequestDTO(null,"Juan", "Perez", "juan@test.com", "password123", Rol.COMPRADOR, null);
        ClienteResponseDTO response = new ClienteResponseDTO(10L, "Juan", "Perez", "juan@test.com", Rol.COMPRADOR, null);
        when(clienteService.guardar(any(ClienteRequestDTO.class))).thenReturn(response);
        try{
            mockMvc.perform(post("/api/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(10L))
                    .andExpect(jsonPath("$.nombre").value("Juan"));
        } catch (Exception e) {
            fail("La petición MockMvc falló debido a: " + e.getMessage(), e);
        }
    }

    @Test
    void testActualizarUsuario() {
        Long id = 1L;
        ClienteRequestDTO request = new ClienteRequestDTO(null, "Luis", "Perez", "luis@test.com", "nuevaClave123", Rol.COMPRADOR, null);
        ClienteResponseDTO response = new ClienteResponseDTO(id, "Luis", "Perez", "luis@test.com", Rol.COMPRADOR, null);

        when(clienteService.actualizar(eq(id), any(ClienteRequestDTO.class))).thenReturn(Optional.of(response));

        try {
            mockMvc.perform(put("/api/usuarios/id/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.nombre").value("Luis"))
                    .andExpect(jsonPath("$.correo").value("luis@test.com"));

        } catch (Exception e) {
            fail("La petición MockMvc de actualizar falló debido a: " + e.getMessage(), e);
        }
    }

    @Test
    void testEliminarPorId() {
        Long id = 1L;
        when(clienteService.obtenerPorId(id)).thenReturn(Optional.empty());
        try {
            mockMvc.perform(delete("/api/usuarios/id/{id}", id))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("La petición MockMvc de eliminar por ID falló debido a: " + e.getMessage(), e);
        }
    }
}