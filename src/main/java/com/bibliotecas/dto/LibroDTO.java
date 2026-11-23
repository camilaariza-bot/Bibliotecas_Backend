package com.bibliotecas.dto;

/**
 * Libro dentro de la respuesta de una biblioteca.
 * No incluye la biblioteca para no generar un ciclo en el JSON:
 * la biblioteca ya es el objeto "padre" que lo contiene.
 */
public record LibroDTO(
        Long id,
        String titulo,
        String autor,
        Integer anioPublicacion
) {
}
