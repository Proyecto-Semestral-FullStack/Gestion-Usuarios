package com.gestionclientes.GestionClientes.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationError(MethodArgumentNotValidException ex){
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errores.put(error.getField(),error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Map<String,String>> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex){
        Map<String, String> error = new LinkedHashMap<>();
        error.put("ERROR", ex.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<Map<String,String>> handleUsuarioYaExiste(UsuarioYaExisteException ex){
        Map<String, String> error = new LinkedHashMap<>();
        error.put("ERROR", ex.getMessage());
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String,String>> handleCreadencialesInvalidas(CredencialesInvalidasException ex){
        Map<String, String> error = new LinkedHashMap<>();
        error.put("ERROR", ex.getMessage());
        return ResponseEntity.status(401).body(error);
    }
}
