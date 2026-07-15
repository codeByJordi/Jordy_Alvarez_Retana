package com.ufide.clase4base.repository;

import com.ufide.clase4base.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesorRepository
        extends JpaRepository<Profesor, Long> {
}
