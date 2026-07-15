package com.ufide.clase4base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.clase4base.entity.Curso;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CursoRepository
        extends JpaRepository<Curso, Long> {

    @Query("Select c from Curso c join Fetch c.profesor")
    List<Curso> findAllByProfesor();

}