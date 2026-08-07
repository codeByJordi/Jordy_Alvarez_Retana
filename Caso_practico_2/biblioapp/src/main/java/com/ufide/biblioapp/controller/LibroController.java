package com.ufide.biblioapp.controller;

import com.ufide.biblioapp.entity.Libro;
import com.ufide.biblioapp.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping("/libros")
    public String listar(Model model) {
        model.addAttribute("libros", libroService.listar());
        return "libros";
    }

    @GetMapping("/libros/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("libro", libroService.buscarPorId(id).orElse(null));
        return "libro-detalle";
    }

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @GetMapping("/libros/nuevo")
    public String mostrarLibroNuevo(Model model) {
        Libro libro = new Libro();
        model.addAttribute("libro", libro);
        return "libros/form";
    }

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @PostMapping("/libros")
    public String PostLibroNuevo(@Valid @ModelAttribute("libro") Libro libro, BindingResult result) {
        if (result.hasErrors()) {
            return "libros/form";
        }
        libroService.guardar(libro);
        return "redirect:/libros";
    }

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @GetMapping("/libros/{id}/editar")
    public String mostrarEditor(@PathVariable Long id, Model model) {
        model.addAttribute("libro", libroService.buscarPorId(id).orElse(null));
        return "libros/form";
    }

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @PostMapping("/libros/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        libroService.eliminar(id);
        return "redirect:/libros";
    }


}
