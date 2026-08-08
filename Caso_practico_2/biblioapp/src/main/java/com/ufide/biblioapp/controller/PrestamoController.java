package com.ufide.biblioapp.controller;

import com.ufide.biblioapp.entity.Libro;
import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.service.LibroService;
import com.ufide.biblioapp.service.PrestamoService;
import com.ufide.biblioapp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/p")
public class PrestamoController {

    // ==========================================================
    // CASO PRACTICO 2 - REQUISITO 2:
    // Aca vas a agregar las rutas de PrestamoController (o un
    // controller nuevo PrestamoController.java) para registrar
    // prestamos y devoluciones, protegidas con @PreAuthorize
    // segun el Requisito 3.
    // ==========================================================

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private LibroService libroService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/prestamos")
    public String listar(Model model,  Authentication authentication) {
        Usuario usuario = usuarioService.buscarPorUsername(authentication.getName());
        if (usuario.getRol().equals("BIBLIOTECARIO")) {
            model.addAttribute("prestamos", prestamoService.findAllByUsuario());
            model.addAttribute("atrasados", prestamoService.atrasados());
        }
        else {
            model.addAttribute("prestamos", prestamoService.findByusuario(usuario));
        }
         return "Prestamos";
    }

    @GetMapping("/prestamos/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("prestamo", prestamoService.findConLibro(id).orElse(null));
        return "Prestamo-detalle";
    }

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @GetMapping("/nuevo/{id}")
    public String mostrarCrearPrestamo(Model modelo, @PathVariable Long id) {
        Prestamo prestamo = new Prestamo();
        Libro libro = libroService.buscarPorId(id).orElse(null);
        prestamo.setLibro(libro);
        modelo.addAttribute("prestamo", prestamo);
        modelo.addAttribute("libro", prestamo.getLibro());
        modelo.addAttribute("usuarios", usuarioService.buscarTodos());
        return "prestamos/form";
    }

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @PostMapping("/nuevo/post")
    public String guardar(@Valid @ModelAttribute("prestamo") Prestamo prestamo,
                          BindingResult result, Model modelo) {
        if (result.hasErrors()) {
            modelo.addAttribute("usuarios", usuarioService.buscarTodos());
            modelo.addAttribute("libro", prestamo.getLibro()); //para que se mantenga y no lanze error
            return "prestamos/form";
        }
        libroService.descontarCopia(prestamo.getLibro());
        prestamo.setFechaLimite(prestamo.getFechaPrestamo().plusDays(14));
        prestamoService.save(prestamo);
        return "redirect:/p/prestamos";
    }

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @PostMapping("/prestamos/{id}/devolucion")
    public String devolucion(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.buscarPrestamo(id).orElse(null);
        prestamo.setFechaDevolucion(LocalDate.now());
        prestamoService.save(prestamo);
        libroService.agregarCopia(prestamo.getLibro());
        return "redirect:/p/prestamos/" + id;
    }
    
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @PostMapping("/prestamos/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        prestamoService.delete(id);
        return "redirect:/p/prestamos";
    }


}
