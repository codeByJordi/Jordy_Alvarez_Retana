package com.ufide.clase4base.service;

import com.ufide.clase4base.entity.Profesor;
import com.ufide.clase4base.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorService {
    @Autowired
    ProfesorRepository profesorRepository;

    public List<Profesor> findAllProfesor(){
        return profesorRepository.findAll();
    }
}
