package com.gestionclientes.GestionClientes.repository;

import com.gestionclientes.GestionClientes.dto.ClienteResponseDTO;
import com.gestionclientes.GestionClientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente,Long>{

    Optional<Cliente> findByNombreContainingIgnoreCase(String nombre);
    Optional<Cliente> findByCorreo(String correo);
    void deleteClienteByCorreo(String correo);
}
