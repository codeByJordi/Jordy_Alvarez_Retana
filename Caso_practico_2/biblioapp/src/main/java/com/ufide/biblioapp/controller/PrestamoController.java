package com.ufide.biblioapp.controller;

import com.ufide.biblioapp.entity.Libro;
import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.service.LibroService;
import com.ufide.biblioapp.service.PrestamoService;
import com.ufide.biblioapp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/Prestamos")
    public String listar(Model model) {
        model.addAttribute("prestamos", prestamoService.findAllByUsuario());
        return "Prestamos";
    }

    @GetMapping("/Prestamos/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("prestamo", prestamoService.findConLibro(id).orElse(null));
        return "Prestamo-detalle";
    }

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @GetMapping("/nuevo/{id}")
    public String mostrarFormNuevo(Model modelo, @PathVariable Long id) {
        Prestamo prestamo = new Prestamo();
        Libro libro = libroService.buscarPorId(id).orElse(null);
        prestamo.setLibro(libro);
        modelo.addAttribute("prestamo", prestamo);
        modelo.addAttribute("libro", prestamo.getLibro());
        modelo.addAttribute("usuarios", usuarioService.buscarTodos());
        return "prestamos/form";
    }

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @PostMapping("/nuevo")
    public String guardar(@Valid @ModelAttribute("prestamo") Prestamo prestamo,
                          BindingResult result, Model modelo) {
        if (result.hasErrors()) {
            modelo.addAttribute("usuarios", usuarioService.buscarTodos());
            return "prestamos/form";
        }
        prestamoService.save(prestamo);
        return "redirect:/Prestamos";
    }



}
