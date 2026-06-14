package com.gestionclientes.GestionClientes.config;

import com.gestionclientes.GestionClientes.dto.ArchivoResponseDTO;
import com.gestionclientes.GestionClientes.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class StorageClient {

    private final WebClient webClient;

    public StorageClient(@LoadBalanced WebClient.Builder webClientBuilder,
                         @Value("${storage.service.url}") String storageUrl) {
        this.webClient = webClientBuilder.baseUrl(storageUrl).build();
    }

    public ArchivoResponseDTO uploadFile(MultipartFile archivo) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("archivo", archivo.getResource())
                .header("Content-Disposition",
                        "form-data; name=archivo; filename=" + archivo.getOriginalFilename());

        try {
            return webClient.post()
                    .uri("/api/archivos")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(ArchivoResponseDTO.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al subir archivo a Storage: {}", e.getMessage());
            throw new StorageException("No se pudo subir la imagen. El servicio de almacenamiento no está disponible en este momento.");
        }
    }
}