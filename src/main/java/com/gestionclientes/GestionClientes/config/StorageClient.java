package com.gestionclientes.GestionClientes.config;


import com.gestionclientes.GestionClientes.dto.ArchivoResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface StorageClient {
    @PostMapping(value = "/api/archivos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ArchivoResponseDTO uploadFile(@RequestPart("archivo") MultipartFile archivo);
}
