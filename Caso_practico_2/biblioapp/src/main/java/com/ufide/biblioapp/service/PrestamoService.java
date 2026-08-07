package com.ufide.biblioapp.service;

import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.repository.PrestamoRepository;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    public Optional<Prestamo> buscarPrestamos(Long id){
        return prestamoRepository.findById(id);
    }

    public List<Prestamo> findAll(){
        return prestamoRepository.findAll();
    }

    public Optional<Prestamo> findConLibro(Long id){
        return prestamoRepository.findConLibro(id);
    }

    public List<Prestamo> findAllByUsuario(){
        return prestamoRepository.findAllConUsuario();
    }

    public Prestamo save(Prestamo prestamo){
        return prestamoRepository.save(prestamo);
    }


}
