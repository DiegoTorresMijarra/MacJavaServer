package com.example.macjava.utils.pagination;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Clase que genera encabezados de enlace de paginación segun el estandar http
 */
@Component
public class PaginationLinksUtils {
    /**
     * Crea el encabezado de paginacion
     * @param page Objeto page que contiene la información de la paginación
     * @param uriBuilder Constructor de uriComponentBuilder para construir la url
     * @return Cadena con el encabezado de paginación
     */
    public String createLinkHeader(Page<?> page, UriComponentsBuilder uriBuilder) {
        final StringBuilder linkHeader = new StringBuilder();

        if (page.hasNext()) {
            String uri = constructUri(page.getNumber() + 1, page.getSize(), uriBuilder);
            linkHeader.append(buildLinkHeader(uri, "next"));
        }

        if (page.hasPrevious()) {
            String uri = constructUri(page.getNumber() - 1, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "prev"));
        }

        if (!page.isFirst()) {
            String uri = constructUri(0, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "first"));
        }

        if (!page.isLast()) {
            String uri = constructUri(page.getTotalPages() - 1, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "last"));
        }


        return linkHeader.toString();
    }

    /**
     * Construye la url de la paginación
     * @param newPageNumber numero de la página
     * @param size tamaño de la página
     * @param uriBuilder controlador de UriComponentBuilder
     * @return Cadena con la url de la paginación
     */
    private String constructUri(int newPageNumber, int size, UriComponentsBuilder uriBuilder) {
        return uriBuilder.replaceQueryParam("page", newPageNumber).replaceQueryParam("size", size).build().encode().toUriString();
    }

    /**
     * Construye el link de la paginación
     * @param uri url de la página
     * @param rel relación de la página
     * @return
     */
    private String buildLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    /**
     * Añade una coma la encabezado si ya contiene enlaces
     * @param linkHeader Objeto StringBuilder que contiene el encabezado del enlace
     */
    private void appendCommaIfNecessary(final StringBuilder linkHeader) {
        if (!linkHeader.isEmpty()) {
            linkHeader.append(", ");
        }
    }

}