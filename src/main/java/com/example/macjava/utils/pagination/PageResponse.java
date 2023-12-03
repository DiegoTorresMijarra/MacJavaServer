package com.example.macjava.utils.pagination;

import com.example.macjava.rest.orders.models.Order;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Es un compnente de paginación standard
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
