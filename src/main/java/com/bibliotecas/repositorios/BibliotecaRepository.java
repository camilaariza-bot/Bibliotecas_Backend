package com.bibliotecas.repositorios;

import com.bibliotecas.entidades.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BibliotecaRepository extends JpaRepository<Biblioteca, Long> {

    /**
     * Trae todas las bibliotecas junto con sus libros en una sola consulta
     * (LEFT JOIN FETCH), incluidas las bibliotecas que aún no tienen libros.
     * Evita el problema de N+1 consultas.
     */
    @Query("SELECT DISTINCT b FROM Biblioteca b LEFT JOIN FETCH b.libros ORDER BY b.id")
    List<Biblioteca> findAllConLibros();
}
