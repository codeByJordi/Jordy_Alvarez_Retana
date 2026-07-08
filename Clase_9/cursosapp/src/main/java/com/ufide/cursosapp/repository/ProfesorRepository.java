package com.ufide.cursosapp.repository;

import com.ufide.cursosapp.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ufide.cursosapp.entity.Profesor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProfesorRepository
        extends JpaRepository<Profesor, Long> {


}
