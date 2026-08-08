package com.ufide.biblioapp.repository;

import com.ufide.biblioapp.entity.Libro;
import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    @Query("SELECT u FROM Prestamo u JOIN FETCH u.usuario")
    List<Prestamo> findAllConUsuario();

    @Query("SELECT u FROM Prestamo u JOIN FETCH u.libro WHERE u.id = :id") //tuve error aqui
    Optional<Prestamo> findConLibro(Long id);

    List<Prestamo> findByUsuario(Usuario usuario);

    @Query("Select p From Prestamo p Where p.fechaDevolucion is NULL AND p.fechaLimite < CURRENT_DATE ") //tuve error aqui, nota para escribir
    List<Prestamo> prestamosAtrasados();

    void deleteByLibro(Libro libro);

}
