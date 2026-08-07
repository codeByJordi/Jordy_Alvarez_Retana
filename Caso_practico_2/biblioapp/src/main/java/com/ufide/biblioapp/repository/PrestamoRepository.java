package com.ufide.biblioapp.repository;

import com.ufide.biblioapp.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    @Query("SELECT u FROM Prestamo u JOIN FETCH u.usuario")
    List<Prestamo> findAllConUsuario();

    @Query("SELECT u FROM Prestamo u JOIN FETCH u.libro")
    Optional<Prestamo> findConLibro(Long id);
}
