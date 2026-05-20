package com.semana2.primerproyecto;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //muy importante
public class homeController {

    @GetMapping("/")
    public String home(Model model){
        
        model.addAttribute("Jordy", "estudiante"); //nombre debe calzar con el del htlm
        return "home"; 
    }

}
