package com.bibliotecas.controladores;

import com.bibliotecas.dto.BibliotecaRequestDTO;
import com.bibliotecas.dto.BibliotecaResponseDTO;
import com.bibliotecas.servicios.BibliotecaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST de Biblioteca.
 *  POST /bibliotecas -> registrar biblioteca
 *  GET  /bibliotecas -> consultar bibliotecas con sus libros
 */
@RestController
@RequestMapping("/bibliotecas")
public class BibliotecaController {

    private final BibliotecaService bibliotecaService;

    public BibliotecaController(BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
    }

    @PostMapping
    public ResponseEntity<BibliotecaResponseDTO> registrar(@Valid @RequestBody BibliotecaRequestDTO request) {
        BibliotecaResponseDTO creada = bibliotecaService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<BibliotecaResponseDTO>> listar() {
        return ResponseEntity.ok(bibliotecaService.listar());
    }
}
