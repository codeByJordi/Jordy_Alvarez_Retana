package com.semana2.primerproyecto;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //muy importante
public class homeController {

    @GetMapping("/")               //para adjuntar variables
    public String home(Model model /*,@RequestParam String nombre1*/){
        model.addAttribute("nombre", "jordi");  //'nombre' es el th:text="${nombre}" en el html
        return "home"; //nombre debe calzar con el del html
    }


}
