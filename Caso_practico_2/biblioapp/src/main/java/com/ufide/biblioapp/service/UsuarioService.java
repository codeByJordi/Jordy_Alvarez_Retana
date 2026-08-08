package com.ufide.biblioapp.service;

import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.repository.UsuarioRepository;
import com.ufide.biblioapp.security.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        validarRol(usuario.getRol());


        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol())))
                .build();

    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    // ==========================================================
    // CASO PRACTICO 2 - BONUS (CRUD completo de Usuarios):
    // Si vas a implementar el bonus, agrega aca los metodos
    // listar/guardar/eliminar usuarios, con la misma logica de
    // validarRol(...) que viste en la Semana 12 (UsuarioService
    // de cursosapp).
    // ==========================================================
    private void validarRol(String rol) {
        try {
            Rol.valueOf(rol.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(
                    "Rol invalido: " + rol + ". Debe ser uno de: " + Arrays.toString(Rol.values()));
        }
    }

    public String crearToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuario.setTokenExpiration(LocalDateTime.now().plusMinutes(30));
        usuarioRepository.save(usuario);
        return token;
    }
    public Optional<Usuario> findByResetToken(String token) {
        return usuarioRepository.findByResetToken(token).filter(u -> u.getTokenExpiration().isAfter(LocalDateTime.now()) && u.getResetToken() != null);
    }
    public void restablecerContra(Usuario usuario, String contra) {
        usuario.setPassword(passwordEncoder.encode(contra));
        usuario.setTokenExpiration(null);
        usuario.setResetToken(null);
        usuarioRepository.save(usuario);
    }

}
