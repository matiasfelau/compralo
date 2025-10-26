package ar.edu.uade.compralo.compralo.controller;

import ar.edu.uade.compralo.compralo.dto.ProductoDTO;
import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.service.CarritoService;
import ar.edu.uade.compralo.compralo.service.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carrito")
@AllArgsConstructor
public class CarritoController {
    private final CarritoService carritoService;
    private final ProductoService productoService;

    /**
     * Recibe el carrito del usuario como JSON array de ProductoDTO (sufre con solo el id)
     * y devuelve recomendaciones (Set<ProductoDTO>).
     *
     * Nota: uso POST porque el cuerpo contiene un conjunto complejo; si prefieres GET
     * con query params puedo añadirlo.
     */
    @PostMapping("/recomendaciones")
    public ResponseEntity<Set<ProductoDTO>> obtenerRecomendaciones(@RequestBody Set<ProductoDTO> carritoDto) {
        Set<Producto> carrito = carritoDto.stream()
                .map(dto -> dto.getId() == null ? null : productoService.encontrarProducto(dto.getId()))
                .filter(p -> p != null)
                .collect(Collectors.toSet());

        Set<Producto> recomendados = carritoService.obtenerRecomendaciones(carrito);

        Set<ProductoDTO> result = recomendados.stream()
                .map(p -> new ProductoDTO(p.getId(), p.getNombre(), p.getPrecio()))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/envio-gratis")
    public ResponseEntity<Set<ProductoDTO>> obtenerEnvioGratis(@RequestBody Set<ProductoDTO> carritoDto) {
        Set<Producto> carrito = carritoDto.stream()
                .map(dto -> dto.getId() == null ? null : productoService.encontrarProducto(dto.getId()))
                .filter(p -> p != null)
                .collect(Collectors.toSet());

        Set<Producto> seleccion = carritoService.obtenerEnvioGratis(carrito);

        Set<ProductoDTO> result = seleccion.stream()
                .map(p -> new ProductoDTO(p.getId(), p.getNombre(), p.getPrecio()))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(result);
    }
}
