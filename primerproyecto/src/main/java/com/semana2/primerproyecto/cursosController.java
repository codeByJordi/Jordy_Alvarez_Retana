package com.semana2.primerproyecto;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class cursosController {

    @GetMapping("cursos") //NUEVA RUTA 1
    public String getMethodName(Model modelo) {
        String cursos[] = {"Java", "Python"};
        modelo.addAttribute("Listacursos", cursos); //new String[] {"Java", "Python"}
        return "cursos";
    }

    @GetMapping("/{cursoID}") //NUEVA RUTA 2
    public String getMethodName(@PathVariable String cursoID, Model modelo) {
        modelo.addAttribute("cursoID", "hOLA");
        return "curso";
    }
    
    

}
