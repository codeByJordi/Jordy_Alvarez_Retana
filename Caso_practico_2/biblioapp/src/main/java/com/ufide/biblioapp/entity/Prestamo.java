package com.ufide.biblioapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "prestamo")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "No puede ser null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libroid", nullable = false)
    private Libro libro;

    @NotNull(message = "Usuario obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioid", nullable = false)
    private Usuario usuario;

    @NotNull
    @Column
    @FutureOrPresent(message = "Debe ser dia que se realiza el prestamo")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaPrestamo;

    @NotNull
    @Column
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaLimite;

    @Column
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaDevolucion;

    public Prestamo(Libro libro, Usuario usuario, LocalDate fechaPrestamo, LocalDate fechaLimite, LocalDate fechaDevolucion) {
        this.libro = libro;
        this.usuario = usuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
    }

    public Prestamo() {

    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
