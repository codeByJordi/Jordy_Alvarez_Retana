package com.ufide.cursosapp.controller;


import com.ufide.cursosapp.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ufide.cursosapp.service.EmailService;
import com.ufide.cursosapp.service.UsuarioService;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class PasswordResetController {

    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/olvide-password")
    public String olvidePassword() {
        return "olvide-password";
    }

    @PostMapping("/olvide-password")
    public String postOlvidePassword(@RequestParam(name="email") String email) {
        usuarioService.buscarPorEmail(email).ifPresent(usuario -> {
            String token = usuarioService.generarTokenReset(usuario);
            String enlace = "http://localhost:8080/restablecer-password?token=" + token;
            emailService.enviarMensaje(usuario.getEmail(), enlace);
        });
        return "olvide-password";
    }

    @GetMapping("/restablecerPassword")
    public String restablecerPassword(@RequestParam String token, Model model){ //el token esta en un input hidden
        Usuario usuario = usuarioService.validarToken(token).orElse(null);
        if(usuario == null) {
            model.addAttribute("tokenInvalido", true); //el true porque usamos un if y unless en el html
            return "olvide-password";
        }
        return "restablecer-password";
    }

    @PostMapping("/restablecerPassword")
    public String postRestablecerPassword(@RequestParam String token, Model model, @RequestParam String password){ //el token esta en un input hidden
        Usuario usuario = usuarioService.validarToken(token).orElse(null);
        if(usuario == null) {
            model.addAttribute("tokenInvalido", true); //el true porque usamos un if y unless en el html
            return "olvide-password";
        }
        usuarioService.resetPassword(usuario, password);
        return "redirect:/login";
    }

    
    
    
}
