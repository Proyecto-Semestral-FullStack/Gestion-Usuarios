package com.gestionclientes.GestionClientes.client;

import com.gestionclientes.GestionClientes.dto.ArchivoResponseDTO;
import jakarta.ws.rs.core.MediaType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "ms-storage")
public interface StorageClient{
    @PostMapping(value = "api/archivos",consumes = MediaType.MULTIPART_FORM_DATA)
    ArchivoResponseDTO uploadFile(@RequestParam("archivo") MultipartFile archivo);
}
