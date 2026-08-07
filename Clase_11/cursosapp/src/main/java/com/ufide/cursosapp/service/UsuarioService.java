package com.ufide.cursosapp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ufide.cursosapp.entity.Usuario;
import com.ufide.cursosapp.repository.UsuarioRepository;

// CRUD de usuarios (Parte F) + generacion/validacion de tokens de
// recuperacion de contrasena (Parte G). El PasswordEncoder es el MISMO bean
// que ya existe en SecurityConfig desde S10 - se reutiliza, no se crea uno
// nuevo (seria un segundo hasher inconsistente con el que valida el login).
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listar() {
        return repo.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return repo.findById(id);
    }

    // Se usa al CREAR un usuario nuevo desde el formulario: la contrasena
    // llega en texto plano y hay que hashearla ANTES de guardar. Sin este
    // paso, el login del nuevo usuario fallaria (BCrypt nunca reconoceria
    // un password en texto plano como valido).
    public Usuario crear(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return repo.save(usuario);
    }

    // Se usa al EDITAR: si el campo password del formulario viene vacio, el
    // admin no quiso cambiarla - se conserva el hash existente. Por eso el
    // Controller NO usa @Valid en el metodo actualizar() (el password puede
    // llegar en blanco a proposito).
    public Usuario actualizar(Long id, Usuario formUsuario) {
        Usuario existente = repo.findById(id).orElseThrow();
        existente.setUsername(formUsuario.getUsername());
        existente.setEmail(formUsuario.getEmail());
        existente.setRol(formUsuario.getRol());
        if (formUsuario.getPassword() != null && !formUsuario.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(formUsuario.getPassword()));
        }
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    

    public Optional<Usuario> buscarPorEmail(String email){
        return repo.findByEmail(email);
    }


    //Crear token
    public String generarTokenReset(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(30);
        usuario.setResetToken(token);
        usuario.setResetTokenExpiracion(expiracion);
        //ojo, esto no son column ni nada, entonces no son obligatorias crear al inicio al guardar,
        //pero aqui se guardan en la bbdd sin existir en la tabla y por ende, el buscartoken funcionara
        repo.save(usuario);
        return token;
    }

    //validar token -- o buscar usuario por token si aun es valido
    public Optional<Usuario> validarToken(String token) {
        return repo.findByResetToken(token).filter(u -> u.getResetToken() != null && u.getResetTokenExpiracion().isAfter(LocalDateTime.now()) );
    }

    //reset password
    public void resetPassword(Usuario usuario, String password){
        usuario.setPassword(passwordEncoder.encode(password));
        //se debe resetear a null el token
        usuario.setResetToken(null);
        usuario.setResetTokenExpiracion(null);
        repo.save(usuario);
    }


  
}