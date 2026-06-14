package com.gestionclientes.GestionClientes.config;

import com.gestionclientes.GestionClientes.model.Cliente;
import com.gestionclientes.GestionClientes.model.Rol;
import com.gestionclientes.GestionClientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ClienteRepository clienteRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(clienteRepository.count() > 0){
            log.info("DataInitializer: La BD ya tiene datos , se omite la carga inicial.");
            return;
        }

        log.info(">>>DataInitializer: BD vacía detectada, insertando datos de prueba...");
        clienteRepository.save(new Cliente(null, "Javier", "Rodriguez", "javiR@gmail.com",
                passwordEncoder.encode("Jav2000"), Rol.ADMIN, null));
        clienteRepository.save(new Cliente(null, "Sopaipilla69", null, "sop69@gmail.com",
                passwordEncoder.encode("S6opaipill9a"), Rol.COMPRADOR, null));
        clienteRepository.save(new Cliente(null, "Vendedor", "Pro", "vendedor@frikitienda.com",
                passwordEncoder.encode("Vendedor123."), Rol.VENDEDOR, null));

        log.info(">>> DataInitializer: usuarios insertados correctamente",clienteRepository.count());
    }
}