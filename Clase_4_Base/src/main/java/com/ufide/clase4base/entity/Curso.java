package com.ufide.clase4base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity; //para el entity
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Modelo de datos del Curso.
 *
 * CLASE 4: convertir esta clase en una Entity de JPA agregando:
 *   - @Entity
 *   - @Table(name = "cursos")
 *   - @Id  +  @GeneratedValue(strategy = GenerationType.IDENTITY)  sobre el id
 *   - @Column(nullable = false)  sobre el nombre
 *
 * Por ahora es solo un POJO que vive en memoria.
 */

@Entity //para la comuniacion con la base de datos
@Table(name = "cursos") //para crear la tabla de la base de datos
public class Curso {

    @Id //para la base de datos
    @GeneratedValue( strategy= GenerationType.IDENTITY) //para la base de datos
    private Long id;

    @Column(nullable= false) //columanas en la base de datos
    private String nombre;
    private String descripcion;
    private int creditos;
    private String profesor;

    /** Constructor vacio - obligatorio cuando esta clase pase a ser @Entity. */
    public Curso() {}

    public Curso(Long id, String nombre, String descripcion, int creditos, String profesor) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.profesor = profesor;
    }

    // Getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }

    public String getProfesor() { return profesor; }
    public void setProfesor(String profesor) { this.profesor = profesor; }
}
