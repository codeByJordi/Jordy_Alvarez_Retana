package com.ufide.clase4base.repository;


import com.ufide.clase4base.entity.*;


import org.springframework.data.jpa.repository.JpaRepository;


public interface cursoRepository extends JpaRepository<Curso, Long>{ //curso es el curso.java de entity
    
}
