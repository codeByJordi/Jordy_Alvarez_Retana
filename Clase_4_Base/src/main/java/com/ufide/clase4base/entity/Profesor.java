package com.ufide.clase4base.entity;

import javax.persistence.*;

import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Profesor")
public class Profesor {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;


    @Size(max = 100)
    @NotBlank
    private String nombre;

    @Size(max = 100)
    @Email
    private String email;

    @Size(max = 80)
    private String especialidad;

    public Profesor() {

    }

    public Profesor(Long id, String nombre, String email, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.especialidad = especialidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
