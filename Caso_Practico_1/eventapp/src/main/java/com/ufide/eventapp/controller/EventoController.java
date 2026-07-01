package com.ufide.eventapp.controller;

import com.ufide.eventapp.EventappApplication;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.ufide.eventapp.entity.Evento;
import com.ufide.eventapp.service.EventoService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;



/**
 * Controlador de eventos - estado base del Caso Practico 1.
 *
 * Endpoints ya implementados:
 *   GET /eventos          -> listar todos
 *   GET /eventos/{id}     -> detalle
 *
 * Endpoints que tenes que implementar (Caso Practico):
 *   GET  /eventos/categoria/{categoria}   -> filtrar por categoria (endpoint paramétrico)
 *   GET  /eventos/nuevo                   -> mostrar form vacio
 *   POST /eventos                         -> guardar nuevo con validaciones
 *   GET  /eventos/{id}/editar             -> mostrar form precargado
 *   POST /eventos/{id}                    -> actualizar
 *   POST /eventos/{id}/eliminar           -> borrar
 */
@Controller
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("eventos", service.listar());
        return "eventos";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Evento evento = service.buscarPorId(id).orElse(null);
        model.addAttribute("evento", evento);
        return "evento";
    }

    // TODO Caso Practico 1: agregar aca los endpoints del CRUD y el GET con parametro.
    //Para ver categorias
    @GetMapping("/buscar")
    public String buscar(Model model, @RequestParam(required = false) String buscar) {
        if(buscar != null && !buscar.isBlank()) {
            model.addAttribute("eventos", service.buscarPorNombreContainingIgnoreCase(buscar));
        }
        else {
            model.addAttribute("eventos", service.listar());
        }
        return "eventos";
    }


    //Para crea un nuevo evento
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("evento", new Evento());
        return "eventos/form";
    }
    @PostMapping
    public String guardar(@Valid @ModelAttribute("evento") Evento evento, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "eventos/form";
        }
        service.guardar(evento);
        ra.addFlashAttribute("ok", "Evento guardado correctamente");
        return "redirect:/eventos";
    }
//
//    //Para editar eventos
    @GetMapping("/{id}/editar")
    public String eventoEditar(@PathVariable Long id, Model model) {
        Evento evento = service.buscarPorId(id).orElse(null);
        model.addAttribute("evento", evento);
        return "eventos/form";
    }
    @PostMapping("/{id}")
    public String actualizar(@Valid @ModelAttribute("evento") Evento evento, BindingResult result, @PathVariable Long id, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "eventos/form";
        }
        evento.setId(id);
        service.guardar(evento);
        ra.addFlashAttribute("ok", "Evento guardado correctamente");
        return "redirect:/eventos";
    }

//    //Para eliminar
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        service.eliminar(id);
        ra.addFlashAttribute("ok", "Evento eliminado correctamente");
        return "redirect:/eventos";
    }
}
