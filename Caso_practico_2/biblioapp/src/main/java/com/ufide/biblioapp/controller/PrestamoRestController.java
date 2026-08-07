package com.ufide.biblioapp.controller;

import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoRestController {
    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/atrasados")
    public List<Prestamo> prestamosAtrasados() {
        return prestamoService.atrasados();
    }
}
