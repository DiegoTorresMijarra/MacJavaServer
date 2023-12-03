package com.example.macjava.utils.pagination;

import com.example.macjava.rest.orders.models.Order;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Es un compnente de paginación standard
 * @param content Lista de elementos
 * @param totalPages Total de páginas que tiene la paginación
 * @param totalElements Total de elementos que tiene la paginación
 * @param pageSize Tamaño de la página
 * @param pageNumber Número de la página
 * @param totalPageElements Total de elementos que tiene la página
 * @param empty Muestra si la página está vacía
 * @param first Indica si es la primera página
 * @param last Indica si es la última página
 * @param sortBy Ordena por el campo que se le indique
 * @param direction Indica la dirección de la paginación
 * @param <T> Tipo de dato que se va a paginar
 */
public record PageResponse<T> (
        List<T> content,
        int totalPages,
        long totalElements,
        int pageSize,
        int pageNumber,
        int totalPageElements,
        boolean empty,
        boolean first,
        boolean last,
        String sortBy,
        String direction
){
    /**
     * Constructor que crea una instancia de la clase PageResponse
     * @param page Página que se va a paginar
     * @param sortBy Ordena por el campo que se le indique
     * @param direction Indica la dirección de la paginación
     * @param <T> Tipo de dato que se va a paginar
     * @return Devuelve una instancia de la clase PageResponse
     */
    public static <T> PageResponse<T> of(Page<T> page, String sortBy, String direction) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.getNumberOfElements(),
                page.isEmpty(),
                page.isFirst(),
                page.isLast(),
                sortBy,
                direction
        );
    }
}
