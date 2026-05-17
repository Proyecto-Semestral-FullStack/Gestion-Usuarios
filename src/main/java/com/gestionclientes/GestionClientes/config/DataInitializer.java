package com.gestionclientes.GestionClientes.config;

import com.gestionclientes.GestionClientes.model.Cliente;
import com.gestionclientes.GestionClientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner{
    private final ClienteRepository clienteRepository;

    @Override
    public void run(String... args) throws Exception {
        if(clienteRepository.count() > 0){
            log.info("DataInitializer: La BD ya tiene datos , se omite la carga inicial.");
            return;
        }
        LocalDateTime ahora = LocalDateTime.now();
        log.info(">>>DataInitializer: BD vacía detectada, insertando datos de prueba...");
        clienteRepository.save(new Cliente(null,"Javier","Rodriguez","javiR@gmail.com","Jav2000",1L,true,null,ahora,ahora));
        clienteRepository.save(new Cliente(null,"Sopaipilla69",null,"sop69@gmail.com","S6opaipill9a.",2L,true,null,ahora,ahora));
        log.info(">>> DataInitializer: usuarios insertados correctamente",clienteRepository.count());
    }
}
