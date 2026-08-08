package com.ufide.biblioapp.controller;

import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.service.EmailService;
import com.ufide.biblioapp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContraController {
    @Autowired
    private UsuarioService usuarioSer;
    @Autowired
    private EmailService emailService;

    @GetMapping("/restablecer")
    public String muestraRestablecer(){
        return "restablecer";
    }

    @PostMapping("/restablecer/proceso")
    public String procesoRestablecer(@RequestParam String email) {
        usuarioSer.findByEmail(email).ifPresent(usuario -> {
            String token = usuarioSer.crearToken(usuario);
            String enlace = "http://localhost:8080/restablecer-password?token=" + token;
            emailService.enviarMensaje(usuario.getEmail(), enlace);
        });
        return "redirect:/login";
    }

    @GetMapping("/restablecer-password")
    public String muestraRestablecerPassword(@RequestParam String token, Model model) {
        Usuario usuario = usuarioSer.findByResetToken(token).orElse(null);
        if(usuario == null){
            model.addAttribute("mensaje", true);
            return "redirect:/restablecer";
        }

        model.addAttribute("token", token);
        return "restablecer-password";
    }
    @PostMapping("/restablecer-password")
    public String PostRestablecerPassword(@RequestParam String token, Model model, @RequestParam String contrasena) {
        Usuario usuario = usuarioSer.findByResetToken(token).orElse(null);
        if(usuario == null){
            model.addAttribute("mensaje", true);
            return "redirect:/restablecer";
        }
        if(contrasena.length() < 8){
            model.addAttribute("error", true);
            model.addAttribute("token", token);
            return "restablecer-password";
        }
        usuarioSer.restablecerContra(usuario, contrasena);
        return "redirect:/login";
    }

}
