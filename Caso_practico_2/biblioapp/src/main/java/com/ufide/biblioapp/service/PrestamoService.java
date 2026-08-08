package com.ufide.biblioapp.service;

import com.ufide.biblioapp.entity.Libro;
import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.repository.PrestamoRepository;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public List<Prestamo> atrasados(){
        return prestamoRepository.prestamosAtrasados();
    }

    public List<Prestamo> findByusuario(Usuario usuario){
        return prestamoRepository.findByUsuario(usuario);
    }

    public Optional<Prestamo> buscarPrestamo(Long id){
        return prestamoRepository.findById(id);
    }

    public List<Prestamo> findAllByUsuario(){
        return prestamoRepository.findAllConUsuario();
    }

    public Prestamo save(Prestamo prestamo){
        return prestamoRepository.save(prestamo);
    }

    public void delete(Long id){
        prestamoRepository.deleteById(id);
    }
    @Transactional //sin esto, no se puede eliminar
    public void  deleteLibro(Libro libro){
        prestamoRepository.deleteByLibro(libro);
    }

}
