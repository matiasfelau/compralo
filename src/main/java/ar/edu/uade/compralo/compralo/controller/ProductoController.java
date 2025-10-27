package ar.edu.uade.compralo.compralo.controller;

import ar.edu.uade.compralo.compralo.model.dto.ProductoDTO;
import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.service.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@AllArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        List<Producto> productos = productoService.encontrarTodosProductos();
        List<ProductoDTO> dtos = productos.stream()
                .map(p -> new ProductoDTO(p.getId(), p.getNombre(), p.getPrecio()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
