package com.bibliotecas.dto;

import java.util.List;

/**
 * Respuesta de la API para una biblioteca con sus libros anidados.
 * Al usar DTOs (y no las entidades) se evitan las referencias circulares
 * Biblioteca -> Libro -> Biblioteca en el JSON.
 */
public record BibliotecaResponseDTO(
        Long id,
        String nombre,
        String direccion,
        String ciudad,
        List<LibroDTO> libros
) {
}
