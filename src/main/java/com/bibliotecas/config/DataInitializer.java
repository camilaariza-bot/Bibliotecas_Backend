package com.bibliotecas.config;

import com.bibliotecas.entidades.Biblioteca;
import com.bibliotecas.entidades.Libro;
import com.bibliotecas.repositorios.BibliotecaRepository;
import com.bibliotecas.repositorios.LibroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Carga datos de prueba al iniciar la aplicación usando los repositorios.
 * Primero se guardan las bibliotecas y luego los libros, cada uno
 * asociado a su biblioteca (llave foránea biblioteca_id).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final BibliotecaRepository bibliotecaRepository;
    private final LibroRepository libroRepository;

    public DataInitializer(BibliotecaRepository bibliotecaRepository, LibroRepository libroRepository) {
        this.bibliotecaRepository = bibliotecaRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Evita duplicar datos si la base ya tiene información
        if (bibliotecaRepository.count() > 0) {
            log.info("Datos de prueba ya existentes, no se cargan de nuevo.");
            return;
        }

        // 1) Bibliotecas
        Biblioteca central = bibliotecaRepository.save(
                new Biblioteca("Biblioteca Central", "Calle 26 # 13-15", "Bogotá"));
        Biblioteca norte = bibliotecaRepository.save(
                new Biblioteca("Biblioteca Norte", "Carrera 15 # 127-30", "Bogotá"));
        Biblioteca sur = bibliotecaRepository.save(
                new Biblioteca("Biblioteca Sur", "Avenida Boyacá # 52-10 Sur", "Bogotá"));

        // 2) Libros, cada uno con su biblioteca obligatoria
        libroRepository.saveAll(List.of(
                new Libro("Cien años de soledad", "Gabriel García Márquez", 1967, central),
                new Libro("María", "Jorge Isaacs", 1867, central),
                new Libro("El coronel no tiene quien le escriba", "Gabriel García Márquez", 1961, norte),
                new Libro("La vorágine", "José Eustasio Rivera", 1924, norte),
                new Libro("Delirio", "Laura Restrepo", 2004, sur),
                new Libro("El amor en los tiempos del cólera", "Gabriel García Márquez", 1985, sur)
        ));

        log.info("Datos de prueba cargados: {} bibliotecas y {} libros.",
                bibliotecaRepository.count(), libroRepository.count());
    }
}
