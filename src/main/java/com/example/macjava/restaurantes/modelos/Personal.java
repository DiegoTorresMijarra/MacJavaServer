package com.example.macjava.restaurantes.modelos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

//Añadi esta clase para hacer pruebas con la relacion onetomany
@Data
@AllArgsConstructor
public class Personal {
    private Long id;
    private String nombre;
}
