package com.gestionclientes.GestionClientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40, unique = true)
    private String nombre;

    @Column(nullable = false, length = 60, unique = true)
    private String correo;

    @Column(nullable = false, length = 20)
    private String rol;

    @Column(nullable = false, length = 50)
    private String contrasena;
}
