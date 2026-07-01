package com.ufide.eventapp.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Entidad Evento - representa un evento publico (concierto, taller, charla...).
 *
 * NOTA IMPORTANTE para el Caso Practico 1:
 *   Esta entidad tiene los campos pero NO tiene validaciones.
 *   Como parte del examen tenes que aplicar las anotaciones de
 *   Bean Validation que veas necesarias (@NotBlank, @Size, @Future, etc).
 *
 *   Tampoco hay metodos util tipo isLleno() o isProximo() - si te sirven
 *   para la vista, podes agregarlos.
 */
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo no puede estar vacio")
    @Size(min = 1, max = 50, message = "Minimo un caracter y maximo 50")
    @Column(nullable = false, length = 120)
    private String nombre;

    @Size(min = 0, max = 500, message = "Maximo 500")
    @Column(length = 500)
    private String descripcion;

    /** Fecha del evento (sin hora). */
    @NotNull(message = "Fecha es obligatoria")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Future(message = "Fecha debe ser a futuro")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotBlank(message = "Debe agregar un lugar")
    @Column(length = 100)
    private String lugar;

    /** Categoria libre: "Musica", "Conferencia", "Deporte", "Taller", etc. */
    @NotBlank(message = "Debe agregar una categoria")
    @Column(length = 50)
    private String categoria;

    @NotBlank(message = "Debe agregar un organizador")
    @Column(length = 80)
    private String organizador;

    /** Cupo total disponible. */
    @Min(value = 1, message = "Cupo minimo 1")
    private int cupoMaximo;

    /** Tickets ya vendidos. */
    @Max(value = 10000)
    @Min(value = 0)
    private int cuposVendidos;

    /** Precio de la entrada (0 si es gratis). */
    @Max(value = 10000, message = "Precio maximo 100000")
    @Min(value = 0, message = "Precio minimo 500")
    private double precio;

    public Evento() {}

    public Evento(String nombre, String descripcion, LocalDate fecha, String lugar,
                  String categoria, String organizador,
                  int cupoMaximo, int cuposVendidos, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.lugar = lugar;
        this.categoria = categoria;
        this.organizador = organizador;
        this.cupoMaximo = cupoMaximo;
        this.cuposVendidos = cuposVendidos;
        this.precio = precio;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getOrganizador() { return organizador; }
    public void setOrganizador(String organizador) { this.organizador = organizador; }

    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public int getCuposVendidos() { return cuposVendidos; }
    public void setCuposVendidos(int cuposVendidos) { this.cuposVendidos = cuposVendidos; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
