package com.bibliotecas;

import com.bibliotecas.dto.BibliotecaRequestDTO;
import com.bibliotecas.dto.BibliotecaResponseDTO;
import com.bibliotecas.repositorios.LibroRepository;
import com.bibliotecas.servicios.BibliotecaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BibliotecasBackendApplicationTests {

    @Autowired
    private BibliotecaService bibliotecaService;

    @Autowired
    private LibroRepository libroRepository;

    @Test
    void losDatosDePruebaQuedanAsociadosASuBiblioteca() {
        List<BibliotecaResponseDTO> bibliotecas = bibliotecaService.listar();

        int totalLibros = bibliotecas.stream().mapToInt(b -> b.libros().size()).sum();
        assertEquals(libroRepository.count(), totalLibros);

        BibliotecaResponseDTO central = bibliotecas.stream()
                .filter(b -> b.nombre().equals("Biblioteca Central"))
                .findFirst()
                .orElseThrow();
        assertTrue(central.libros().stream().anyMatch(l -> l.titulo().equals("Cien años de soledad")));
    }

    @Test
    void registrarBibliotecaLaDevuelveConIdYSinLibros() {
        BibliotecaResponseDTO creada = bibliotecaService.registrar(
                new BibliotecaRequestDTO("Biblioteca de Prueba", "Calle 1 # 2-3", "Tunja"));

        assertNotNull(creada.id());
        assertTrue(creada.libros().isEmpty());
    }
}
