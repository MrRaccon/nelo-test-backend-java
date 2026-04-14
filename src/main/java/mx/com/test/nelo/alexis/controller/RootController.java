package mx.com.test.nelo.alexis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    
    @GetMapping("/")
    public String holaMundo() {
        return "¡Hola Mundo desde Nelo Test Backend!";
    }
}
