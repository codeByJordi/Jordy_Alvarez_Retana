package com.ufide.biblioapp;

import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BiblioappApplication implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(BiblioappApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0 && !usuarioRepository.findByUsername("biblotecario1").isPresent() && !usuarioRepository.findByUsername("lector").isPresent()) {
            Usuario usuario = new Usuario();
            usuario.setUsername("bibliotecaria1");
            usuario.setPassword(passwordEncoder.encode("password123"));
            usuario.setEmail("bibi@gmail.com");
            usuario.setRol("BIBLIOTECARIO");
            usuario.setNombreCompleto("Bibliotecaria jordi");
            usuarioRepository.save(usuario);

            Usuario lector = new Usuario();
            lector.setUsername("lector");
            lector.setPassword(passwordEncoder.encode("password123"));
            lector.setEmail("lector@gmail.com");
            lector.setRol("LECTOR");
            lector.setNombreCompleto("lector jordi");
            usuarioRepository.save(lector);

        }



    }
}
