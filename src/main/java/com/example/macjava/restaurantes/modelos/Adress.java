package com.example.macjava.restaurantes.modelos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Adress {
    private String street;
    private String number;
    private int portal;
    private int floor;
    private char door;

}
