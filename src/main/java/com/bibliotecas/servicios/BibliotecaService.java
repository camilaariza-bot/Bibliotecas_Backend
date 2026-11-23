package com.bibliotecas.servicios;

import com.bibliotecas.dto.BibliotecaRequestDTO;
import com.bibliotecas.dto.BibliotecaResponseDTO;
import com.bibliotecas.dto.LibroDTO;
import com.bibliotecas.entidades.Biblioteca;
import com.bibliotecas.entidades.Libro;
import com.bibliotecas.repositorios.BibliotecaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * Capa de servicio: lógica de negocio para registrar y consultar bibliotecas.
 * El controlador nunca toca el repositorio directamente.
 */
@Service
public class BibliotecaService {

    private final BibliotecaRepository bibliotecaRepository;

    public BibliotecaService(BibliotecaRepository bibliotecaRepository) {
        this.bibliotecaRepository = bibliotecaRepository;
    }

    /** Registra una nueva biblioteca (sin libros) y devuelve la biblioteca creada. */
    @Transactional
    public BibliotecaResponseDTO registrar(BibliotecaRequestDTO request) {
        Biblioteca biblioteca = new Biblioteca(
                request.nombre().trim(),
                request.direccion().trim(),
                request.ciudad().trim()
        );
        Biblioteca guardada = bibliotecaRepository.save(biblioteca);
        return toResponse(guardada);
    }

    /** Consulta todas las bibliotecas con sus libros asociados. */
    @Transactional(readOnly = true)
    public List<BibliotecaResponseDTO> listar() {
        return bibliotecaRepository.findAllConLibros()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ---------- Conversión entidad -> DTO ----------

    private BibliotecaResponseDTO toResponse(Biblioteca biblioteca) {
        List<LibroDTO> libros = biblioteca.getLibros()
                .stream()
                .sorted(Comparator.comparing(Libro::getId))
                .map(this::toLibroDTO)
                .toList();

        return new BibliotecaResponseDTO(
                biblioteca.getId(),
                biblioteca.getNombre(),
                biblioteca.getDireccion(),
                biblioteca.getCiudad(),
                libros
        );
    }

    private LibroDTO toLibroDTO(Libro libro) {
        return new LibroDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getAnioPublicacion()
        );
    }
}
