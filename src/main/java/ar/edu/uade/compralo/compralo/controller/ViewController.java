package ar.edu.uade.compralo.compralo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Importante: Usar @Controller para servir vistas (HTML)
public class ViewController {

    /**
     * Mapea la URL principal a la plantilla catalogo.html
     */
    @GetMapping({"/catalogo.html"})
    public String catalogo() {
        return "catalogo";
    }

    /**
     * Mapea la URL /carrito.html a la plantilla carrito.html
     */
    @GetMapping("/carrito.html")
    public String carrito() {
        return "carrito";
    }
}